(ns rcljs-widgets.rectangles
  (:require [rum.core :as rum]))

(rum/defc square [fill width]
  [:div {:style {:width            width
                 :height           width
                 :background-color fill}}])

(rum/defc rect [fill width height]
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
