#!/bin/bash

if [ -z "$GLASSFISH_BIN" ]; then
    GLASSFISH_BIN=`which asadmin`
    if ! [ -z "$GLASSFISH_BIN" ]; then
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

$ASADMIN_EXE "$@" undeploy apatasche-ear