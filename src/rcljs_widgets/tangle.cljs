(ns rcljs-widgets.tangle
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    ))

(defn foo [{:keys [val min] :or {min 0}}]
  [val min]
  (let [val (or val min)]
    [val min]))


(rum/defc tangle-numeric [{:keys [value on-change minimum maximum steps
                                 pixel-distance class-name
                                 on-input format]
                          :or   {minimum  0 maximum 100 steps 100
                                 format   identity
                                 on-input (constantly minimum)}}]
  (let [lb minimum
        ub (if (< lb maximum) maximum (+ lb 100))
        val (if (<= lb value ub) (or value lb) lb)
        step (/ (- ub lb) (max 2 steps))
        ]
    [:div
     (str "val = " val
          "; min = " lb
          "; max = " ub
          "; step = " step)])
  )