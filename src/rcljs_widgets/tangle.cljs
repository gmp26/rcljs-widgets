(ns rcljs-widgets.tangle
  (:require
    [rum.core :as rum]
    [pubsub.feeds :as q]
    ))

(defn foo [boo & [{:keys [val min] :or {min 0}}]]
  [val min]
  (let [val (or val min)]
    [val min]))

(defonce tn (atom {:a {:b 7}}))

(def tangle-cursor (rum/cursor-in tn [:a :b]))

(rum/defcs tangle-numeric <
  rum/reactive
  (rum/local false ::mouse-down?)
  [state value &
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

    [:div
     [:input.react-tangle-input {:key 1
                                 :type            "number"
                                 :value           (rum/react value)
                                 :min             lb
                                 :max             ub
                                 :step            step
                                 :style           {:width "30px"}
                                 :on-change       (fn [e] (reset! value (.-value (.-target e))))
                                 :on-double-click (fn [e] (.focus (.-target e)))
                                 :on-blur         (fn [e])}]
     [:div {:key 2}
      (str "val = " (rum/react value)
           "; min = " lb
           "; max = " ub
           "; step = " step)]]

    )
  )
