@ECHO OFF

SET MAVEN_EXE="%~dp0\tools\maven\bin\mvn.bat"

IF NOT "%MAVEN_TARGETS%"=="" GOTO BUILD
SET MAVEN_TARGETS=clean install

:BUILD
ECHO.
ECHO ===========================================================================
ECHO   BUILDING ...
ECHO ===========================================================================
ECHO.
ECHO.

CALL %MAVEN_EXE% -f %~dp0\pom.xml %MAVEN_TARGETS%
IF NOT ERRORLEVEL 0 EXIT /B 1

ECHO.
ECHO.
ECHO ===========================================================================
ECHO   DEPLOYING TO EMBEDDED GLASSFISH AT http://localhost:8282/itlenergy-web ...
ECHO ===========================================================================
ECHO.
ECHO.

CALL %MAVEN_EXE% -f %~dp0\itlenergy-ear\pom.xml embedded-glassfish:run