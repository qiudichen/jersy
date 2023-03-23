1. start server with jmx options:
-Dcom.sun.management.jmxremote.port=16301 
-Dcom.sun.management.jmxremote.ssl=false 
-Dcom.sun.management.jmxremote.login.config=NiceJMX 
-Djava.security.auth.login.config=nice_jaas.config 
-Dcom.sun.management.jmxremote.password.file=config/jmxremote.password -Dcom.sun.management.jmxremote.access.file=config/jmxremote.access

-Dcom.sun.management.jmxremote.port=16301 -Dcom.sun.management.jmxremote.rmi.port=16301  
-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true 


java.runtime.name=OpenJDK Runtime Environment
sun.boot.library.path=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib
java.vm.version=25.352-b08
gopherProxySet=false
jdk.vendor.version=Zulu 8.66.0.15-CA-macosx
java.vm.vendor=Azul Systems, Inc.
java.vendor.url=http://www.azul.com/
path.separator=:
java.vm.name=OpenJDK 64-Bit Server VM
file.encoding.pkg=sun.io
user.country=US
sun.java.launcher=SUN_STANDARD
sun.os.patch.level=unknown
com.sun.management.jmxremote.port=16301
java.vm.specification.name=Java Virtual Machine Specification
user.dir=/Users/DavidChen/development/WFM/eclipse/dev/encryptedLoginModule
java.runtime.version=1.8.0_352-b08
java.awt.graphicsenv=sun.awt.CGraphicsEnvironment
java.endorsed.dirs=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/endorsed
os.arch=x86_64
java.io.tmpdir=/var/folders/jy/vldd0lvx7ndctg2kphy684jh0000gp/T/
line.separator=

java.vm.specification.vendor=Oracle Corporation
os.name=Mac OS X
sun.jnu.encoding=UTF-8
java.library.path=/Users/DavidChen/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
java.specification.name=Java Platform API Specification
java.class.version=52.0
sun.management.compiler=HotSpot 64-Bit Tiered Compilers
os.version=10.13.6
http.nonProxyHosts=local|*.local|169.254/16|*.169.254/16
user.home=/Users/DavidChen
user.timezone=
java.awt.printerjob=sun.lwawt.macosx.CPrinterJob
java.specification.version=1.8
file.encoding=UTF-8
com.sun.management.jmxremote.password.file=config/jmxremote.password
user.name=DavidChen
java.class.path=/Users/DavidChen/development/WFM/eclipse/dev/encryptedLoginModule/target/test-classes:/Users/DavidChen/development/WFM/eclipse/dev/encryptedLoginModule/target/classes:/Users/DavidChen/.m2/repository/log4j/log4j/1.2.16/log4j-1.2.16.jar:/Users/DavidChen/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:/Users/DavidChen/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/DavidChen/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/DavidChen/development/eclipses/jee-2022/Eclipse.app/Contents/Eclipse/configuration/org.eclipse.osgi/412/0/.cp/lib/javaagent-shaded.jar
com.sun.management.jmxremote.login.config=NiceJMX
java.vm.specification.version=1.8
sun.arch.data.model=64
java.home=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre
sun.java.command=com.nice.wfm.security.jmx.JMXServer
java.specification.vendor=Oracle Corporation
user.language=en
awt.toolkit=sun.lwawt.macosx.LWCToolkit
java.vm.info=mixed mode
com.sun.management.jmxremote.ssl=false
java.version=1.8.0_352
java.ext.dirs=/Users/DavidChen/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
sun.boot.class.path=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/cat.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/classes
java.vendor=Azul Systems, Inc.
java.specification.maintenance.version=4
java.security.auth.login.config=nice_jaas.config
file.separator=/
java.vendor.url.bug=http://www.azul.com/support/
sun.cpu.endian=little
sun.io.unicode.encoding=UnicodeBig
com.sun.management.jmxremote.access.file=config/jmxremote.access
socksNonProxyHosts=local|*.local|169.254/16|*.169.254/16
ftp.nonProxyHosts=local|*.local|169.254/16|*.169.254/16
sun.cpu.isalist=


I've enabled the jmx agent with:

-Dcom.sun.management.jmxremote
I've set it to run on localhost on port 1234:

-Dcom.sun.management.jmxremote.port=1234
I've configured the JMX agent to use a specified JAAS configuration entry:

-Dcom.sun.management.login.config=Sample
and specified the path to the Jaas configuration file:

-Djava.security.auth.login.config=sample_jaas.config

sample_jaas.config

Sample {
   sample.module.SampleLoginModule required debug=true;
   authIdentity=monitorRole;
};

monitor role is specified in jmxremote.access

-Dcom.sun.management.jmxremote.access.file=jmxremote.access
and looks like this:

monitorRole readonly
controleRole readwrite
my Loginmodule is simple in that is returns true whatever.


java -Dcom.sun.management.jmxremote.port=16301 \
	-Dcom.sun.management.jmxremote.rmi.port=16301 \
	-Dcom.sun.management.jmxremote.ssl=false \
	-Dcom.sun.management.jmxremote.authenticate=true \
	
	-Dcom.sun.management.jmxremote.login.config=Sample \
	-Dcom.java.security.auth.login.config=sample_jaas.config \
	-Djava.security.policy==sampleazn.policy \
	-Dcom.sun.management.jmxremote.access.file=jmxremote.access -jar MBeanSecure.jar
	
	
	
-Dcom.sun.management.jmxremote.port=16301 
-Dcom.sun.management.jmxremote.ssl=false 
-Dcom.sun.management.jmxremote.login.config=NiceJMX 
-Djava.security.auth.login.config=nice_jaas.config 
-Dcom.sun.management.jmxremote.access.file=/Users/DavidChen/development/servers/wfm_server/config/tvconfig/jmxremote.access


	
-Dcom.sun.management.jmxremote.port=16301 -Dcom.sun.management.jmxremote.rmi.port=16301 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.login.config=Sample -Dcom.java.security.auth.login.config=/Users/DavidChen/development/WFM/eclipse/dev/MBeanSecure/sample_jaas.config -Dcom.sun.management.jmxremote.access.file=/Users/DavidChen/development/servers/wfm_server/config/tvconfig/jmxremote.access
	
	     -Dcom.sun.management.jmxremote.login.config=ExampleCompanyConfig
     -Djava.security.auth.login.config=ldap.config
     
 -Djava.security.auth.login.config==sample_jaas.config
 
 	
I would like to fix the bug in this code. Change in login method only:

System.out.println("Login Module - login called");
    if (callbackHandler == null) {
        throw new LoginException("Oops, callbackHandler is null");
    }

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("name:");
    callbacks[1] = new PasswordCallback("password:", false);

    try {
        callbackHandler.handle(callbacks);
    } catch (IOException e) {
        throw new LoginException("Oops, IOException calling handle on callbackHandler");
    } catch (UnsupportedCallbackException e) {
        throw new LoginException("Oops, UnsupportedCallbackException calling handle on callbackHandler");
    }

    NameCallback nameCallback = (NameCallback) callbacks[0];
    PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];

    String name = nameCallback.getName();
    String password = new String(passwordCallback.getPassword());

    if ("sohanb".equals(name) && "welcome".equals(password)) {
        System.out.println("Success! You get to log in!");
        user = new JMXPrincipal(name);
        succeeded = true;
        return succeeded;
    } else {
        System.out.println("Failure! You don't get to log in");
        succeeded = false;
        throw new FailedLoginException("Sorry! No login for you.");
    }
Added:  user = new JMXPrincipal(name);

Also comment all lines of code in commit() function nd just add :

 subject.getPrincipals().add(user);
 
	
	
-Dcom.sun.management.jmxremote.port=16301 -Dcom.sun.management.jmxremote.rmi.port=16301  
-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true 
-Dcom.sun.management.jmxremote.password.file=/Users/DavidChen/development/servers/wfm_server/config/tvconfig/jmxremote.password 
-Dcom.sun.management.jmxremote.access.file=/Users/DavidChen/development/servers/wfm_server/config/tvconfig/jmxremote.access

	
