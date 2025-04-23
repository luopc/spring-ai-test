#!/bin/bash

set -e

BUILD_TYPE=$1

if [ "$BUILD_TYPE" = "release" ]; then
  echo "Build Release Version"
  git clean -f
  #mvn versions:use-latest-releases -Dincludes=com.luopc.platform.cloud.common -DgenerateBackupPoms=false

  echo "Detecting auto version"
  DIFF=$(eval 'git diff --stat')
  if [ -n "$DIFF" ]; then
    echo "change detected"
    git add .

    CHANGE=$(git status | grep "to be committed")
    if [ -n "$CHANGE" ]; then
      echo "Version Progressing"
      git commit -m "#plugin - auto committed"
    fi
  fi
  echo "release build starting"
  #mvn dependency:resolve dependency:revolve-plugin
  mvn -B clean release:prepare-with-pom release:perform -DuseReleaseProfile=false -Dmaven.javadoc.skip=true  -Puat
else
  echo "Building Snapshot Version"
  mvn -B clean deploy -Dmaven.javadoc.skip=true
  VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.6.0:exec)
  echo "current version is $VERSION"
  echo "Local command: java -jar -Xms128m -Xmx512m -Dspring.profiles.active=dev -Denv=dev {project}/target/{project-jar}.jar"
fi
