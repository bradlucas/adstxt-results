(ns adstxt-results.zip
  (:require [clojure.java.io :as io])
  (:import java.util.zip.ZipInputStream))


;; Read zip contents
;; https://stackoverflow.com/a/5428265
(defn filenames-in-zip [filename]
  (let [z (java.util.zip.ZipFile. filename)] 
    (map #(.getName %) (enumeration-seq (.entries z)))))

;; Parse out the name of the most recent file
;; Example
;; ("InteractiveAdvertisingBureau-adstxtcrawler-548893a/"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_2017-09-11"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_2017-09-19.txt"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_2017-09-25.txt"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_2017-10-02.txt"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_2017-10-09.txt"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_2017-10-16.txt"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_2017-10-23.txt"
;;  "InteractiveAdvertisingBureau-adstxtcrawler-548893a/adstxt_domains_july31.txt")
;;
;; Filter out foo_yyyy-mm-dd.txt instances
;; (clojure.pprint/pprint (filter #(re-matches #".*_([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])).txt" %) l))
(defn filter-for-valid-names [l]
  (filter #(re-matches #".*_([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])).txt" %) l))

;; Filter for file with most recent 'date'
(defn most-recent [l]
  (first (reverse (filter #(re-matches #".*_([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])).txt" %) l))))

(defn get-most-recent-filename [zipfile]
  (println "get-most-recent-filenamne" zipfile)
  (-> zipfile
      (-> filenames-in-zip
          filter-for-valid-names
          most-recent)))

;; Extract file
;; https://stackoverflow.com/questions/10188063/clojure-unzipping-a-zip-file-stored-as-a-resource
;; http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java
(defn entries [zipfile]
 (lazy-seq
  (if-let [entry (.getNextEntry zipfile)]
    (cons entry (entries zipfile)))))

(defn extract-file [zip-stream filename]
  (println "extract-file" filename)
  (let [shortname (last (clojure.string/split filename #"/"))]
    (with-open [out-file (io/output-stream shortname)]
      (let [buff-size 4096
            buffer (byte-array buff-size)]
        (loop [len (.read zip-stream buffer)]
          (when (> len 0)
            (.write out-file buffer 0 len)
            (recur (.read zip-stream buffer)))))
      )
    shortname))

(defn extract-file-from-zip [zipfile filename]
  (println "extract-file-from-zip " zipfile filename)
  (let [zs (ZipInputStream. (io/input-stream zipfile))]
    (let [entry (first (filter #(= (.getName %) filename) (entries zs)))]
      (extract-file zs filename))))

(defn extract-most-recent-file [zipfile]
  (let [recentfile (get-most-recent-filename zipfile)]
    (extract-file-from-zip zipfile recentfile)))


;; NEW TEMP
(defn extract-most-recent-file-to-temp
  "For a given zip file extract the most recent filename. Return the filename and the file as a java.io.File"
  [file]
  (let [recent-filename (get-most-recent-filename (.getAbsolutePath file))
        shortname (last (clojure.string/split recent-filename #"/"))
        recent-file (java.io.File/createTempFile "adstxt-results-recent-" ".txt")]
    (let [zs (ZipInputStream. (io/input-stream file))]
      (let [entry (first (filter #(= (.getName %) recent-filename) (entries zs)))]
        (with-open [out-file (io/output-stream (.getAbsolutePath recent-file))]
          (let [buff-size 4096
                buffer (byte-array buff-size)]
            (loop [len (.read zs buffer)]
              (when (> len 0)
                (.write out-file buffer 0 len)
                (recur (.read zs buffer))))))))
    [shortname recent-file]))


  
