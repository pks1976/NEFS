@echo off

REM $Id: build.bat,v 1.1 2003-03-13 18:24:57 shahid.shah Exp $

REM **************************************************************************
REM ** This script should be be run from the PROJECT_HOME\conf directory.   **
REM ** It is basically a "launcher" for Ant and the actual work is done in  **
REM ** the build.xml file.                                                  **
REM **************************************************************************

if "%JAVA_HOME%" == "" echo Error: JAVA_HOME environment variable is not set. && goto end
if "%NS_COMMONS_HOME%" == "" set NS_COMMONS_HOME=..\..\Commons
if "%NS_COMMONS_REDIST_LIB%" == "" set NS_COMMONS_REDIST_LIB=%NS_COMMONS_HOME%\lib\redist

if "%JAVACMD%" == "" set JAVACMD=%JAVA_HOME%\bin\java
if not exist "%JAVACMD%.exe" echo Error: "%JAVACMD%.exe" not found - check JAVA_HOME && goto end

if exist "%JAVA_HOME%/lib/tools.jar" set JAVACP=%JAVA_HOME%\lib\tools.jar
if exist "%JAVA_HOME%/lib/classes.zip" set JAVACP=%JAVACP%;%JAVA_HOME%\lib\classes.zip

%JAVACMD% -classpath %NS_COMMONS_REDIST_LIB%\ant.jar;%NS_COMMONS_REDIST_LIB%\ant-optional.jar;%NS_COMMONS_REDIST_LIB%\junit.jar;%NS_COMMONS_REDIST_LIB%\xerces.jar;%JAVACP% org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

:end