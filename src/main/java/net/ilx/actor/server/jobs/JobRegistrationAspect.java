package net.ilx.actor.server.jobs;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class JobRegistrationAspect {
	private static final Logger LOG = Logger.getLogger(JobRegistrationAspect.class);

	@Autowired
	private JobRegistry jobRegistry;

	@Pointcut("execution(* net.ilx.actor.server.jobs.Job+.execute()) && !within(net.ilx.actor.server.jobs.JobRegistrationAspect+)")
	public void jobExecute() {
	}

	@Pointcut("execution(* net.ilx.actor.server.jobs.Job+.stop()) && !within(net.ilx.actor.server.jobs.JobRegistrationAspect+)")
	public void jobStop() {
	}



	@Before("jobStop() && target(job)")
	public void onJobStop(final Job job) {
		LOG.debugv("job {0} stopping", job.name());
	}

	@Before("jobExecute() && target(job)")
	public void onJobExecute(final Job job) {
		LOG.debugv("job {0} execution started", job.name());

		jobRegistry.add(job.name(), job);
	}

	@After("jobExecute() && target(job)")
	public void afterJobExecute(final Job job) {
		Job registeredJob = jobRegistry.get(job.name());
		if (null != registeredJob) {
			jobRegistry.remove(job.name());
		}
		else {
			throw new IllegalStateException("Job must be registered in order to be removed!");
		}
	}
}
