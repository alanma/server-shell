package net.ilx.server.shell.core.server.spring.components.registry;

import java.util.Set;

public interface Registry<AliasType, ValueType> {

	ValueType get(final AliasType alias);

	void add(final AliasType alias, final ValueType command);

	Set<AliasType> keys();

	void remove(AliasType key);
}
