(defproject adstxt-results "0.0.1"
  
  :description "https://github.com/bradlucas/adstxt-results"
  :url "https://github.com/bradlucas/adstxt-results"
  
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.1"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 [environ "1.0.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [ring-webjars "0.2.0"]
                 [metosin/ring-http-response "0.9.0"]
                 [selmer "1.11.1"]]

  :min-lein-version "2.0.0"

  :plugins [[lein-ring "0.9.7"]
            [environ/environ.lein "0.3.1"]]
  
  :hooks [environ.leiningen.hooks]
  :ring {:handler adstxt-results.handler/app}

  :target-path "target/%s/"
  :main ^:skip-aot adstxt-results.core
  

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "adstxt-results-standalone.jar"}
   :production {:env {:production true}}
   :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})

