(ns cards.tangles
  (:require
    [rum.core :as rum]
    [rcljs-widgets.core :as core]
    [rcljs-widgets.tangle :refer [tangle-numeric inline-tangle]]
    [rcljs-widgets.wrapped-react-tangle-js :refer [js-tangletext wrap-js-tangletext]]
    [example.data :refer [bref* cref* d* update-b* update-c* update-d*]]
    [clojure.string :as string]
    [pubsub.feeds :refer [create-feed ->Topic]]
    [cljs.test :refer-macros [is testing]]
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
  Calling `tangle-numeric` then creates the react element. Here we ensure that the tangle steps by £1, and returns an integer value.

  In general, (parse (format x) == x for all x of interest (integers in [0,10] here)

  ```clojure
  (tangle-numeric bref* update-b*
                  {:minimum 0 :maximum 10 :step 1
                   :format #(str \"£\" %)
                   :parse #(js/parseInt (string/replace % #\"[^\\d.]\" \"\")})
  ```

  ###...yielding"

  (tangle-numeric bref* update-b*
                  {:minimum        0 :maximum 10 :step 1
                   :pixel-distance 5
                   :format         #(str "£" (js/Math.round %))
                   :parse          #(js/parseInt (string/replace % #"£" ""))}))

(defcard inline-tangles
         "#Inline tangle usage

         This is where tangles come into their own - they provide the ability to make statements that allow complex relations to be explored interactively.
         Let's test a few tangles out at once.
         ```clojure
         [:span \"Embedding a tangle \" (tangle-numeric cref* update-c*) \" inline.\"]
         ```
         "
         ((rum/defc foo []
             [:span
              [:p (inline-tangle cref* update-c*)]

              [:p (tangle-numeric d* update-d*
                                  {:minimum 0 :maximum 10 :step 0.1
                                   :format  #(.toFixed (js/Number. %) 1)
                                   :parse   #(/ (js/Math.floor (* 10 (js/parseFloat %1))) 10)})]]))
         )

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
  ;(js-tangletext)
  (wrap-js-tangletext cref* update-c*
                      {:minimum 0 :maximum 10 :step 0.1})
  )

(deftest
  just-checking
  "##Docs here"
  (testing
    "success"
    (is true "success")
    "failure"
    (is false "failure")))

