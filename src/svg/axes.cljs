(ns svg.axes
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.scales :refer [->Identity in out i->o o->i]]))


(defstyle styles
          [[".svg-box" {:padding          0
                        :margin           0
                        :background-color "#fee"}]
           [".axis" {:stroke-width 1
                     :stroke       "#000"}]
           [".tick" {:stroke-width 1
                     :stroke       "#000"}]
           [".label" {}]])


(rum/defc axisBottom [{:keys [scale ticks formatter]
                       :or   {scale (->Identity [0 1])
                              ticks (range 0 1 0.1)
                              formatter #(.toFixed (js/Number. %) 0)}}]
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
        [:text {:key         2
                :x           tick
                :dx          0
                :dy          "2.5ex"
                :text-anchor "middle"
                } (formatter tick)]])]))

(rum/defc axisLeft [{:keys [scale ticks formatter]
                     :or   {scale (->Identity [0 1])
                            ticks (range 0 1 0.1)
                            formatter #(.toFixed (js/Number. %) 0)}}]
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
        [:text {:key 2
                :x   0
                :y   ((i->o scale) tick)
                :dx  "-1ex"
                :dy  "0.5ex"
                :text-anchor "end"
                } (formatter tick)]
        [:line {:key        1
                :x1         0
                :y1         ((i->o scale) tick)
                :x2         -6                             ;((o->i scale) -6)
                :y2         ((i->o scale) tick)
                :class-name (:tick styles)}]
        ])]))
