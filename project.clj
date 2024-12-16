(defproject welcome "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :aws {:access-key ~(System/getenv "AWS_ACCESS_KEY")
        :secret-key ~(System/getenv "AWS_SECRET_KEY")}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "2.0.0-RC4"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler welcome.handler/app
         :port 5000}
  :profiles 
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
