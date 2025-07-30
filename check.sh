#!/usr/bin/env bash
set -euo pipefail

./gradlew build publishToMavenLocal -Pproject.version=dev
./gradlew build -PconventionPluginsVersion=dev
