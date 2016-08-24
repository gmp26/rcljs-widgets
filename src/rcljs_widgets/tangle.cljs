(ns rcljs-widgets.tangle
  (:require
    [rum.core :as rum]
    [pubsub.feeds :as feeds :refer [publish]]
    [example.data]
    ))

(defn handle-down [event state value]
  (when (and (zero? (.-button event)) (not= (.-target event) (.-activeElement js/document)))
    (println "v = " @value)
    (println "screenX = " (.-screenX event))
    (println "down b " (.-button event))
    (println "down t " (.-target event))
    (println "down e " (.-activeElement js/document))
    (println "value v" @value)
    (.preventDefault event)
    (reset! (::mouse-down? state) true)
    (reset! (::dragged? state) false)
    (reset! (::start-x state) (.-screenX event))
    (reset! (::start-value state) @value)
    (println "start-v " @(::start-value state))
    )
  )

(defn handle-move [event state output-stream step pixel-distance]
  (when @(::mouse-down? state)
    (let [moved (- @(::start-x state) (.-screenX event))
          change (int (if (pos? pixel-distance) (/ moved pixel-distance) moved))]
      (reset! (::dragged? state) false)
      (println "moved " moved)
      (println "change " change)
      (println "step " step)
      (println "start-v " @(::start-value state))
      (publish output-stream (- @(::start-value state) (* change step))))))

(defn handle-up [event state output-stream parse]
  (when @(::mouse-down? state)
    (.preventDefault event)
    (if @(::dragged? state)
      (reset! (::dragged? state) false)
      ;; todo: add validation
      (publish output-stream (parse (.. event -target -value)))
      )
    )
  )

(rum/defcs tangle-numeric < rum/reactive
                            (rum/local false ::mouse-down?)
                            (rum/local false ::dragged?)
                            (rum/local 0 ::start-x)
                            (rum/local 0 ::start-value)
                            {:will-update (fn [state]
                                            (.log js/console @(::mouse-down? state))
                                            state)}
  ;;
  ;; add a formatter (value -> DOM) and a parser (DOM -> value)
  ;;
  [state value output-stream &                              ; value is a number, output-stream is a pubsub topic
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
            parse          identity}}]]

  (let [lb minimum                                          ;lower bound
        step (if (pos? step) step 1)
        ub (if (< lb maximum) maximum (+ (* step 10)))]     ;upper bound
    (letfn [(handle [event] (do
                              (println "new value: " (parse (.. event -target -value)))
                              ;; todo: add validation
                              (publish output-stream (parse (.. event -target -value)))))]

      [:input {:class           class
               :type            "text"
               :value           (format (rum/react value))
               :min             lb
               :max             ub
               :step            step
               :style           {:width "30px"}             ; todo - find a good way to size things
               :on-change       handle
               :on-mouse-down   #(handle-down %1 state value)
               :on-mouse-up     #(handle-up %1 state output-stream parse)
               :on-mouse-move   #(handle-move %1 state output-stream step pixel-distance)
               :on-double-click #(.focus (.-target %))
               :on-blur         handle}])))

(rum/defc inline-tangle < rum/static [value output-stream]
  [:span "Embedding a tangle " (tangle-numeric value output-stream) " inline. To change the value, drag the number
  to left or right. Edit by double clicking on it to gain focus. When the input has focus, up and down keys will step the value up or down and numeric keys will be entered. The widget emits the final value when focus is lost and when it changes."]
  )