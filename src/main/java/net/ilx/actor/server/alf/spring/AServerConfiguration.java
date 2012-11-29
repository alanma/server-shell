package net.ilx.actor.server.alf.spring;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.ilx.actor.server.ActorServer.Greeter;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.jumi.actors.ActorRef;
import fi.jumi.actors.ActorThread;
import fi.jumi.actors.Actors;
import fi.jumi.actors.MultiThreadedActors;
import fi.jumi.actors.eventizers.dynamic.DynamicEventizerProvider;
import fi.jumi.actors.listeners.CrashEarlyFailureHandler;
import fi.jumi.actors.listeners.NullMessageListener;

@Configuration("AlfConfiguration")
public class AServerConfiguration {

	private static final Logger LOG = Logger.getLogger(AServerConfiguration.class);

	@Bean
	public ExecutorService actorsThreadPool() {
		LOG.debug("starting actor thread pool (Executor Service)");
	    ExecutorService actorsThreadPool = Executors.newCachedThreadPool();
	    return actorsThreadPool;
	}

	@Bean(name="actorThread")
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
                new NullMessageListener()
        );

	    // Start up a thread where messages to actors will be executed
        ActorThread actorThread = actors.startActorThread();

        return actorThread;
	}

	@Bean(name="greeter")
	public ActorRef<Greeter> getGreeter() {
		LOG.debug("creating greeter");
		ActorThread actorThread = actorThread();
        // Create an actor to be executed in this actor thread. Some guidelines:
        // - Never pass around a direct reference to an actor, but always use ActorRef
        // - To avoid confusion, also avoid passing around the proxy returned by ActorRef.tell(),
        // though that may sometimes be warranted when interacting with actor-unaware code
        // or if you wish to avoid the dependency to ActorRef
        ActorRef<Greeter> helloGreeter = actorThread.bindActor(Greeter.class, new Greeter() {
            @Override
            public void sayGreeting(final String name) {
                System.out.println("Hello " + name + " from " + Thread.currentThread().getName());
            }
        });
        return helloGreeter;

	}
}
