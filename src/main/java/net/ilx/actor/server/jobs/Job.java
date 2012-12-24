package net.ilx.actor.server.jobs;

import net.ilx.actor.server.actors.Stoppable;


public interface Job extends Stoppable {

	String id();
	String name();
	String status();

	String statistics();

	void execute();
}
