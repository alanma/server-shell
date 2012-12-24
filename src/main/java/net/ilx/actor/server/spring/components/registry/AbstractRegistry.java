package net.ilx.actor.server.spring.components.registry;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AbstractRegistry<K, V> implements Registry<K, V> {

	public Map<K, V> registrar = new TreeMap<K, V>();

	@Override
	public V get(final K alias) {
		return registrar.get(alias);
	}

	@Override
	public void add(final K alias,
					final V command)
	{
		if (registrar.containsKey(alias)) {
			throw new IllegalStateException("Registrar already contains key " + alias);
		}
		registrar.put(alias, command);
	}

	@Override
	public Set<K> keys() {
		return registrar.keySet();
	}

	@Override
	public void remove(final K key) {
		registrar.remove(key);
	}
}
