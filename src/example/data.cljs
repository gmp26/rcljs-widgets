(ns example.data
  (:require [rum.core :as rum]
            [pubsub.feeds :refer [create-feed ->Topic subscribe]]))

(defonce db* (atom {:a {:b 7 :c 9 :d 11}}))
(defonce bref* (rum/cursor-in db* [:a :b]))                 ; allow rum component to watch :b
(defonce cref* (rum/cursor-in db* [:a :c]))                 ; allow rum component to watch :b
(defonce d* (atom 11))

(defonce feed* (create-feed))                               ; just the one feed, carrying
(defonce update-b* (->Topic :bref feed*))                   ; an 'update :b' topic
(defonce update-c* (->Topic :cref feed*))                   ; and an 'update :c' topic
(defonce update-d* (->Topic :dref feed*))                   ; and an 'update d*' topic

(subscribe update-b*
           (fn [_ value]
             (println "update b " value)
             (swap! db* update-in [:a :b] (fn [_] (js/Math.round value)))))

(subscribe update-c*
           (fn [_ value]
             (println "update c " value)
             (swap! db* update-in [:a :c] (fn [_] value))))

(subscribe update-d*
           (fn [_ value]
             (println "update d " value)
             (reset! d* value)))