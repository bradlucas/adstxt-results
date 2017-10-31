(ns adstxt-results.core
  (:require [clojure.java.io :as io]
            [adstxt-results.repo :as repo]
            [adstxt-results.zip :as zip]
            [compojure.handler :refer [site]]
            [adstxt-results.handler :as h]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            )
  (:gen-class))

(defn file-data
  "Return the data ignoring the four lines of header information"
  [filename]
  (with-open [rdr (io/reader filename)]
    (doall (drop 4 (line-seq rdr)))))

(defn file-details
  "Parse the four line header block and return the file date and the number of domains"
  [filename]
  ;; 2017-10-23
  ;; Ads.txt crawler output
  ;; Total number of domains: 16516
  ;; =========================
  (with-open [rdr (io/reader filename)]
    (let [[line1 _ line3] (take 3 (line-seq rdr))
          num (Integer/parseInt (second (clojure.string/split line3 #": ")))]
      (println line1)
      (println num)
      )))


      

(defn run []
  (let [localzip "adstxt-results.zip"]
    (repo/save-branch-to-zip localzip repo/url)
    (let [recentfile (zip/extract-most-recent-file localzip)]
      (println "Most recent file " recentfile "has been downloaded"))))


(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'h/app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
  
 
