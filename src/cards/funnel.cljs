(ns cards.funnel
  (:require [pubsub.feeds :refer [create-feed ->Topic]]
            [cljs.test :refer-macros [is testing]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [svg.space :refer [space]]
            [rcljswidgets.funnel-plots :refer [funnel]]
            [alg.binom :as alg]))

(enable-console-print!)


(def margin {:top 20 :right 20 :bottom 20 :left 20})
(def padding {:top 60 :right 30 :bottom 40 :left 70})
(def outer {:width 700 :height 600})

(defcard
  "a funnel"
  (funnel (space outer margin padding [0 200] 10 [1 100] 5)))
