(ns example.data
  (:require [rum.core :as rum]
            [pubsub.feeds :refer [create-feed ->Topic subscribe]]))

(defonce db* (atom {:a {:b 7 :c 9}}))
(defonce bref* (rum/cursor-in db* [:a :b]))
(defonce cref* (rum/cursor-in db* [:a :c]))

(defonce feed* (create-feed))

(defonce tangle-events* (->Topic :bref feed*))
(defonce tangle-inline* (->Topic :cref feed*))

(subscribe tangle-events*
           (fn [_ value]
             (println "received " value)
             (swap! db* update-in [:a :b] (fn [_] value))))

(subscribe tangle-inline*
           (fn [_ value]
             (println "inline " value)
             (swap! db* update-in [:a :c] (fn [_] value))))