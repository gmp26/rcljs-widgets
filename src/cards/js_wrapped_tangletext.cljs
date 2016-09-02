(ns cards.js-wrapped-tangletext
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


(defcard-doc
  "# Wrapping a `react-tangle`
   ## - to connect it to app state and pubsub event channels\n\n  It's easy enough to make a component that wraps the js component, adapting it to communicate in the manner we want.\n")

(defcard
  "

```clj
(wrap-js-tangletext cref* update-c*
                    {:minimum 0 :maximum 10 :step 0.1})
```
  resulting in:"
  (wrap-js-tangletext cref* update-c*
                        {:minimum 0 :maximum 10 :step 0.1})
  )

(defcard
  "## Wrapping code
```clj
(rum/defc wrap-js-tangletext < rum/reactive\n  [value output-stream &\n   [{:keys [minimum maximum step\n            pixel-distance class\n            format\n            parse]\n     :or   {minimum        -Infinity\n            maximum        Infinity\n            step           1\n            class          \"react-tangle-input\"\n            pixel-distance 0\n            format         identity\n            parse          #(js/parseInt %)}}]]\n\n  (let [lb minimum\n        step (if (pos? step) step 1)\n        ub (if (< lb maximum) maximum (+ (* step 10)))\n        validate (comp #(clamp lb % ub) parse)]\n\n    (.createElement js/React js/TangleText #js\n        {:value      (rum/react value) :min lb :max ub :step step\n         :format     format\n         :class-name class\n         :pixel-distance pixel-distance\n         :onChange   (fn [val] (publish output-stream (validate val)))\n         })))
```"
  "")