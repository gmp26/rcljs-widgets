(ns svg.space
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs.pprint :refer [cl-format]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.axis :refer [axisBottom axisLeft]]
            [svg.scales :refer [->Identity nice-linear i->o o->i in out ticks]]
            ))


(defstyle styles
          [[".outer" {:fill   "none"
                      :stroke "#000"}]
           [".inner" {:fill             "#ccc"
                      :stroke           "#000"
                      :stroke-width 0.5
                      :stroke-dasharray "3, 4"}]])

(defn space [outer margin padding x-domain x-ticks y-domain y-ticks]
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
     :x       (nice-linear x-domain [0 width] x-ticks)
     :y       (nice-linear y-domain [height 0] y-ticks)
     }))

(rum/defc start-marker []
  [:marker {:id            "triangle-start"
            :view-box      "0 0 10 10"
            :ref-x         10
            :ref-y         5
            :marker-width  -6
            :marker-height 6
            :orient        "auto"}
   [:path {:d "M 0 0 L 10 5 L 0 10 z"}]])

(rum/defc end-marker []
  [:marker {:id            "triangle-end"
            :view-box      "0 0 10 10"
            :ref-x         10
            :ref-y         5
            :marker-width  6
            :marker-height 6
            :orient        "auto"}
   [:path {:d "M 0 0 L 10 5 L 0 10 z"}]])

(rum/defc margins [{:keys [outer margin inner padding width height x y]}]
  (let [inner (if (nil? inner) {:width  (- (:width outer) (:left margin) (:right margin))
                                :height (- (:height outer) (:top margin) (:bottom margin))}
                               inner)
        width (if (nil? width) (- (:width inner) (:left padding) (:right padding)) width)
        height (if (nil? height) (- (:height inner) (:top padding) (:bottom padding)) height)
        x (if (nil? x) (->Identity [0 width] 10) x)
        x-ticks (ticks x)                                   ;(if (nil? x-ticks) (ticks 0 width 10) x-ticks)
        y (if (nil? y) (->Identity [0 height] 10) y)
        y-ticks (ticks y)                                   ;(if (nil? y-ticks) (ticks 0 height 5) y-ticks)
        ]

    [:svg {:width  (:width outer)
           :height (:height outer)}
     [:g {:transform (str "translate(" (:left margin) "," (:right margin) ")")}
      [:defs {:key 0}
       (rum/with-key (start-marker) 0)
       (rum/with-key (end-marker) 1)]
      [:rect {:key        1
              :class-name (:outer styles)
              :width      (:width inner)
              :height     (:height inner)}]
      [:g {:key       2
           :transform (str "translate(" (:left padding) "," (:right padding) ")")}
       [:rect {:key        1
               :class-name (:inner styles)
               :width      width
               :height     height}]
       [:g {:key        2
            :class-name ".xaxis"
            :transform  (str "translate(0," (+ (first (out y)) 10) ")")}
        (axisBottom {:scale x :ticks x-ticks})]
       [:g {:key        3
            :class-name ".yaxis"
            :transform  (str "translate(" (- (first (out x)) 10) ",0)")}
        (axisLeft {:scale y :ticks y-ticks})]]]]))


(rum/defc svg [space]
  [:svg {:width  (:width (:outer space))
         :height (:height (:outer space))}
   [:g {:transform (str "translate(" (:left (:margin space)) "," (:right (:margin space)) ")")}
    [:rect {:class-name (:outer styles)
            :width      (:width (:inner space))
            :height     (:height (:inner space))}]]])

