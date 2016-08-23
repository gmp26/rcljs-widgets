(ns rcljs-widgets.tangle
  (:require
    [rum.core :as rum]
    [pubsub.feeds :as feeds :refer [publish]]
    [example.data]
    ))


(rum/defcs tangle-numeric < rum/reactive
  (rum/local false ::mouse-down?)
  [state value value-changes &
   [{:keys [minimum maximum step
            pixel-distance class-name
            on-input format]
     :or   {minimum       -Infinity
            maximum       Infinity
            step          10
            format        identity
            pixelDistance nil
            on-input      (constantly minimum)}}]]
  (let [lb minimum
        ub (if (< lb maximum) maximum (+ lb 100))
        step (if (<= 0 step (/ (- ub lb) 2))
               step
               (- ub lb))
        ]
    (letfn [(handle [event] (publish value-changes (.. event -target -value)))]
      [:div
       [:input.react-tangle-input {:key             1
                                   :type            "number"
                                   :value           (rum/react value)
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
