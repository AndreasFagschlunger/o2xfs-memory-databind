package at.o2xfs.memory.datatype.jdk8;

import at.o2xfs.memory.databind.Module;

public class Jdk8Module extends Module {

	@Override
	public void setupModule(SetupContext context) {
		context.addSerializers(new Jdk8Serializers());
		context.addDeserializers(new Jdk8Deserializers());
	}

}
