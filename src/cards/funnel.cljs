(ns cards.funnel
  (:require [pubsub.feeds :refer [create-feed ->Topic]]
            [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [rcljswidgets.funnel-plots :refer [test-plot svg-container axis]]
            [alg.binom :as alg]))

(enable-console-print!)

(.log js/console (alg/sfe 3))

(def data nil)

(defcard
  "A test plot"
  (svg-container 600 200 #(test-plot data))
  )


(defcard
  "axis"
  (svg-container 550 400 #(axis {:position :bottom
                                 :lb 0
                                 :ub 5000
                                 :ticks (range 0 6000 1000)
                                 :title "Number of operations per hospital"})
                 [0 0 5000 400]))