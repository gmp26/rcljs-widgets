(ns svg.axes
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.scales :refer [->Identity in out o->i]]))


(defstyle styles
          [[".svg-box" {:padding          0
                        :margin           0
                        :background-color "#fee"}]
           [".axis" {:stroke-width 1
                     :stroke       "#000"}]
           [".tick" {:stroke-width 1
                     :stroke       "#000"}]
           [".label" {}]])


(rum/defc axisBottom [{:keys [scale ticks]
                       :or   {scale (->Identity [0 1])
                              ticks (range 0 1 0.1)}}]
  (let [[x1 x2] (in scale)]
    [:g
     [:line {:key        "X"
             :x1         x1
             :y1         0
             :x2         x2
             :y2         0
             :class-name (:axis styles)}]

     (for [tick ticks]
       [:line {:key        (gensym "K")
               :x1         tick
               :y1         0
               :x2         tick
               :y2         ((o->i scale) 6)
               :class-name (:tick styles)}])]))

