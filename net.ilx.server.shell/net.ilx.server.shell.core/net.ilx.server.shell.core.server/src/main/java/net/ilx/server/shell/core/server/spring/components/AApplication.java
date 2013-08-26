package net.ilx.server.shell.core.server.spring.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import net.ilx.server.shell.core.server.spring.conf.ActorServerConfiguration;

import org.jboss.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AApplication implements ApplicationContextAware, ApplicationEventPublisherAware{

    private static final Logger LOG = Logger.getLogger(AApplication.class);

    private static boolean stopped = false;

    @Autowired
    @Qualifier(ActorServerConfiguration.DEFAULT_THREAD_POOL)
    private ExecutorService executorService;

    private ConfigurableApplicationContext applicationContext;

    private int shutdownTimetout;

    private ApplicationEventPublisher publisher;

    public AApplication(final int shutdownTimetout) {
        this.shutdownTimetout = shutdownTimetout;
    }

    public void start() {
        LOG.info("starting main program");
        sendStartEvent();
    }

    private void sendStartEvent() {
        CustomApplicationEvent se = new CustomApplicationEvent(this, ApplicationEventType.START);
        publisher.publishEvent(se);
    }

    public void stop() {
        sendStopEvent();

        shutdownAndAwaitTermination(executorService);

        applicationContext.stop();
    }

    private void sendStopEvent() {
        CustomApplicationEvent stopEvent = new CustomApplicationEvent(this, ApplicationEventType.STOP);
        applicationContext.publishEvent(stopEvent);
    }

    void shutdownAndAwaitTermination(final ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(shutdownTimetout, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(shutdownTimetout, TimeUnit.SECONDS)) {
                    LOG.errorv("Executor service thread pool did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            LOG.errorv(ie, "Executor service thread interrupted while waiting for termination.");
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
