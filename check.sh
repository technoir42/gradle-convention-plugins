#!/usr/bin/env bash
set -euo pipefail

./gradlew publishToMavenLocal -Pproject.version=dev
./gradlew build -PconventionPluginsVersion=dev
