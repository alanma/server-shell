package net.ilx.server.shell.core.server;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ClassUtils;

public class InitializingAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {

	private ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers =
			new ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>>();


	@SuppressWarnings("unchecked")
	@Override
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		String[] initializerNames = beanFactory.getBeanNamesForType(ApplicationContextInitializer.class, false ,false);
		for (String initializer : initializerNames) {
			BeanDefinition initializerBeanDefinition = beanFactory.getBeanDefinition(initializer);
			String beanClassName = initializerBeanDefinition.getBeanClassName();
			ApplicationContextInitializer<ConfigurableApplicationContext> initializerBean;
			try {
				Class<?> initializerClass = ClassUtils.forName(beanClassName, beanFactory.getBeanClassLoader());
				initializerBean = BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
				this.contextInitializers.add(initializerBean);
			} catch (Throwable t) {
				String msg = String.format("Unable to initialize ApplicationContextInitializer: %s.", beanClassName);
				throw new IllegalStateException(msg, t);
			}
		}

		Collections.sort(this.contextInitializers, new AnnotationAwareOrderComparator());
		for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
			initializer.initialize(this);
		}


		super.prepareBeanFactory(beanFactory);
	}

}
