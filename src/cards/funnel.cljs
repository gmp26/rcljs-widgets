(ns cards.funnel
  (:require [pubsub.feeds :refer [create-feed ->Topic]]

            [cljs-css-modules.macro :refer-macros [defstyle]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [svg.space :refer [space]]
            [rcljswidgets.funnel-plots :refer [funnel non-negative-int? valid-record data-space derived-data]]
            [tests.funnel-data :refer [CABG]]
            [alg.binom :as alg]))

(enable-console-print!)


(def margin {:top 20 :right 20 :bottom 20 :left 20})
(def padding {:top 60 :right 30 :bottom 40 :left 70})
(def outer {:width 700 :height 600})

(defcard
  "a funnel"
  (funnel (data-space outer margin padding
                      (derived-data CABG)
                      [0.001 0.025])))
