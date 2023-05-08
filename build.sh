#!/bin/bash
cd $(dirname $0) 
DRIVER_PATH=$(pwd)

cd ../metabase

DREMIO_DRIVER_VERSION=24.0.0-202302100528110223-3a169b7c

clojure \
  -Sdeps '{:mvn/repos {"dremio-free" {:url "https://maven.dremio.com/free/"}} :aliases {:dremio { :extra-deps {com.dremio.distribution/dremio-jdbc-driver {:mvn/version "'${DREMIO_DRIVER_VERSION}'"} com.metabase/dremio-driver {:local/root "'${DRIVER_PATH}'"} }}}}'  \
  -X:build:dremio \
  build-drivers.build-driver/build-driver! \
  "{:driver :dremio, :project-dir \"${DRIVER_PATH}\", :target-dir \"${DRIVER_PATH}/target\"}"

cd "${DRIVER_PATH}"

