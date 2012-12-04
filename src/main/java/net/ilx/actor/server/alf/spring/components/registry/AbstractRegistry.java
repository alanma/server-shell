package net.ilx.actor.server.alf.spring.components.registry;

import java.util.Map;
import java.util.TreeMap;

public class AbstractRegistry<K,V> implements Registry<K,V> {

	public Map<K, V> registrar = new TreeMap<K, V>();

	@Override
	public V get(final K alias) {
		return registrar.get(alias);
	}

	@Override
	public void add(final K alias,
					final V command)
	{
		registrar.put(alias, command);
	}

}
