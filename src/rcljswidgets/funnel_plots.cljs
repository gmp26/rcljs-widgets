(ns rcljswidgets.funnel-plots
  (:require [rum.core :as rum]
            [clojure.string :as s]))

(def data nil)

(rum/defc funnel-plot [data]
  [:rect {:fill "red" :height 100 :width 100}])

(def rect [:rect {:fill "red" :height 100 :width 100}])

(rum/defc svg-container [width height content & [[xmin ymin xmax ymax :as view-box]]]
  [:svg {:width    width
         :height   height
         :view-box (s/join " " (map str view-box))}
   rect
   [:g {:transform "translate(200,0)"} rect]
   ;(content)
   ])





