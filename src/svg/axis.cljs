(ns svg.axis
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.scales :refer [->Identity ->Linear in out i->o o->i tick-format]]))


(defstyle styles
          [[".axis" {:stroke-width 1
                     :stroke       "green"
                     :shape-rendering "crispEdges"}]
           [".tick" {:stroke-width 1
                     :stroke       "green"
                     :shape-rendering "crispEdges"}]
           [".tick-label" {:fill "green"}]])


(rum/defc axisBottom [{:keys [scale ticks formatter]
                       :or   {scale (->Identity [0 1] 10)
                              ticks (range 0 1 0.1)}}]
  (let [[x1 x2] (in scale)
        x (i->o scale)
        formatter (if (nil? formatter) (tick-format scale) formatter)]
    [:g
     [:line {:key        "X"
             :x1         (x x1)
             :y1         0
             :x2         (x x2)
             :y2         0
             :class-name (:axis styles)}]

     (for [tick ticks]
       [:g {:key (gensym "K")}
        [:line {:key        1
                :x1         (x tick)
                :y1         0
                :x2         (x tick)
                :y2         "6px"
                :class-name (:tick styles)}]
        [:text {:key         2
                :class-name  (:tick-label styles)
                :x           (x tick)
                :dx          0
                :dy          "2.5ex"
                :text-anchor "middle"
                } (formatter tick)]])]))

(rum/defc axisLeft [{:keys [scale ticks formatter]
                     :or   {scale (->Identity [0 1] 10)
                            ticks (range 0 1 0.1)}}]
  (let [[y1 y2] (in scale)
        y (i->o scale)
        formatter (if (nil? formatter) (tick-format scale) formatter)]
    [:g
     [:line {:key        "Y"
             :x1         0
             :y1         (y y1)
             :x2         0
             :y2         (y y2)
             :class-name (:axis styles)}]

     (for [tick ticks]
       [:g {:key (gensym "K")}
        [:text {:class-name  (:tick-label styles)
                :key         2
                :x           0
                :y           (y tick)
                :dx          "-1ex"
                :dy          "0.5ex"
                :text-anchor "end"
                } (formatter tick)]
        [:line {:key        1
                :x1         0
                :y1         (y tick)
                :x2         "-6px"
                :y2         (y tick)
                :class-name (:tick styles)}]
        ])]))
