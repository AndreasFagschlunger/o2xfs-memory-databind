package at.o2xfs.memory.core;

public interface MemorySystem {

	void free(Address address);

	Address nullValue();

	Address write(Object value);

	<T> T read(Address address, Class<T> valueType);

}
