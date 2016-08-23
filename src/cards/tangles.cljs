(ns cards.tangles
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    [rcljs-widgets.core :as core]
    [rcljs-widgets.tangle :refer [tangle-numeric inline-tangle]]
    [example.data :refer [db* tangle-events* tangle-inline*]]
    [clojure.string :as string]
    [pubsub.feeds :refer [create-feed ->Topic]]
    )
  (:require-macros
    [devcards.core :as dc :refer [defcard-doc defcard deftest]]
    ))

(enable-console-print!)

;; Visit http://localhost:3449/cards.html to see this

(defcard-doc
  "#Replicating Bret Victor's tanglekit in clojurescript.")

(defcard tangle-numeric
  "##tangle-numeric usage.

  `db*` is an atom containing `{:a {:b 7 :c 9}}` and we use `rum/cursor-in` to create a *watched atom-like* ref the `7`.
  ```
  (ns my-ns
    (:require
      [pubsub.feeds :refer [create-feed ->Topic subscribe]]
      [rcljs-widgets.tangle :refer [tangle-numeric]])

  (defonce db* (atom {:a {:b 7 :c 9}}))
  (defonce feed* (create-feed))
  (defonce tangle-events* (->Topic :tangle feed*))
  (subscribe tangle-events*
             (fn [_ value]
               (swap! db* update-in [:a :b] (fn [_] value))))

  (tangle-numeric (rum/cursor-in db* [:a :b])
    (->Topic :tangle tangle-events*)
  ```"
  (tangle-numeric (rum/cursor-in db* [:a :b]) tangle-events*
                         {:minimum 0 :maximum 10 :step 5
                          :format #(str "£" %)
                          :parse #(string/replace % #"\D" "")}))


(defcard inline-tangle
  "Tangle-numeric can be embedded inline to provide adjustable values within a sentence."
  (inline-tangle (rum/cursor-in db* [:a :c]) tangle-inline*))

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
