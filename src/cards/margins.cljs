(ns cards.margins
  (:require [pubsub.feeds :refer [create-feed ->Topic]]
            [cljs.test :refer-macros [is testing]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [svg.space :refer [margins space]]))

(enable-console-print!)

(def margin {:top 20 :right 20 :bottom 20 :left 20})
(def padding {:top 60 :right 60 :bottom 60 :left 60})
(def outer {:width 700 :height 400})

(defcard
  "margins"
  (margins (space outer margin padding)))



