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
  "##Tangle-numeric usage.

  Each `tangle-numeric` component is a little like a `type=\"range\"` input element where the value can be changed
  by dragging it to the right or left. Double clicking on the value gives it focus and allows the user to enter numbers
  or press the up and down keys to adjust.

  It has two required parameters:

  1. a reference to the value to be rendered, and
  1. a [topic feed](https://github.com/gmp26/pubsub) on which to report changes to that value.

  These may be followed by a map of optional parameters. Here they are with their default values:
  ```clojure
  {:minimum        -Infinity
   :maximum        Infinity
   :step           1
   :class          \"react-tangle-input\"  ; becomes the CSS class of the component
   :pixel-distance 1                       ; How many pixels to drag to change the value by one step
   :format         identity                ; a function which formats the numeric value as text (e.g. to add a unit)
   :parse          #(js/parseInt %)}       ; a function which parses the displayed string to retrieve a number
  ```


  ### Providing a value reference
  There are many ways to provide a value ref for `tangle-numeric` to watch and render. It can be an atom, or a [cursor](https://github.com/tonsky/rum) path
  to a value within an atom, or a [derived atom](https://github.com/tonsky/rum), or a [derivative](https://github.com/martinklepsch/derivatives).

  Here `db*` is an atom containing `{:a {:b 7 :c 9}}` and we use `rum/cursor-in` to define cursors pointing to the values at `:b` and `:c`.

  ### Updating the value reference
  The parse function we supply (by default `#(js/parseInt %)`) should convert the input string
  to a numeric value. Changes to the number will be returned on the topic feed we give as second parameter.

  We subscribe a handler to the topic feed that has the job of updating the value reference with the new value.


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

  ```
  Calling `tangle-numeric` then creates the react element:
  ```clojure
  (tangle-numeric bref* update-b*
                  {:minimum 0 :maximum 10 :step 1
                   :format #(str \"£\" %)
                   :parse #(string/replace % #\"\\D\" \"\")})
  ```

  ###...yielding"

  (tangle-numeric bref* update-b*
                  {:minimum 0 :maximum 10 :step 1
                   :format  #(str "£" %)
                   :parse   #(js/parseInt (string/replace % #"\D" ""))})
  )


(defcard inline-tangle
  "#Inline tangle usage

  This is where tangles come into their own - they provide the ability to make statements that allow complex relations to be explored interactively.
  Tangle-numeric can be embedded inline to provide adjustable values within a sentence.
  ```clojure
  [:span \"Embedding a tangle \" (tangle-numeric cref* update-c*) \" inline.\"]
  ```
  "
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

