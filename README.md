# GMSI
GeX's Map Script Interpreter.

compile (drag n drop).bat requires javac to be in environment variables. It uses the original GMSI.jar (GMSI_do_not_change.jar) to recompile the code. The compiled class file is placed in the same folder as the input file.

You need to replace the old .class files manually in your GMSI.jar.

The decompiled code is riddled with type errors and whatnot (that seem to be ok to fix by blindly changing the types to correct ones), so compiling the GMSI.jar from scratch is impossible (so far).


The list of currently changed files:

imageIO/TGA32Encoder.java

wcData/MapHandle.java 

wcData/objects/MetaData.java

wcData/objects/W3O_File.java
