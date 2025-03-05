#!/bin/bash

# Check if a version number is provided
if [ -z "$1" ]; then
  echo "Error: No version number provided."
  echo "Usage: $0 <version-number>"
  exit 1
fi

VERSION=$1
JAR_NAME="otel-guidewire-instrumentation-v3-$VERSION.jar"
BUILD_DIR="javaagent/build/libs"

echo "Starting the build process for version $VERSION..."

# Display the JAR file if it already exists
if [ -f "$BUILD_DIR/$JAR_NAME" ]; then
  echo "Existing JAR file:"
  ls -ltr "$BUILD_DIR/$JAR_NAME"
fi

echo "Applying Spotless formatting..."
../../gradlew :instrumentation:guidewire-instrumentation-v3:javaagent:spotlessApply

echo "Cleaning previous builds..."
../../gradlew :instrumentation:guidewire-instrumentation-v3:javaagent:clean

echo "Building the project..."
../../gradlew :instrumentation:guidewire-instrumentation-v3:javaagent:build

echo "Renaming the JAR file to include the version number..."
mv "$BUILD_DIR/agent-testing.jar" "$BUILD_DIR/$JAR_NAME"

echo "Final JAR file:"
ls -ltr "$BUILD_DIR/$JAR_NAME"

echo "The build process for version $VERSION is complete."

