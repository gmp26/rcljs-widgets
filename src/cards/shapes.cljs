(ns cards.shapes
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    [rcljs-widgets.core :as core]
    [example.data :refer [cursor*]]
    )
  (:require-macros
    [devcards.core :as dc :refer [defcard-doc defcard deftest]]
    ))


(defcard-doc
  "#Rectangle tests
  Visible container proxies to test resize behaviour in various contexts."
  )

(defcard a-square
  (core/square "rgb(150,170,200)" "100px"))

(defcard a-rectangle
  (core/rect "rgb(150,200,170)" "60%" "30%"))