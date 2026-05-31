#!/bin/sh

MAVEN_VERSION=3.9.9
WRAPPER_DIR="$HOME/.m2/wrapper/dists/apache-maven-$MAVEN_VERSION"
MAVEN_HOME="$WRAPPER_DIR/apache-maven-$MAVEN_VERSION"

if [ ! -x "$MAVEN_HOME/bin/mvn" ]; then
  echo "Downloading Apache Maven $MAVEN_VERSION..."
  mkdir -p "$WRAPPER_DIR"
  ZIP_FILE="/tmp/apache-maven-$MAVEN_VERSION-bin.zip"
  if command -v curl >/dev/null 2>&1; then
    curl -L "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip" -o "$ZIP_FILE"
  else
    wget "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip" -O "$ZIP_FILE"
  fi
  unzip -q "$ZIP_FILE" -d "$WRAPPER_DIR"
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
