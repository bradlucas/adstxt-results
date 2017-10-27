(ns adstxt-results.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [adstxt-results.zip :as zip]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn home-page []

  )

;; Dupliucate function. See @core.clj
(defn file-data
  "Return the data ignoring the four lines of header information"
  [filename]
  (with-open [rdr (io/reader filename)]
    (doall (drop 4 (line-seq rdr)))))

(defn domains-list []
  ;; return the latest domains as a list 

  ;; Assume we've downloaded the zip already to 'adstxt-results.zip'
  (let [localzip "adstxt-results.zip"
        recentfile (zip/extract-most-recent-file localzip)]
    (println "Most recent file " recentfile "has been downloaded")
    {:Status 200
     :headers {"Content-Type" "text/plain"} 
     :body (clojure.string/join "\n" (file-data recentfile))
     }))

(defn file-map
  [filename]
  (with-open [rdr (io/reader filename)]
    (let [[[l1 _ l3 _] domains] (split-at 4 (line-seq rdr))
          num-domains (Integer/parseInt (second (clojure.string/split l3 #": ")))]
      {:name l1
       :num-domains num-domains
       :domains (doall domains)}
      )))

(defn domains-file []
  ;; return the actual file
  (let [localzip "adstxt-results.zip"
        recentfile (zip/extract-most-recent-file localzip)]
    {:status 200
     :headers {"content-disposition" (format "attachment; filename=\"%s\"" recentfile)} 
     :body (io/file recentfile)
     }))

(defn domains-json []
  ;; package the domains file in json with the date, number of records and domains in a list
  (let [localzip "adstxt-results.zip"
        recentfile (zip/extract-most-recent-file localzip)]
    {:status 200
     :headers {"Content-Type" "application/json"} 
     :body (json/write-str (file-map recentfile))
     }))


(defroutes app-routes
  (GET "/" [] (home-page))
  (GET "/api/list" [] (domains-list))
  (GET "/api/file" [] (domains-file))
  (GET "/api/json" [] (domains-json))
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (wrap-defaults app-routes site-defaults))
