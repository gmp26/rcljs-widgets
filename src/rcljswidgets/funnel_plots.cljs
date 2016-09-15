(ns rcljswidgets.funnel-plots
  (:require [rum.core :as rum]
            [clojure.string :as s]))

(def data nil)

(rum/defc test-plot [data]
  [:rect {:fill "red" :height 100 :width 100}])

(def rect [:rect {:fill "red" :height 100 :width 100}])

(rum/defc axis [])

(rum/defc svg-container [width height content & [[xmin ymin xmax ymax :as view-box]]]
  [:svg {:width    width
         :height   height
         :view-box (s/join " " (map str view-box))}
   rect
   [:g {:transform "translate(200,0)"
        :width 300
        :height 10} rect]
   ;(content)
   ])





