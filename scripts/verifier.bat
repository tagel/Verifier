@echo off

set dir=%~dp0
set CLASSPATH=%dir%/verifier.jar;%dir%/lib/stax-1.2.0-all-bin.zip

java -cp %CLASSPATH% main.Main %*