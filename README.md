# metabase-dremio-driver

Dremio driver for Metabase BI

Tested on:

-   Dremio 15.0.0
-   Metabase 0.39.x


## Which features works

Dremio Driver can works in most metabase functionalities:

-   get correct data type for columns
-   support filter by numbers & strings & datetime
-   get table lists
-   x-ray on dremio tables


## How to use

1.  Download the dremio.metabase-driver.jar from releases
2.  Put it under metabase's plugins folder (plugins folder is at the same parent folder with metabase.jar)
3.  Restart metabase


## Building the driver


### Prereq: Install Metabase as a local maven dependency, compiled for building drivers

Clone the [Metabase repo](https://github.com/metabase/metabase) first if you haven't already done so.

```shell
cd /path/to/metabase_source
lein install-for-building-drivers
```


### Build dremio driver

```shell
# (In the metabase-dremio-driver directory)
lein clean
LEIN_SNAPSHOTS_IN_RELEASE=true lein uberjar
```

The generated "dremio.metabase-driver.jar" can be found in target/uberjar folder


## Thanks

Referred to <https://github.com/arsenikstiger/dremio-driver>, but most logic are referred from Metabase's redshift & sparksql driver.