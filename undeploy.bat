@ECHO OFF

WHERE /Q asadmin.bat
IF ERRORLEVEL 0 GOTO DEPLOY_PATH
IF DEFINED GLASSFISH_ROOT GOTO DEPLOY

ECHO To use this automatic deploy script, please ensure that the `asadmin' 
ECHO command is on your PATH, or define GLASSFISH_ROOT to point to the root of
ECHO your desired glassfish installation.
EXIT /B 1


:DEPLOY_PATH
REM set GLASSFISH_ROOT to root dir of glassfish
FOR /F "delims=" %%i IN ('WHERE asadmin.bat') DO SET ASADMIN_EXE=%%i
FOR %%i IN ("%ASADMIN_EXE%") DO SET GLASSFISH_ROOT=%%~dpi
PUSHD %GLASSFISH_ROOT%
CD ..
SET GLASSFISH_ROOT=%CD%
POPD

:DEPLOY
SET ASADMIN_EXE="%GLASSFISH_ROOT%\bin\asadmin.bat"

CALL %ASADMIN_EXE% %* undeploy itlenergy-ear

IF NOT ERRORLEVEL 0 EXIT /B 2
