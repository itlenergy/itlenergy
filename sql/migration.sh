#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
pushd "$DIR"
rm -f migration.sql

if [ "$1" == 'test' ]; then
    cat *.sql test-data/*.sql > migration.sql
else
    cat *.sql > migration.sql
fi

popd