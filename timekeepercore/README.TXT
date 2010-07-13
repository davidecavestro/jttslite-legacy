Please beware that this netbeans project has been
customized to avoid problems related to the JDO
enhancer compiler plugin.

The javac classpath variable has been setted into the build.xml
ant script, to let the compiler find the needed libraries, but still
avoiding the presence of core jdo implementation classes (datanucleus
core lib) into the netbeans run classpath. In fact, since netbeans always
"exports" the compilation liraries even for dependent project "run" calls, the JDO plugin
claimed a double registration of itself, hence stopping the IDE run/debug
process.

The first effect is that the changes to the compiler  classpath from the IDE
gui are not reflected to the actual compiler calls.