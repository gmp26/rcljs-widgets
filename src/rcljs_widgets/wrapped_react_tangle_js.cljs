(ns rcljs-widgets.wrapped-react-tangle-js
  (:require [rum.core :as rum]
            [pubsub.feeds :refer [publish]]
            [rcljs-widgets.utils :refer [clamp]]))

(enable-console-print!)

(rum/defc js-tangletext []
  (.createElement js/React js/TangleText #js {:value 6 :min 0 :max 10 :step 0.1 :onChange #(.log js/console %1)})
  )


(rum/defc wrap-js-tangletext < rum/reactive
  [value output-stream &
   [{:keys [minimum maximum step
            pixel-distance class
            format
            parse]
     :or   {minimum        -Infinity
            maximum        Infinity
            step           1
            class          "react-tangle-input"
            pixel-distance 0
            format         identity
            parse          #(js/parseInt %)}}]]

  (let [lb minimum
        step (if (pos? step) step 1)
        ub (if (< lb maximum) maximum (+ (* step 10)))
        validate (comp #(clamp lb % ub) parse)]

    (.createElement js/React js/TangleText #js
        {:value      (rum/react value) :min lb :max ub :step step
         :format     format
         :class-name class :pixel-distance pixel-distance
         :onChange   (fn [val] (publish output-stream (validate val)))
         })))

