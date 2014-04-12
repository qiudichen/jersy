@echo off

@setlocal

@set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_45
@set MAVEN_HOME=C:\development\utils\apache-maven-3.2.1
@set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

mvn %*