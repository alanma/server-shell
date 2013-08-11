package net.ilx.server.shell.modules.jolokia;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.sql.DataSource;

import net.ilx.server.shell.modules.jolokia.beans.Logging;
import net.ilx.server.shell.modules.jolokia.model.SafeConnectionFactory;
import net.ilx.server.shell.modules.jolokia.model.SafeDataSource;

import org.jolokia.jvmagent.JolokiaServer;
import org.jolokia.jvmagent.JolokiaServerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.export.assembler.InterfaceBasedMBeanInfoAssembler;
import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jmx.support.RegistrationPolicy;

@Configuration
public class JmxConfiguration implements BeanFactoryPostProcessor, EnvironmentAware {
	private static final String DEFAULT_JOLOKIA_HOST = "*";
	private static final String DEFAULT_JOLOKIA_PORT= "9798";
	private static final String PROPERTY_JOLOKIA_PORT = "jolokia.port";
	private static final String PROPERTY_JOLOKIA_HOST = "jolokia.host";
	private static final String KEY_HOST = "host";
	private static final String KEY_PORT = "port";
	private static final String DEFAULT_DOMAIN = "alf";
	private static final String TYPE_DATA_SOURCE = "DataSource";
	private static final String TYPE_CONNECTION_FACTORY = "ConnectionFactory";

	@Autowired
	private Environment env;

	@Bean
	public JolokiaServer jolokiaAgent() throws IOException {
		boolean lazy = false;
		JolokiaServer server = new JolokiaServer(jolokiaConfig(), lazy);
		server.start();
		return server;
	}

	@Bean
	public JolokiaServerConfig jolokiaConfig() {
		Map<String, String> pConfig = new HashMap<String, String>();
		String jolokia_host = env.getProperty(PROPERTY_JOLOKIA_HOST, String.class, DEFAULT_JOLOKIA_HOST);
		pConfig.put(KEY_HOST, jolokia_host);
		String jolokia_port = env.getProperty(PROPERTY_JOLOKIA_PORT, String.class, DEFAULT_JOLOKIA_PORT);
		pConfig.put(KEY_PORT, jolokia_port);
		JolokiaServerConfig config = new JolokiaServerConfig(pConfig);
		return config;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		registerDataSourcesWithMBeanServer(beanFactory);
		registerConnectionPools(beanFactory);
	}

	private void registerLogging(ConfigurableListableBeanFactory beanFactory) {
		// no need for explicit invocation of this method since we've defined annotationAwareMbeanExporter
		Logging logging = beanFactory.getBean(Logging.class);
		MBeanExporter mbeanExporter = annotationAwareMbeanExporter();
		mbeanExporter.registerManagedResource(logging);
	}

	private void registerDataSourcesWithMBeanServer(ConfigurableListableBeanFactory beanFactory) {
		MBeanExporter exporter = mbeanExporter();
		registerBeansOfType(beanFactory, exporter, DataSource.class, TYPE_DATA_SOURCE);
	}

	private void registerConnectionPools(ConfigurableListableBeanFactory beanFactory) {
		MBeanExporter exporter = mbeanExporter();
		registerBeansOfType(beanFactory, exporter, ConnectionFactory.class, TYPE_CONNECTION_FACTORY);
	}

	private static <T> void registerBeansOfType(ConfigurableListableBeanFactory beanFactory, MBeanExporter exporter, Class<T> beanType, String typeName) {
		Map<String, T> beansOfType = beanFactory.getBeansOfType(beanType);
		Set<String> names = beansOfType.keySet();

		for (String name : names) {
			T bean = beansOfType.get(name);
			String domain = DEFAULT_DOMAIN;
			Hashtable<String, String> table = new Hashtable<String, String>();
			table.put("type", typeName);
			table.put("name", name);
			try {
				ObjectName objectName = new ObjectName(domain, table);
				exporter.registerManagedResource(bean, objectName);
			} catch (Throwable t) {
				throw new BeanCreationException(name, "Unable to register JMX resource for bean.", t);
			}
		}
	}


	@Bean
	public MBeanExporter mbeanExporter() {
		AnnotationMBeanExporter mbeanExporter = new AnnotationMBeanExporter();
		RegistrationPolicy registrationPolicy = RegistrationPolicy.FAIL_ON_EXISTING;
		mbeanExporter.setRegistrationPolicy(registrationPolicy);

		MBeanServerFactoryBean mBeanServerFactoryBean = mbeanServerFactory();
		MBeanServer mBeanServer = mBeanServerFactoryBean.getObject();
		mbeanExporter.setServer(mBeanServer);

		mbeanExporter.setAutodetectMode(MBeanExporter.AUTODETECT_ALL);
		mbeanExporter.setAutodetect(true);
		mbeanExporter.setAssembler(interfaceMbeanInfoAssembler());

		return mbeanExporter;
	}

	@Bean
	public MBeanExporter annotationAwareMbeanExporter() {
		AnnotationMBeanExporter mbeanExporter = new AnnotationMBeanExporter();
		RegistrationPolicy registrationPolicy = RegistrationPolicy.FAIL_ON_EXISTING;
		mbeanExporter.setRegistrationPolicy(registrationPolicy);

		MBeanServerFactoryBean mBeanServerFactoryBean = mbeanServerFactory();
		MBeanServer mBeanServer = mBeanServerFactoryBean.getObject();
		mbeanExporter.setServer(mBeanServer);

		mbeanExporter.setAutodetectMode(MBeanExporter.AUTODETECT_ALL);
		mbeanExporter.setAutodetect(true);

		return mbeanExporter;
	}

	@Bean
	public MBeanInfoAssembler interfaceMbeanInfoAssembler() {
		// SimpleReflectiveMBeanInfoAssembler assembler = new SimpleReflectiveMBeanInfoAssembler();
		InterfaceBasedMBeanInfoAssembler assembler = new InterfaceBasedMBeanInfoAssembler();
		// assembler.setManagedInterfaces(new Class[] {ConnectionPoolMBean.class});
		assembler.setManagedInterfaces(new Class[] { SafeDataSource.class, SafeConnectionFactory.class });

		return assembler;
	}

	@Bean
	public MBeanServerFactoryBean mbeanServerFactory() {
		MBeanServerFactoryBean factory = new MBeanServerFactoryBean();
		factory.setDefaultDomain(DEFAULT_DOMAIN);
		factory.setRegisterWithFactory(true);
		factory.setLocateExistingServerIfPossible(true);

		return factory;
	}

	@Bean
	public Logging logging() {
		Logging logging = new Logging();
		return logging;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

}
