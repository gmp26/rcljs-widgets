(ns rcljs-widgets.tangle
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    ))

(defn foo [{:keys [val min] :or {min 0}}]
  [val min]
  (let [val (or val min)]
    [val min]))

(defonce tn (atom {:a {:b 7}}))

(def tangle-cursor (rum/cursor-in tn [:a :b]))

(rum/defcs tangle-numeric <
  rum/reactive
  (rum/local false ::mouse-down?)
  [state value &
   {:keys [value on-change minimum maximum steps
           pixel-distance class-name
           on-input format]
    :or   {minimum  -Infinity
           maximum  Infinity
           step 1
           format   identity
           pixelDistance nil
           on-input (constantly minimum)}}]
  (let [lb minimum
        ub (if (< lb maximum) maximum (+ lb 100))
        val (rum/react value)
        step (/ (- ub lb) (max 2 steps))
        ]

    [:div
     [:input.react-tangle-input {:type            "number"
                                 :value           (rum/react val)
                                 :min             lb
                                 :max             ub
                                 :style           {:width "30px"}
                                 :on-change       (fn [e] (swap! val (.-value (.-target e))))
                                 :on-double-click (fn [e] (.focus (.-target e)))
                                 :on-blur         (fn [e])}]
     [:div
      (str "val = " (rum/react val)
           "; min = " lb
           "; max = " ub
           "; step = " step)]]

    )
  )
