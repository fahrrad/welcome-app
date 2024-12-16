(ns welcome.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [redirect response]]
            [hiccup2.core :as h]
            [clojure.string :as s]))

(def agenda-headers 
  ["From" "To" "Duration" "Title" "Link"])

(def agenda "1:30 pm	1:40 pm	10	Welcome & Introduction	
1:40 pm	2:00 pm	20	Compute Overview *Presentation*	
2:00 pm	2:15 pm	15	Log into the AWS accounts\thttps://catalog.us-east-1.prod.workshops.aws/join?access-code=f6be-02b504-cc
2:15 pm	2:45 pm	30	Elastic Compute Cloud (EC2) Hands on *Lab*	https://catalog.us-east-1.prod.workshops.aws/workshops/f3a3e2bd-e1d5-49de-b8e6-dac361842e76/en-US/basic-modules/10-ec2
2:45 pm	3:00 pm	15	*Break*	
3:00 pm	3:20 pm	20	Storage in AWS *Presentation*	
3:20 pm	4:05 pm	45	Simple Storage Service (S3) Hands on *Lab*	https://catalog.us-east-1.prod.workshops.aws/workshops/f3a3e2bd-e1d5-49de-b8e6-dac361842e76/en-US/basic-modules/60-s3/s3
4:05 pm	4:25 pm	20	Networking in AWS *Presentation*	
4:25 pm	4:45 pm	20	Virtual Private Cloud (VPC) Hands on *Lab*	https://catalog.us-east-1.prod.workshops.aws/workshops/f3a3e2bd-e1d5-49de-b8e6-dac361842e76/en-US/basic-modules/20-vpc/vpc
\t\t\tWe want your feedback!\thttps://pulse.aws/survey/O9BD7WEM
4:45 pm	5:00 pm	15	Q&A and Conclusion")

(def agenda-items
  (let [agenda-rows (s/split agenda #"\n")]
    (map #(s/split % #"\t") agenda-rows)))

(defn process-format [i]
  (-> i
      (s/replace #"(?i)(.+)\*lab\*" "🧪 $1 🧪")
      (s/replace #"(?i)\*break\*" "⏸️ Break ⏯️")
      (s/replace #"(?i)(.+)\*presentation\*" "👩🏼‍🏫 $1 👩🏼‍🏫")))

(defn process-url [i]
  (if (s/starts-with? i "http")
    [:a {:href i :target "blank"} "link"]
    i))

(defn agenda-generator [_]
  (str (h/html 
  [:table
    [:theader
     [:tr
      (for [h agenda-headers] [:td h] )]]
    [:tbody
     (for [row agenda-items]
       [:tr 
        (for [i row] [:td (-> i process-format process-url )])])]])))

(defroutes app-routes
  (HEAD "/" [] "")
  (GET "/" [] (redirect "/index.html"))
  (GET "/health" [] (response "Ok!"))
  (GET "/agenda" [] agenda-generator)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(comment
  (process-format "test")
  (process-format "*test*")
  (process-format "*lab*")
  (process-format "*Presentation*")
  (process-format "*LAB*") 
  (agenda-generator {})
  (h/raw (h/html [:a {:href "http://www.google.be" :target "blank"} "link"]))
  agenda-items 
  )
