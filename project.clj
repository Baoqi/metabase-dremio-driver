(defproject metabase/dremio-driver "1.0.0"
  :min-lein-version "2.5.0"

  :dependencies
  [[com.dremio.distribution/dremio-jdbc-driver "15.5.0-202105040508160960-dddd4dca"]]
  
  :aot :all     ; Checks for compile-time failures when building the uberjar

  :repositories {"dremio-free" "https://maven.dremio.com/free/"}

  :profiles
  {:provided
   {:dependencies
    [[org.clojure/clojure "1.10.1"]
     [metabase-core "1.0.0-SNAPSHOT"]
     ]}

   :uberjar
   {:auto-clean    true
    :aot :all
    :javac-options ["-target" "1.8", "-source" "1.8"]
    :target-path   "target/%s"
    :uberjar-name  "dremio.metabase-driver.jar"}})
