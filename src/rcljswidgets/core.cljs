(ns rcljswidgets.core
  (:require
    [rum.core :as rum]
    [rcljswidgets.rectangles :refer [square rect]]
    ))

(enable-console-print!)

;; Visit http://localhost:3449/index.html to see this
(rum/defc rum-tester []
  [:div
   [:h1 "Cljs widgets for R"]]
  )

(defn mount-component [el fill]
  (rum/mount (square fill "100px") el))

(defn resize-component [el fill width height]
  (rum/mount (rect fill width height) el))

;;;
;; Export render and resize function for each htmlwidget on the cljsWidgets global
;;;
;
; todo: test this alternative:
(def ^:external cljsWidgets
  (clj->js {:filled_rectangle {:render mount-component
                               :resize resize-component}}))
;
#_(set! (.-cljsWidgets js/window)
      (clj->js {:filled_rectangle {:render mount-component
                                   :resize resize-component}}))

