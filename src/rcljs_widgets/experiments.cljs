(ns rcljs-widgets.experiments
  (:require
    [goog.string :as gstring]
    [goog.string.format]))

(def foo
  (rum.core/build-defc
    (clojure.core/fn ([]
                      (do (js/React.createElement "div" nil)))) nil "foo"))


(def fee
  (rum.core/build-defc
    (clojure.core/fn                                                     ;; render-body
      ([width]
       (do
         (js/React.createElement "div" #js
             {:style #js {:width width}}))))
    nil                                                     ;; mixins
    "fee"                                                   ;; display-name
    ))

(defn format [v]
  (gstring/format "$%2f" v))