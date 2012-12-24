package net.ilx.actor.server.spring.conf;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.ilx.actor.server.jobs.JobRegistry;
import net.ilx.actor.server.spring.components.commands.EnableCommands;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import fi.jumi.actors.ActorThread;
import fi.jumi.actors.Actors;
import fi.jumi.actors.MultiThreadedActors;
import fi.jumi.actors.eventizers.dynamic.DynamicEventizerProvider;
import fi.jumi.actors.listeners.CrashEarlyFailureHandler;
import fi.jumi.actors.listeners.MessageListener;
import fi.jumi.actors.listeners.NullMessageListener;

@Configuration("ActorConfiguration")
@EnableCommands
//@EnableAspectJAutoProxy(proxyTargetClass=true)
public class ActorJobConfiguration {

	private static final Logger LOG = Logger.getLogger(ActorJobConfiguration.class);

	@Bean
	public ExecutorService actorsThreadPool() {
		LOG.debug("starting actor thread pool (Executor Service)");
	    ExecutorService actorsThreadPool = Executors.newCachedThreadPool();
	    return actorsThreadPool;
	}


//	@Bean
//	public JobRegistrationAspect jobRegistrationAspect() {
//		return new JobRegistrationAspect();
//	}

	@Bean
	public JobRegistry jobRegistry() {
		return new JobRegistry();
	}

	@Bean
	public MessageListener messageListener() {
//		return new JobMessageListener(jobRegistry());
		return new NullMessageListener();
	}

	@Bean(name="actorThread" )
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode=ScopedProxyMode.TARGET_CLASS)
	public ActorThread actorThread() {
		LOG.debug("creating actor thread");
		// Configure the actors implementation and its dependencies:
        // - Executor for running the actors (when using MultiThreadedActors)
        // - Eventizers for converting method calls to event objects and back again
        // - Handler for uncaught exceptions from actors
        // - Logging of all messages for debug purposes (here disabled)
	    Actors actors = new MultiThreadedActors(
                actorsThreadPool(),
                new DynamicEventizerProvider(),
                new CrashEarlyFailureHandler(),
                messageListener()
        );

	    // Start up a thread where messages to actors will be executed
        ActorThread actorThread = actors.startActorThread();

        return actorThread;
	}

}
