(ns rcljs-widgets.experiments)

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
