(ns clj-digito.api
  (:require [org.httpkit.client :as http]
            [environ.core :refer [env]]))

(def api-root (or (env :api-root) "https://api.digitalocean.com/v2/"))

(def token (env :token)) ; api token given by digital ocean

;; Required for authorization, appended to header in every call
(def auth-header {:headers {"Authorization" (str "Bearer " token)}})

;; Default droplet parameters
(def default-droplet-prefix (env :default-droplet-prefix))
(def default-image-id (env :default-image-id))

(defn api-request 
  "General api request"
  [params f]
  (http/request (conj params auth-header) f))

(defn api-get 
  "Get subject"
  [subject f]
  (api-request {:url (str api-root subject) :method :get} f))

(defn api-post 
  "Create subject with given parameters"
  [subject params f]
  (api-request (conj params {:url (str api-root subject) :method :post}) f))

(defn api-delete 
  "Delete subject"
  [subject f]
  (api-request {:url (str api-root subject) :method :delete} f))

(defn create-droplet 
  "Example api call, create droplet with given paramters"
  [params]
  (api-post "droplets" {:form-params params} parse-result))

(defn delete-droplet 
  "Example api call, delete droplet with given id"
  [id]
  (api-delete (str "droplets/" id) parse-result))

(defn create-droplet-params 
  "Just to show which parameter map is required for creating droplet"
  [{:keys [name region size image]
    :or {name (str default-droplet-prefix (rand-int 1000000))
         region "ams2" ; Amsterdam 2
         size "512mb" ; Droplet size 512mb
         image default-image-id}}] ; Default image id 
  {:name name :region region :size size :image image})


