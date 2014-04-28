#!/bin/bash

if [ -z "$GLASSFISH_BIN" ]; then
    GLASSFISH_BIN=`which asadmin`
    if ! [ -z "GLASSFISH_BIN" ]; then
        GLASSFISH_BIN=`dirname "$GLASSFISH_BIN"`
    fi
fi

if [ -z "$GLASSFISH_BIN" ]; then
    echo To use this automatic deploy script, please ensure that the 'asadmin' 
    echo command is on your PATH, or define GLASSFISH_BIN to point to the bin
    echo folder of your glassfish installation.
    exit 1
fi


DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ASADMIN_EXE="$GLASSFISH_BIN/asadmin"
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
echo   DEPLOYING TO GLASSFISH ...
echo ===========================================================================
echo
echo

TARGET_DIR=$DIR/itlenergy-ear/target
DEPLOY_TARGET=`find "$TARGET_DIR" -name "itlenergy-ear-*.ear"`

if [ -z $DEPLOY_TARGET ]; then
    echo Deploy target not found in $TARGET_DIR
    exit 1
else
    echo Found EAR $DEPLOY_TARGET
fi

$ASADMIN_EXE "$@" deploy --force=true --name itlenergy-ear "$DEPLOY_TARGET"
exit $?
