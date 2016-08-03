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
   [:h1 "This is your first devcard!"]]
  )

#_(rum/defc a-square []
  [:div {:style {:width "100px"
                 :height "100px"
                 :background-color "orange"}}])

(rum/defc a-square [fill]
  [:div {:style {:width "100px"
                 :height "100px"
                 :background-color fill}}])

(defn mount_component [el fill]
  (println "mountcomponent called")
  (rum/mount (a-square fill) el))

;;;
;; Export the function to mount elements as for use as an htmlwidgets render function
;;;
(set! (.-cljsWidgets js/window)
      (clj->js {:filled_square (fn [el fill] (rum/mount (a-square fill) el))}))

(set! (.-mountComponent js/window) (fn [el fill] (rum/mount (a-square fill) el)))

#_(defn main []
  ;; conditionally start the app based on whether the #app
  ;; node is on the page
  (if-let [node (.getElementById js/document "app")]
    (rum/mount (rum-tester) node)))

#_(main)