(ns rcljswidgets.funnel-plots
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [garden.selectors :refer-macros [defselector]]
            [cljs-css-modules.macro :refer-macros [defstyle]]))

(def data nil)

(rum/defc test-plot [data]
  [:rect {:fill "red" :height 100 :width 100}])

(def rect [:rect {:fill "red" :height 100 :width 100}])


(defstyle styles
          [[".axis" {:stroke-width 4
                     :stroke       "#000"}]
           [".tick" {:stroke-width 4
                     :stroke       "#000"}]])

(rum/defc axis [{:keys [position lb ub ticks title]
                 :or   {position :bottom
                        lb       0
                        ub       10
                        tick     (range 10)
                        title    ""}}]
  (case
    (= position :bottom)
    [:g
     [:line {:x1         lb
             :x2         ub
             :class-name (:axis styles)}]
     (for [tick ticks]
       [:line {:x1 tick :y1 0 :x2 tick :y2 100 :class-name (:tick styles)}])
     ]))

(rum/defc svg-container [width height content & [[xmin ymin xmax ymax :as view-box]]]
  [:svg {:width    width
         :height   height
         :view-box (s/join " " (map str view-box))}
   (content)
   ])





