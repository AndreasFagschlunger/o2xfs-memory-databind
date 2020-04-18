o2xfs-memory-databind
=====

Based on the work from [jackson-databind](https://github.com/FasterXML/jackson-databind) this project aims to provide a similar mapping to native data structures. So the `MemoryMapper` maps native memory to POJOs and vice versa.

```java
public class SystemtimeDemo {

	public static void main(String[] args) throws IOException {
		MemoryMapper mapper = new MemoryMapper();
		Win32MemorySystem memorySystem = Win32MemorySystem.INSTANCE;
		Systemtime expected = Systemtime.valueOf(LocalDateTime.now());
		System.out.println("Expected: " + expected);
		Address lpSystemtime;
		try (Win32MemoryGenerator gen = new Win32MemoryGenerator(memorySystem)) {
			mapper.write(gen, expected);
			lpSystemtime = gen.allocate();
		}
		System.out.println("lpSystemtime: " + lpSystemtime);
		Systemtime actual = mapper.read(memorySystem.dereference(lpSystemtime), Systemtime.class);
		System.out.println("Actual: " + actual);
		memorySystem.free(lpSystemtime);
	}

}
```

When I executed the program, the output was as follows:
```
Expected: 2020-04-18T23:28:34.359
lpSystemtime: 161D7798
Actual: 2020-04-18T23:28:34.359
```

So [Systemtime](src/main/java/at/o2xfs/memory/databind/win32/Systemtime.java) is a POJO where `memory-databind` uses the annotations in the class to correctly create a native memory representation of the value, matching the memory model of the original [SYSTEMTIME](https://docs.microsoft.com/en-us/windows/win32/api/minwinbase/ns-minwinbase-systemtime) structure of the Windows API.

So after `allocate()` the Address within `lpSystemtime` points to a block of memory like the following:

`0xE40704000600120017001C0022006701`

So the first `WORD` (a 16-bit unsigned integer) in this block would represent the year, in this case `2020` (0x7e4) or `0xE407` in Windows due to "little-endian" endianness.

Next `WORD` would be month `0x0400`, dayOfWeek `0x0600`, day `0x1200`, hour `0x1700`, and so on.

The same annotations used to create the native memory model allows us to read it as well as following:

```
Systemtime actual = mapper.read(memorySystem.dereference(lpSystemtime), Systemtime.class);
System.out.println("Actual: " + actual);
```

<aside class="warning">
Currently [Alignment](https://docs.microsoft.com/en-us/cpp/cpp/alignment-cpp-declarations?view=vs-2019) is not supported, header files (*.h) should add the line `#pragma pack(push,1)` on top to disable alignment.
</aside>

## Code Status

[![Build Status](https://api.travis-ci.com/AndreasFagschlunger/o2xfs-memory-databind.svg?branch=develop)](https://travis-ci.com/AndreasFagschlunger/o2xfs-memory-databind)
