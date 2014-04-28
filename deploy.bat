@ECHO OFF

SET MAVEN_EXE="%~dp0\tools\maven\bin\mvn.bat"

IF NOT "%GLASSFISH_BIN%"=="" GOTO DEPLOY
WHERE /Q asadmin.bat
IF "%ERRORLEVEL%"=="0" GOTO DEPLOY_PATH

ECHO To use this automatic deploy script, please ensure that the `asadmin' 
ECHO command is on your PATH, or define GLASSFISH_BIN to point to the root of
ECHO your desired glassfish installation.
EXIT /B 1


:DEPLOY_PATH
FOR /F "delims=" %%I IN ('where asadmin.bat') DO SET ASADMIN_EXE=%%I
GOTO BUILD

:DEPLOY
SET ASADMIN_EXE="%GLASSFISH_BIN%\asadmin.bat"

:BUILD
ECHO.
ECHO ===========================================================================
ECHO   BUILDING ...
ECHO ===========================================================================
ECHO.
ECHO.

CALL %MAVEN_EXE% -f %~dp0\pom.xml clean install
IF NOT ERRORLEVEL 0 EXIT /B 1

ECHO.
ECHO.
ECHO ===========================================================================
ECHO   DEPLOYING TO GLASSFISH ...
ECHO ===========================================================================
ECHO.
ECHO.

SET TARGET_DIR=%~dp0\itlenergy-ear\target
FOR /F "delims=" %%I IN ('dir /B "%TARGET_DIR%\itlenergy-ear-*.ear"') DO SET DEPLOY_TARGET=%TARGET_DIR%\%%I
SET DEPLOY_TARGET=%DEPLOY_TARGET:\=/%
ECHO Found EAR %DEPLOY_TARGET%
CALL %ASADMIN_EXE% %* deploy --force=true --name itlenergy-ear "%DEPLOY_TARGET%"
IF NOT ERRORLEVEL 0 EXIT /B 1

EXIT /B 0
