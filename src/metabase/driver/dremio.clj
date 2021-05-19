(ns metabase.driver.dremio
  "Dremio Driver."
  (:require [cheshire.core :as json]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [clojure.tools.logging :as log]
            [honeysql.core :as hsql]
            [metabase.config :as config]
            [metabase.driver :as driver]
            [metabase.driver.sql-jdbc.common :as sql-jdbc.common]
            [metabase.driver.sql-jdbc.connection :as sql-jdbc.conn]
            [metabase.driver.sql-jdbc.execute :as sql-jdbc.execute]
            [metabase.driver.sql-jdbc.execute.legacy-impl :as legacy]
            [metabase.driver.sql-jdbc.sync :as sql-jdbc.sync]
            [metabase.driver.sql-jdbc.sync.describe-database :as sync.describe-database]
            [metabase.driver.sql.query-processor :as sql.qp]
            [metabase.mbql.util :as mbql.u]
            [metabase.public-settings :as pubset]
            [metabase.query-processor.store :as qp.store]
            [metabase.query-processor.util :as qputil]
            [metabase.util.honeysql-extensions :as hx]
            [metabase.util.i18n :refer [trs]])
  (:import [java.sql Types]
           java.time.OffsetTime))

(driver/register! :dremio, :parent #{:postgres ::legacy/use-legacy-classes-for-read-and-set})

;;; +----------------------------------------------------------------------------------------------------------------+
;;; |                                             metabase.driver impls                                              |
;;; +----------------------------------------------------------------------------------------------------------------+

(defmethod driver/display-name :dremio [_] "Dremio")
     
;;; +----------------------------------------------------------------------------------------------------------------+
;;; ----------------------------------------------------- Impls ------------------------------------------------------
;;; +----------------------------------------------------------------------------------------------------------------+

;; don't use the Postgres implementation for `describe-table` since it tries to fetch enums which Dremio doesn't
;; support
(defmethod driver/describe-table :dremio
  [& args]
  (apply (get-method driver/describe-table :sql-jdbc) args))

                  
(defmethod sql-jdbc.conn/connection-details->spec :dremio
  [_ {:keys [user password dbname host port ssl]
      :or {user "dbuser", password "dbpassword", host "localhost", dbname "Dremio", port 31010}
      :as details}]
  (-> {:applicationName    config/mb-app-id-string
       :type :dremio
       :subprotocol "dremio"
       :subname (str "direct=" host ":" port ";schema=" dbname)
       :user user
       :password password
       :host host
       :port port
       :classname "com.dremio.jdbc.Driver"
       :loginTimeout 10
       :encrypt (boolean ssl)
       :sendTimeAsDatetime false}
      (sql-jdbc.common/handle-additional-options details, :seperator-style :semicolon)))

;; custom Dremio type handling
(def ^:private database-type->base-type
  (sql-jdbc.sync/pattern-based-database-type->base-type
    [[#"(?i)DOUBLE" :type/Float]       ; Dremio uses DOUBLE for float8
     [#"(?i)INTEGER" :type/Integer]    ; Dremio uses INTEGER for int4
     ]))

(defmethod sql-jdbc.sync/database-type->base-type :dremio
  [driver column-type]
  (or (database-type->base-type column-type)
      ((get-method sql-jdbc.sync/database-type->base-type :postgres) driver (keyword (str/lower-case (name column-type))))))


;; Dremio doesn't support "+ (INTERVAL '-30 day')"
(defmethod sql.qp/add-interval-honeysql-form :dremio
  [_ hsql-form amount unit]
  (hsql/call :timestampadd (hsql/raw (name unit)) amount (hx/->timestamp hsql-form)))


;; Dremio's jdbc doesn't support getObject(Class<T> type)
(prefer-method
  sql-jdbc.execute/read-column-thunk
  [::legacy/use-legacy-classes-for-read-and-set Types/TIMESTAMP]
  [:postgres Types/TIMESTAMP])

(prefer-method
  sql-jdbc.execute/read-column-thunk
  [::legacy/use-legacy-classes-for-read-and-set Types/TIME]
  [:postgres Types/TIME])

(prefer-method
  sql-jdbc.execute/set-parameter
  [::legacy/use-legacy-classes-for-read-and-set OffsetTime]
  [:postgres OffsetTime])
