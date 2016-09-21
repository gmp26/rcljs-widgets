(ns cards.margin-convention
  (:require [pubsub.feeds :refer [create-feed ->Topic]]
            [cljs.test :refer-macros [is testing]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [svg.space :refer [space]]
            [svg.margin-convention :refer [margins]]))

(enable-console-print!)

(def margin {:top 20 :right 20 :bottom 20 :left 20})
(def padding {:top 60 :right 60 :bottom 60 :left 60})
(def outer {:width 700 :height 400})

(defcard
  "margins"
  (margins (space outer margin padding [0 200] 10 [1 100] 5)))

(defcard
  "axes spanning 0"
  (margins (space outer margin padding [-200 200] 10 [-100 100] 5)))

(defcard
  "reversed scales spanning 0"
  (margins (space outer margin padding [200 -200] 10 [100 -100] 5)))

(defcard
  "nicely calculated ticks from data in [23 3200] [10.2 10.5]"
  (margins (space outer margin padding [23 3200] 10 [10.2 10.5] 5)))

(defcard
  "large value and low value scales"
  (margins (space outer margin padding [20000 30000] 5 [0.001 0.002] 5)))

