(ns rcljs-widgets.tangle
  (:require
    [rum.core :as rum]
    [pubsub.feeds :as feeds :refer [publish]]
    [example.data]
    ))


(rum/defcs tangle-numeric < rum/reactive
                            (rum/local false ::mouse-down?)
  ;;
  ;; add a formatter (value -> DOM) and a parser (DOM -> value)
  ;;
  [state value output-stream &        ; value is a number, output-stream is a pubsub topic
   [{:keys [minimum maximum step
            pixel-distance class
            format
            parse]
     :or   {minimum       -Infinity
            maximum       Infinity
            step          1
            class         "react-tangle-input"
            pixelDistance nil
            format        identity
            parse         identity}}]]
  (let [lb minimum                                          ;lower bound
        step (if (pos? step) step 1)
        ub (if (< lb maximum) maximum (+ (* step 10)))]     ;upper bounds
    (letfn [(handle [event] (do
                              (println "new value: " (parse (.. event -target -value)))
                              (publish output-stream (parse (.. event -target -value)))))]

      [:input {:class           class
               :type            "text"
               :value           (format (rum/react value))
               :min             lb
               :max             ub
               :step            step
               :style           {:width "30px"}
               :on-change       handle
               :on-double-click #(.focus (.-target %))
               :on-blur         handle}])))

(rum/defc inline-tangle < rum/static [value output-stream]
  [:span "Embedding a tangle " (tangle-numeric value output-stream) " inline. To change the value, drag the number
  to left or right. Edit by double clicking on it to gain focus. When the input has focus, up and down keys will step the value up or down and numeric keys will be entered. The widget emits the final value when focus is lost and when it changes."]
  )