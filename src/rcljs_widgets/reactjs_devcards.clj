(ns rcljs-widgets.reactjs-devcards
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    [rcljs-widgets.core :as core]
    [rcljs-widgets.tangle :as tangle]
    [example.data :refer [*cursor]]
    )
  (:require-macros
    [devcards.core :as dc :refer [defcard-doc defcard deftest]]
    ))

(enable-console-print!)

;; Visit http://localhost:3449/cards.html to see this


(defcard-doc
  " ## Interop with a javascript reactjs library
  If we load an existing react tangle library after cljs has loaded react_js
  ```html
      <script src=\"/js/react-tangle.js\" type=\"text/javascript\"></script>
  ```
  We can proceed to use it
  ```clj
  (rum/defc tangle-card []
    [:div
       (.createElement js/React js/TangleText
                       #js {:value 6 :min 0 :max 10 :step 0.1 :onChange #(.log js/console %1)})])

  (defcard tangle-card
    (core/tangle-card))
  ```
  resulting in:")

(defcard tangle-card
         (core/tangle-card))
