#!/usr/bin/env bash

SCRIPT_PATH=$(cd "$(dirname "$0")" ; pwd -P)
APP=${1}
PROFILE=${2}
PROJECT=

if [[ -z ${PROFILE} ]]; then
  PROFILE="default"
fi


if [[ ${APP} == "server" ]]; then
  PROJECT="config-server"
elif [[ ${APP} == "client" ]]; then
  PROJECT="config-client"
else
  echo "start.sh [server|client] [profile]"
  exit 1
fi

cd "${SCRIPT_PATH}/.." && ./mvnw spring-boot:run --projects=${PROJECT} -Dspring-boot.run.profiles=${PROFILE}
