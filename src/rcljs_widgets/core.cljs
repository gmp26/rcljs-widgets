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

(rum/defc a-square []
  [:div {:style {:width "100px"
                 :height "100px"
                 :background-color "red"}}])

(rum/defc filled-square [fill]
  [:div {:style {:width "100px"
                 :height "100px"
                 :background-color fill}}])

(defn mount-component [el]
  (println "mountcomponent called")
  (rum/mount (a-square) el))

(defn export [component]
  (set! (.-mountComponent js/window) component))

(export mount-component)


(defn main []
  ;; conditionally start the app based on whether the #app
  ;; node is on the page
  (if-let [node (.getElementById js/document "app")]
    (rum/mount (rum-tester) node)))

(main)