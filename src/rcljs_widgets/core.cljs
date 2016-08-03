(ns rcljs-widgets.core
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    )
)

(enable-console-print!)

;; Visit http://localhost:3449/index.html to see this

(rum/defc rum-tester []
  [:div
   [:h1 "Cljs widgets for R"]]
  )

(rum/defc a-square [fill]
  [:div {:style {:width "100%"
                 :height "100%"
                 :background-color fill}}])

(rum/defc a-square-size [fill width height]
  [:div {:style {:width width
                 :height height
                 :background-color fill}}])

(defn mount-component [el fill]
  (rum/mount (a-square fill) el))

(defn resize-component [el fill width  height]
  (rum/mount (a-square-size fill width height) el))

;;;
;; Export render and resize function for each htmlwidget on the cljsWidgets global
;;;
(set! (.-cljsWidgets js/window)
      (clj->js {:filled_square {:render mount-component
                                :resize resize-component}}))
