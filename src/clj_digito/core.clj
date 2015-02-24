(ns clj-digito.core
  (:require [clj-digito.api :refer [api-get]]
            [cheshire.core :as json]))

(defn parse-result 
  "Default result parser"
  [{:keys [status headers body error]}]
  (if error
    error
    [status headers (json/parse-string body true)]))

(defn get-available-regions 
  "Example api call, get available regions"
  []
  (let [[_ _ regions] @(api-get "regions" parse-result)]
    (filter #(and (:available %) 1) (:regions regions))))

(defn foo
  "I don't do a whole lot."
  []
  (get-available-regions))
