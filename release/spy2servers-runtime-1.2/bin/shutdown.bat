@echo off

REM     Copyright 2006 the original author or authors.
REM
REM     Licensed under the x.matthew License, Version 1.0 (the "License");
REM     you may not use this file except in compliance with the License.
REM
REM     Unless required by applicable law or agreed to in writing, software
REM     distributed under the License is distributed on an "AS IS" BASIS,
REM     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM     See the License for the specific language governing permissions and
REM     limitations under the License.


rem get current parent directory
set CURRENT_DIR=%~dp0..

echo %CURRENT_DIR%
if "%JAVA_HOME%" == "" goto noJavaHome
set _JAVA_CMD=%JAVA_HOME%\bin\java
goto run

:noJavaHome
set _JAVA_CMD=C:\Program Files\Java\jdk1.5.0_05\bin\java.exe

:run
if "%SUNJMX%" == "" set SUNJMX=-Dcom.sun.management.jmxremote
REM set SUNJMX=-Dcom.sun.management.jmxremote.port=1616 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false


set JAVA_OPTS = "-Xms256m -Xmx256m"

"%_JAVA_CMD%" %JAVA_OPTS% %SUNJMX% -Djava.ext.dirs="%CURRENT_DIR%\lib" -cp "%CURRENT_DIR%\classes;.;%CURRENT_DIR%\conf" org.xmatthew.spy2servers.console.Main stop

:end
set SPY2LOGGER_HOME=
set _JAVA_CMD=
set MYCLASSPATH=

@echo on