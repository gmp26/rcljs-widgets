(ns cards.shiny-dimensions
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [defcard-doc defcard deftest]]
    [rWrappers.tangleRectangle :refer [adjuster]]))

(defcard width-adjuster
  (adjuster :width 150))

(defcard height-adjuster
  (adjuster :height 250))