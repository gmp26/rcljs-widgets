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

(defn mount-component [el fill]
  (println "mountcomponent called")
  (rum/mount (a-square fill) el))

(set! (.-mountComponent js/window) mount-component)
(.log js/console js/window)

#_(defn main []
  ;; conditionally start the app based on whether the #app
  ;; node is on the page
  (if-let [node (.getElementById js/document "app")]
    (rum/mount (rum-tester) node)))

#_(main)