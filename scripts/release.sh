#!/usr/bin/env bash

# Set release version
version=`mvn -B help:evaluate -N -Dexpression=project.version | sed -n '/^[0-9]/p'`
tag=`echo ${version} | cut -d'-' -f 1`
echo "release version is: ${tag}"

mvn -B license:format versions:set -DprocessAllModules=true -DgenerateBackupPoms=false -DnewVersion="${tag}"
mvn -B versions:set-property -Dproperty=revision -DnewVersion="${tag}"

# Publish to Central
mvn -B -DskipTests clean deploy


