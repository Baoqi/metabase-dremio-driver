#!/bin/bash
cd $(dirname $0) 
DRIVER_PATH=$(pwd)

cd ../metabase

DREMIO_DRIVER_VERSION=26.0.0-202504290223270716-afdd6663

clojure \
  -Sdeps '{:mvn/repos {"dremio-free" {:url "https://maven.dremio.com/free/"}} :aliases {:dremio { :extra-deps {com.dremio.distribution/dremio-jdbc-driver {:mvn/version "'${DREMIO_DRIVER_VERSION}'"} com.metabase/dremio-driver {:local/root "'${DRIVER_PATH}'"} }}}}'  \
  -X:build:dremio \
  build-drivers.build-driver/build-driver! \
  "{:driver :dremio, :project-dir \"${DRIVER_PATH}\", :target-dir \"${DRIVER_PATH}/target\"}"

cd "${DRIVER_PATH}"

