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
  [state value output-stream &                              ; value is a number, output-stream is a pubsub topic
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
      [:div
       [:input {:key             1
                :class           class
                :type            "text"
                :value           (format (rum/react value))
                :min             lb
                :max             ub
                :step            step
                :style           {:width "30px"}
                :on-change       handle
                :on-double-click #(.focus (.-target %))
                :on-blur         handle}]
       [:div {:key 2}
        (str "val = " (rum/react value)
             "; min = " lb
             "; max = " ub
             "; step = " step)]])

    )
  )
