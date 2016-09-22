(ns svg.markers
  (:require [rum.core :as rum]
            [cljs-css-modules.macro :refer-macros [defstyle]]))

(defstyle styles
          [[".open" {:fill         "none"
                     :stroke       "#000"
                     :stroke-width 1}]
           [".filled" {:fill   "#000"
                       :stroke "none"}]])

(defn op [c-name]
  (if c-name c-name :filled))

(rum/defc dot [r cx cy & [c-name]]
  [:circle {:class-name ((op c-name) styles)
            :r          r
            :cx         cx
            :cy         cy}])

(defn ring [r cx cy]
  (dot r cx cy :open))

(def odot ring)

(rum/defc square [r cx cy & [c-name]]
  [:rect {:class-name ((op c-name) styles)
          :x          (- cx r)
          :y          (- cy r)
          :width      (* 2 r)
          :height     (* 2 r)}])

(defn osquare [r cx cy]
  (square r cx cy :open))

(rum/defc diamond [r cx cy & [c-name]]
  [:g {:transform (str "translate(" cx "," cy ")")}
   [:rect {:class-name ((op c-name) styles)
           :x          (- r)
           :y          (- r)
           :width      (* 2 r)
           :height     (* 2 r)
           :transform  "rotate(45)"
           }]])

(defn odiamond [r cx cy]
  (diamond r cx cy :open))

(rum/defc plus [r cx cy]
  [:g
   [:line {:class-name (:open styles)
           :x1         cx
           :y1         (- cy r)
           :x2         cx
           :y2         (+ cy r)
           }]
   [:line {:class-name (:open styles)
           :x1         (- cx r)
           :y1         cy
           :x2         (+ cx r)
           :y2         cy
           }]])

(rum/defc cross [r cx cy]
  [:g {:transform (str "translate(" cx "," cy ")")}
   [:g {:transform "rotate(45)"}
    (plus r 0 0)]]
  )