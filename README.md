# metabase-dremio-driver

Dremio driver for Metabase BI

Version compatibility:

-   Version 1.4.x works with Metabase v0.50.0/v1.50.0 and above
-   Version 1.3.x works with Metabase v0.48.0/v1.48.0 and above
-   Version 1.2.x works with Metabase v0.46.0/v1.46.0 and above
-   Version 1.1.x works with Metabase v0.43.0/v1.43.0 and bellow v0.46.0/v1.46.0 (exclude)
-   Version 1.0.x works with Metabase bellow v0.43.0/v1.43.0 (exclude)


Tested on:

-   Dremio 24.0.0+
-   Metabase 0.50.0+


## Which features works

Dremio Driver can work in most metabase functionalities:

-   get correct data type for columns
-   support filter by numbers & strings & datetime
-   get table lists
-   x-ray on dremio tables


## How to use

1.  Download the dremio.metabase-driver.jar from releases
2.  Put it under metabase's plugins folder (plugins folder is at the same parent folder with metabase.jar)
3.  Restart metabase


## Building the driver

For Metabase 0.43.0+, there were many changes, for exmample, the build tool changed from lein to Clojure CLI Tools. So, the build steps for metabase-dremio-driver 1.1.x also changed.

For Metabase 0.46.0+, there were more changes. For that version, we should use build.sh instead

### Prereq: Build Metabase source locally

Please refer to [Building Metabase](https://www.metabase.com/docs/latest/developers-guide/build.html) document.

### Build metabase-dremio-driver

clone the source code, and put it under the same parent folder as metabase's source code.

then, under this metabase-dremio-driver folder, run

```shell
./build.sh
```

The generated "dremio.metabase-driver.jar" can be found in target folder


## Thanks

Referred to <https://github.com/arsenikstiger/dremio-driver>, but most logic are referred from Metabase's redshift & sparksql driver.