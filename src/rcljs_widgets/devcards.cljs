(ns rcljs-widgets.devcards
  (:require
    [rum.core]
    [cljs.test :as t]
    [rcljs-widgets.core :as core]
    )
  (:require-macros
    [devcards.core :as dc :refer [defcard deftest]]
    ))

(enable-console-print!)


;; Visit http://localhost:3449/cards.html to see this

(defcard first-card
  (core/rum-tester))

(defcard second-card
  (core/a-square "pink"))
