(ns example.data
  (:require [rum.core :as rum]
            [pubsub.feeds :refer [create-feed ->Topic subscribe]]))

(defonce db* (atom {:a {:b 7}}))

(def cursor* (rum/cursor-in db* [:a :b]))

(def feed* (create-feed))
(def tangle-events* (->Topic :tangle feed*))

(subscribe tangle-events*
           (fn [_ value]
             (println "received " value)
             (swap! db* update-in [:a :b] (fn [_] value))))