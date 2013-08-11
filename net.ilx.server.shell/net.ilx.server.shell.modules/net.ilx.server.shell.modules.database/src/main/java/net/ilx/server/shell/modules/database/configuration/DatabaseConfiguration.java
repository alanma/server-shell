package net.ilx.server.shell.modules.database.configuration;

import net.ilx.server.shell.core.utils.alf.server.spring.beans.factory.support.ExtendedPropertiesBeanDefinitionReader;
import net.ilx.server.shell.modules.database.commands.DatabaseCommands;

import org.jboss.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

/**
 * Database configuration.
 *
 * It dynamically registers bean definitions. Because of
 * this, most of the time, it has to be run after other
 * BeanDefinitionRegistryPostProcessors (for example before
 * EncryptConfiguration) is invoked.
 * That's why it has priority of LOWEST_PRECEDENCE.
 *
 * It reads bean defitions that start with 'dataSource' prefix from any available PropertySource.
 * Bean definitions are in the same format as described in {@link PropertiesBeanDefinitionReader}.
 *
 * @author ilonca
 *
 */
@Configuration
@PropertySource("classpath:/database.properties")
//@EnableEncryptedPropertySources
public class DatabaseConfiguration implements EnvironmentAware, BeanDefinitionRegistryPostProcessor, Ordered {
    private static final Logger LOG = Logger.getLogger(DatabaseConfiguration.class);

    private ConfigurableEnvironment env;

    @Override
    public void setEnvironment(Environment environment) {
        env = (ConfigurableEnvironment) environment;
    }

    @Bean
    public DatabaseCommands databaseCommands() {
        return new DatabaseCommands();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // nothing
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registerDataSourcesIntoContext(registry);
    }

    private void registerDataSourcesIntoContext(BeanDefinitionRegistry beanDefinitionRegistry) {
        ExtendedPropertiesBeanDefinitionReader reader = new ExtendedPropertiesBeanDefinitionReader(beanDefinitionRegistry);
        reader.setEnvironment(env);
        MutablePropertySources propertySources = env.getPropertySources();
        int numberOfDataSources = reader.loadBeanDefinitions(propertySources, "dataSource");

        LOG.debugv("Registered {0} datasources (including abstract beans).", numberOfDataSources);
    }


    public int getOrder() {
        // make sure we add beans after other
        // BeanDefinitionRegistryPostProcessors - you might need to fine tune
        // this
        return PriorityOrdered.LOWEST_PRECEDENCE;
    }

}
