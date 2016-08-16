(ns rcljs-widgets.devcards
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    [rcljs-widgets.core :as core]
    [rcljs-widgets.tangle :as tangle]
    )
  (:require-macros
    [devcards.core :as dc :refer [defcard deftest]]
    ))

(enable-console-print!)

;; Visit http://localhost:3449/cards.html to see this

(defcard first-card
  (core/rum-tester))

(defcard a-square
  (core/square "rgb(150,170,200)" "100px"))

(defcard a-rectangle
  (core/rect "rgb(150,200,170)" "60%" "30%"))

(defcard tangle-card
  (core/tangle-card))

(defcard tangle-numeric
  (tangle/tangle-numeric tangle/tangle-cursor
                         {:minimum 0 :maximum 10 :step 5})
  )

(defcard format
  (core/format 2.5))
