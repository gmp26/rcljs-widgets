(ns cards.tangles
  (:require
    [rum.core :as rum]
    [rcljs-widgets.core :as core]
    [rcljs-widgets.tangle :refer [tangle-numeric inline-tangle]]
    [rcljs-widgets.wrapped-react-tangle-js :refer [js-tangletext wrap-js-tangletext]]
    [example.data :refer [bref* cref* d* update-b* update-c* update-d*]]
    [clojure.string :refer [replace]]
    [pubsub.feeds :refer [create-feed ->Topic]]
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [defcard-doc defcard deftest]]
    ))

(enable-console-print!)

;; Visit http://localhost:3449/cards.html to see this

(defcard-doc
  "#Replicating Bret Victor's tanglekit in clojurescript."
  )

(defcard
  "##Tangle-numeric
    Each `tangle-numeric` component is a little like a `type=\"range\"` input element where the value can be changed\n  by dragging it to the right or left. Double clicking on the value gives it focus and allows the user to enter numbers\n  or press the up and down keys to adjust."
  (tangle-numeric bref* update-b*
                  {:minimum        0 :maximum 10 :step 1
                   :pixel-distance 5
                   :format         #(str "£" (js/Math.round %))
                   :parse          #(js/parseInt (replace (str %) #"£" ""))}))

(defcard
  "##Tangle-numeric usage.

  It has two required parameters:

  1. a ref to the value to be rendered, and
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

  In general, (parse (format x) == x for all x of interest (integers in [0,10] here)

  ```clojure
  (tangle-numeric bref* update-b*
                  {:minimum 0 :maximum 10 :step 1
                   :format #(str \"£\" %)
                   :parse #(js/parseInt (replace % #\"[^\\d.]\" \"\")})
  ```

  *...yielding* "

  (tangle-numeric bref* update-b*
                  {:minimum        0 :maximum 10 :step 1
                   :pixel-distance 5
                   :format         #(str "£" (js/Math.round %))
                   :parse          #(js/parseInt (replace (str %) #"£" ""))}))

(defcard inline-tangles
  "#Inline tangle usage

  This is where tangles come into their own - they provide the ability to make statements that allow complex relations to be explored interactively.

```clojure
\"Embedding a tangle \"
(rum/defc inline []
  (tangle-numeric cref*
                  update-c*
                  {:minimum 0 :maximum 10 :step 0.1
                  :format  #(.toFixed (js/Number. %) 1)
                  :parse   #(/ (js/Math.floor (* 10 (js/parseFloat %1))) 10)}))
\" inline\"
```
"
  ((rum/defc inline []
     [:span " Embedding a tangle " (tangle-numeric cref*
                                                  update-c*
                                                  {:minimum 0 :maximum 10 :step 0.1
                                                   :format  #(.toFixed (js/Number. %) 1)
                                                   :parse   #(/ (js/Math.floor (* 10 (js/parseFloat %1))) 10)})
      " inline. To change the value, drag the number
  to left or right. Edit by double clicking on it to gain focus. When the input has focus, up and down keys will step the value up or down and numeric keys will be entered. "]
     ))
  )



