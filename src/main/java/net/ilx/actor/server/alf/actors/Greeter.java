package net.ilx.actor.server.alf.actors;
public interface Greeter {
	// Methods on actor interfaces must return void and not have throws
	// declarations.
	// Any parameters may be used, but immutable ones are strongly
	// encouraged.
	// Actors should always be passed around as ActorRefs.
	void sayGreeting(String name);

	void stop();
}
