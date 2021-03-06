(ns adstxt-results.repo
  (:require [adstxt-results.zip :as zip]))

;; url
;; https://developer.github.com/v3/repos/contents/#get-archive-link
;; https://github.com/InteractiveAdvertisingBureau/adstxtcrawler/tree/adstxt-results
(def url "https://api.github.com/repos/InteractiveAdvertisingBureau/adstxtcrawler/zipball/adstxt-results")

;; Download url to a file
;; https://nakkaya.com/2010/06/15/clojure-io-cookbook/
(defn save-branch-to-zip [filename url]
  (println "fetch-url-to-file" filename url)
  (let  [con    (-> url java.net.URL. .openConnection)
         fields (reduce (fn [h v] 
                          (assoc h (.getKey v) (into [] (.getValue v))))
                        {} (.getHeaderFields con))
         size   (first (fields "Content-Length"))
         in     (java.io.BufferedInputStream. (.getInputStream con))
         out    (java.io.BufferedOutputStream. 
                 (java.io.FileOutputStream. filename))
         buffer (make-array Byte/TYPE 1024)]
    (loop [g (.read in buffer)
           r 0]
      (if-not (= g -1)
        (do
          (.write out buffer 0 g)
          (recur (.read in buffer) (+ r g)))))
    (.close in)
    (.close out)
    (.disconnect con)))


(defn download-if-needed [filename]
  (if (not (.exists (clojure.java.io/as-file filename)))
    (save-branch-to-zip filename url))
  filename)


;; NEW TEMP
(defn download-to-temp
  "Download the repo as a zip to a tmp file. Return the java.io.File instance to use and then delete"
  []
  (let [file (java.io.File/createTempFile "adstxt-results" ".zip")
        filename (.getAbsolutePath file)]
    (save-branch-to-zip filename url)
    file))
        

(defn download-most-recent []
  (let [zip (download-to-temp)
        [shortname recentfile] (zip/extract-most-recent-file-to-temp zip)]
    [shortname recentfile zip]))
