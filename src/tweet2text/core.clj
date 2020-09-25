(ns tweet2text.core
 (:require [clj-http.client :as http]
           [clojure.edn :as edn]
           [clojure.java.io :as io]
           [clojure.data.json :as json]))

(def config (-> "twitter.edn" io/resource slurp edn/read-string))

(defn tweets-by-id [id]
 (get
 (json/read-str
 (:body
 (http/get (-> config :api-url (str "tweets/" id)) 
           {:headers {"Authorization" (str "Bearer " (:bearer-token config))} 
            :query-params {"tweet.fields" "conversation_id"}})))
 "data")
)

; This technically works, but only if the thread was within the last 7 days.
; See: https://developer.twitter.com/en/docs/twitter-api/tweets/search/introduction
; Which says: "Soon, we plan to release a full-archive version which will make
; the entire archive of public Tweets available. The recent and full-archive
; search endpoints will share common design and features and are part of the
; Search Tweets group of endpoints."

(defn tweets-by-conversation-id [conversation-id]
;(get (json/read-str (:body
 (http/get (-> config :api-url (str "tweets/search/recent")) {:headers
  {"Authorization" (str "Bearer " (:bearer-token config))} :query-params
  {"query" (str "conversation_id:" conversation-id) "tweet.fields"
  "conversation_id"}})
; "data"))
)
