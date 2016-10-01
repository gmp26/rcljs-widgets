(ns rcljswidgets.rectangles
  (:require [rum.core :as rum]))

(rum/defc ^:export square
  "Draw a square with a given fill"
  [fill width]
  [:div {:style {:width            width
                 :height           width
                 :background-color fill}}])

(rum/defc ^:export rect
  "Draw a rectangle of width and height pixels. Also allow height as a percentage of width"
  [fill width height]
  [:div (let [w (if (number? width)
                  (str width "px") width)
              h_map (if (number? height)
                      {:height (str height "px")}
                      (if (pos? (-indexOf height "%"))
                        {:padding-top height}
                        {:height height}))]
          {:style (merge {:width            w
                          :background-color fill}
                         h_map)})])

