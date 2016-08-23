(ns example.data
  (:require [rum.core :as rum]
            [pubsub.feeds :refer [create-feed ->Topic subscribe]]))

(defonce db* (atom {:a {:b 7 :c 9}}))

(def tangle-events* (->Topic :tangle (create-feed)))

(subscribe tangle-events*
           (fn [_ value]
             (println "received " value)
             (swap! db* update-in [:a :b] (fn [_] value))))