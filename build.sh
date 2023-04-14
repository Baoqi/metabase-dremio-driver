#! /usr/bin/env bash

# switch to the local checkout of the Metabase repo
# EX: cd /home/user/repos/metabase
cd "/path/to/metabase/repo"

# get absolute path to the driver project directory
# EX: DRIVER_PATH="/home/user/repos/metabase-dremio-driver"
DRIVER_PATH="/path/to/metabase-dremio-driver/repo"

# Build driver. See explanation in Sudoku sample driver README
clojure \
  -Sdeps "{:aliases {:dremio {:extra-deps {com.metabase/dremio-driver {:local/root \"$DRIVER_PATH\"}}}}}"  \
  -X:build:dremio \
  build-drivers.build-driver/build-driver! \
  "{:driver :dremio, :project-dir \"$DRIVER_PATH\", :target-dir \"$DRIVER_PATH/target\"}"