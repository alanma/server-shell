package net.ilx.actor.server.alf.spring.components.registry;

public interface Registry<K,V> {

	public abstract V get(final K alias);

	public abstract void add(final K alias,
								final V command);

}
