(ns adstxt-results.core
  (:require [clojure.java.io :as io]
            [adstxt-results.repo :as repo]
            [adstxt-results.zip :as zip])
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
          num (Integer/parseInt (second (clojure.string/split "Total number of domains: 16516" #": ")))
          ]
      (println line1)
      (println num)
      )))


(defn run []
  (let [localzip "adstxt-results.zip"]
    (repo/save-branch-to-zip localzip repo/url)
    (let [recentfile (zip/extract-most-recent-file localzip)]
      (println "Most recent file " recentfile "has been downloaded"))))


(defn -main [& args]
  (run))
  
 
