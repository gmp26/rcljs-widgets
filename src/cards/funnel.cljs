(ns cards.funnel
  (:require [pubsub.feeds :refer [create-feed ->Topic]]
            [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [rcljswidgets.funnel-plots :refer [funnel-plot svg-container]]
            [alg.binom :as alg]))

(enable-console-print!)

(.log js/console (alg/sfe 3))

(def data nil)

(defcard
  "A funnel plot"
  (svg-container 600 200 #(funnel-plot data) [0 0 300 100])
  )
