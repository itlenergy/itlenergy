#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
MAVEN_EXE="$DIR/tools/maven/bin/mvn"


echo
echo ===========================================================================
echo   BUILDING ...
echo ===========================================================================
echo
echo

if ! $MAVEN_EXE -f "$DIR/pom.xml" clean install; then
    exit 1
fi


echo
echo ===========================================================================
echo   DEPLOYING TO EMBEDDED GLASSFISH AT http://localhost:8282/itlenergy-web ...
echo ===========================================================================
echo
echo

$MAVEN_EXE -f "$DIR/itlenergy-ear/pom.xml" embedded-glassfish:run 2>&1 | tee "$DIR/server.log"
exit $?
