#!/usr/bin/env bash

function increment() {
  local version=$1
  result=$(echo ${version} | awk -F. -v OFS=. 'NF==1{print ++$NF}; NF>1{if(length($NF+1)>length($NF))$(NF-1)++; $NF=sprintf("%0*d", length($NF), ($NF+1)%(10^length($NF))); print}')
  echo "${result}-SNAPSHOT"
}

BASEDIR=$(dirname "$0")

echo "Current path is ${BASEDIR}"

cd $BASEDIR/..

if [ ! -f pom.xml ]; then
  echo "pom.xml is not exists..."
  exit 1
fi

# extract the release version from the pom file
version=$(mvn -B help:evaluate -N -Dexpression=project.version | sed -n '/^[0-9]/p')
tag=$(echo ${version} | cut -d'-' -f 1)
echo "release version is: ${tag}"

# Update the versions to the release version
mvn -B license:format versions:set -DprocessAllModules=true -DgenerateBackupPoms=false -DnewVersion="${tag}"
mvn -B versions:set-property -DgenerateBackupPoms=false -Dproperty=revision -DnewVersion="${tag}"

git commit --no-verify -a -m "[ci skip] Releasing ${tag}"
git push origin $(git rev-parse --abbrev-ref HEAD)

# Tag Release
git tag -a "v${tag}" -m "Release v${tag}"
git push --tags

# determine the next snapshot version
snapshot=$(increment ${tag})
echo "next snapshot version is: ${snapshot}"

snapshot_branch=$(echo ${snapshot} | sed 's/-SNAPSHOT//')
git checkout -b ${snapshot_branch}

# Update the versions to the next snapshot
mvn -B license:format versions:set -DprocessAllModules=true -DgenerateBackupPoms=false -DnewVersion="${snapshot}"
mvn -B versions:set-property -DgenerateBackupPoms=false -Dproperty=revision -DnewVersion="${snapshot}"

git commit --no-verify -a -m "[ci skip] Releasing ${snapshot_branch}"
git push origin $(git rev-parse --abbrev-ref HEAD)
