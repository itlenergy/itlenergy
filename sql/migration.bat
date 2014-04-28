@ECHO OFF
DEL "%~dp0\migration.sql" 2>NUL

IF "%1"=="test" THEN GOTO TEST

COPY "%~dp0\*.sql" "%~dp0\migration.sql"
EXIT /B 0

:TEST
COPY "%~dp0\*.sql"+"%~dp0\test-data\*.sql" "%~dp0\migration.sql"