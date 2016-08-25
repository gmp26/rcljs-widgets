(ns cards.tangles
  (:require
    [rcljs-widgets.core :as core]
    [rcljs-widgets.tangle :refer [tangle-numeric inline-tangle]]
    [example.data :refer [bref* cref* update-b* update-c*]]
    [clojure.string :as string]
    [pubsub.feeds :refer [create-feed ->Topic]]
    )
  (:require-macros
    [devcards.core :refer [defcard-doc defcard deftest]]
    ))

(enable-console-print!)

;; Visit http://localhost:3449/cards.html to see this

(defcard-doc
  "#Replicating Bret Victor's tanglekit in clojurescript.")

(defcard
  "##tangle-numeric usage.

  `db*` is an atom containing `{:a {:b 7 :c 9}}` and we use `rum/cursor-in` to create a *watched atom-like* ref the `7`.
  ```clojure
  (ns my-ns
    (:require
      [pubsub.feeds :refer [create-feed ->Topic subscribe]]
      [rcljs-widgets.tangle :refer [tangle-numeric]])

  (defonce db* (atom {:a {:b 7 :c 9}}))
  (defonce bref* (rum/cursor-in db* [:a :b]))            ; allow rum component to watch :b
  (defonce cref* (rum/cursor-in db* [:a :c]))            ; and :c

  (defonce feed* (create-feed))                          ; create a feed to carry
  (defonce update-b* (->Topic :bref feed*))              ;   - an 'update :b' topic
  (defonce update-c* (->Topic :cref feed*))              ;   - and an 'update :c' topic

  (subscribe update-b*
             (fn [_ value]
               (println \"received \" value)
               (swap! db* update-in [:a :b] (fn [_] value))))

  (subscribe update-c*
             (fn [_ value]
                (println \"inline \" value)
                (swap! db* update-in [:a :c] (fn [_] value))))

  ```"
  (tangle-numeric bref* update-b*
                         {:minimum 0 :maximum 10 :step 1
                          :format #(str "£" %)
                          :parse #(string/replace % #"\D" "")}))


(defcard inline-tangle
  "#Inline tangle usage

  This is where tangles come into their own - they provide the ability to make statements that describe complex relations.

  Tangle-numeric can be embedded inline to provide adjustable values within a sentence."
  (inline-tangle cref* update-c*))

(defcard
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
  resulting in:"
  (core/tangle-card)
  )

