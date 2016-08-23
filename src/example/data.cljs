(ns example.data
  (:require [rum.core :as rum]
            [pubsub.feeds :refer [create-feed ->Topic subscribe]]))

(defonce db* (atom {:a {:b 7 :c 9}}))

(defonce feed* (create-feed))

(defonce tangle-events* (->Topic :tangle feed*))
(defonce tangle-inline* (->Topic :inline feed*))

(subscribe tangle-events*
           (fn [_ value]
             (println "received " value)
             (swap! db* update-in [:a :b] (fn [_] value))))

(subscribe tangle-inline*
           (fn [_ value]
             (println "inline " value)
             (swap! db* update-in [:a :c] (fn [_] value))))