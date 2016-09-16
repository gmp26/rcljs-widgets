(ns svg.space
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.axes :refer [axisBottom axisLeft]]
            [svg.scales :refer [->Identity ->Linear i->o o->i]]))


(defstyle styles
          [[".svg-box" {:padding          0
                        :margin           0
                        :background-color "#fee"}]
           [".axis" {:fill            "none"
                     :stroke-width    4
                     :stroke          "#000"
                     :shape-rendering "crispEdges"}]
           [".tick" {:stroke-width 4
                     :stroke       "#000"}]
           [".outer" {:fill   "pink"
                      :stroke "#000"}]
           [".inner" {:fill "#CCC"
                      :stroke "#000"
                      :stroke-dasharray "3, 4"}]])

(defn space [outer margin padding]
  (let [inner {:width  (- (:width outer) (:left margin) (:right margin))
               :height (- (:height outer) (:top margin) (:bottom margin))}
        width (- (:width inner) (:left padding) (:right padding))
        height (- (:height inner) (:top padding) (:bottom padding))]
    {:outer   outer
     :inner   inner
     :margin  margin
     :padding padding
     :width   width
     :height  height
     :x       (->Identity [0 width])
     :y       (->Linear [0 height] [height 0])
     }))

(rum/defc start-marker []
  [:marker {:id "triangle-start"
            :view-box "0 0 10 10"
            :ref-x 10
            :ref-y 5
            :marker-width -6
            :marker-height 6
            :orient "auto"}
   [:path {:d "M 0 0 L 10 5 L 0 10 z"}]])

(rum/defc end-marker []
  [:marker {:id "triangle-end"
            :view-box "0 0 10 10"
            :ref-x 10
            :ref-y 5
            :marker-width 6
            :marker-height 6
            :orient "auto"}
   [:path {:d "M 0 0 L 10 5 L 0 10 z"}]])

(rum/defc svg2 [{:keys [outer margin inner padding width height x y]
                 :or {inner {:width  (- (:width outer) (:left margin) (:right margin))
                             :height (- (:height outer) (:top margin) (:bottom margin))}
                      width (- (:width inner) (:left padding) (:right padding))
                      height (- (:height inner) (:top padding) (:bottom padding))
                      x (->Identity [0 width])
                      y (->Identity [0 height])
                      }}]
  [:svg {:width  (:width outer)
         :height (:height outer)}
   [:g {:transform (str "translate(" (:left margin) "," (:right margin) ")")}
    [:defs
     (start-marker)
     (end-marker)]
    [:rect {:class-name (:outer styles)
            :width      (:width inner)
            :height     (:height inner)}]
    [:g {:transform (str "translate(" (:left padding) "," (:right padding) ")")}
     [:rect {:class-name (:inner styles)
             :width width
             :height height}]
     [:g {:class-name ".xaxis"
          :transform  (str "translate(0," ((i->o y) -10) ")")}
      (axisBottom {:scale x :ticks (range 0 (+ width (/ width 9)) (/ width 9))})]
     [:g {:class-name ".yaxis"
          :transform (str "translate(-10,0)")}
      (axisLeft {:scale y :ticks (range 0 (+ height (/ height 8)) (/ height 8))})]]
    ]
   ])


(rum/defc svg [space]
  [:svg {:width  (:width (:outer space))
         :height (:height (:outer space))}
   [:g {:transform (str "translate(" (:left (:margin space)) "," (:right (:margin space)) ")")}
    [:rect {:class-name (:outer styles)
            :width      (:width (:inner space))
            :height     (:height (:inner space))}]]])

