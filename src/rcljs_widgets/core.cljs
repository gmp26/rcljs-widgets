(ns rcljs-widgets.core
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    )
)

(enable-console-print!)

;; Visit http://localhost:3449/index.html to see this

(rum/defc rum-tester []
  [:div
   [:h1 "Cljs widgets for R"]]
  )

(rum/defc square [fill width]
  [:div {:style {:width width
                 :height width
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

(rum/defc tangle-card []
  [:div
   (.createElement js/React js/TangleText #js {:value 6 :min 0 :max 10 :setp 1 :onChange #(.log js/console %1)})
   ])

(defn mount-component [el fill]
  (rum/mount (square fill "100%") el))

(defn resize-component [el fill width height]
  (rum/mount (rect fill width height) el))

;;;
;; Export render and resize function for each htmlwidget on the cljsWidgets global
;;;
(set! (.-cljsWidgets js/window)
      (clj->js {:filled_rectangle {:render mount-component
                                   :resize resize-component}}))
