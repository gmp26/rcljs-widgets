(ns example.data
  (:require     [rum.core :as rum]
                [pubsub.feeds :refer [create-feed ->Topic]]))

(defonce *tn (atom {:a {:b 7}}))

(def *cursor (rum/cursor-in *tn [:a :b]))

(def *feed (create-feed))
(def *tangle-events (->Topic :tangle *feed))

