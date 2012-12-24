package net.ilx.actor.server.spring.components.registry;

import java.util.Set;

public interface Registry<K,V> {

	V get(final K alias);

	void add(final K alias,
								final V command);

	Set<K> keys();


	void remove(K key);
}
