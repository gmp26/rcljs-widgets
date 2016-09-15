(ns cards.funnel
  (:require [pubsub.feeds :refer [create-feed ->Topic]]
            [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [rcljswidgets.funnel-plots :refer [test-plot svg-container]]
            [alg.binom :as alg]))

(enable-console-print!)

(.log js/console (alg/sfe 3))

(def data nil)

(defonce funnel-data (atom {}))


(defcard
  "A test plot"
  (svg-container 600 200 #(test-plot data) [0 0 300 100])
  )
