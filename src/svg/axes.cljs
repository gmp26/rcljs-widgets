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
       [:g {:key (gensym "K")}
        [:line {:key        1
                :x1         tick
                :y1         0
                :x2         tick
                :y2         ((o->i scale) 6)
                :class-name (:tick styles)}]
        [:text {:key 2
                :x   tick
                :dx  ((o->i scale) -10)
                :dy  ((o->i scale) 20)
                } (.toFixed (js/Number. tick) 0)]])]))

(rum/defc axisLeft [{:keys [scale ticks]
                     :or   {scale (->Identity [0 1])
                            ticks (range 0 1 0.1)}}]
  (let [[y1 y2] (in scale)]
    [:g
     [:line {:key        "Y"
             :x1         0
             :y1         y1
             :x2         0
             :y2         y2
             :class-name (:axis styles)}]

     (for [tick ticks]
       [:g {:key (gensym "K")}
        [:line {:key        1
                :x1         0
                :y1         tick
                :x2         -6                             ;((o->i scale) -6)
                :y2         tick
                :class-name (:tick styles)}]
        [:text {:key 2
                :x   0
                :y   tick
                :dx  ((o->i scale) -30)
                :dy  ((o->i scale) 0)
                } (.toFixed (js/Number. tick) 0)]])]))
