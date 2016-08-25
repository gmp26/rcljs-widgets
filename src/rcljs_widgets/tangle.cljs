(ns rcljs-widgets.tangle
  "A simple tangle is a numeric input which allows the number to be adjusted by dragging. On double click, the tangle
  behaves like an ordinary numeric input, responding to keyboard number entry and up and down keys. The name is due to
  Bret Victor. The concept is  heavily used in Adobe user interfaces."
  (:require
    [rum.core :as rum]
    [pubsub.feeds :as feeds :refer [publish]]
    [example.data]
    [clojure.string :as s]
    ))

(defn clamp [lb value ub]
  "return a numeric value adjusted if necessary to be in range"
  (if (not (js/isNaN value))
    (if (< value lb)
      lb
      (if (> value ub)
        ub
        value))
    (do
      (println "bad value " value)
      (/ (+ ub lb) 2))))

(defn handle-down [event state value]
  (when (and (zero? (.-button event)) (not= (.-target event) (.-activeElement js/document)))
    ;(println "v = " @value)
    ;(println "screenX = " (.-screenX event))
    ;(println "down b " (.-button event))
    ;(println "down t " (.-target event))
    ;(println "down e " (.-activeElement js/document))
    ;(println "value v" @value )
    (.preventDefault event)
    (reset! (::mouse-down? state) true)
    (reset! (::dragged? state) false)
    (reset! (::start-x state) (.-screenX event))
    (reset! (::start-value state) @value)
    (println "start-v " @(::start-value state))
    (let [handlers @(::handlers state)]
      (js/addEventListener "mousemove" (:move handlers))
      (js/addEventListener "mouseup" (:up handlers)))))

(defn handle-move [event state output-stream lb ub step pixel-distance]
  (when @(::mouse-down? state)
    (let [moved (- (.-screenX event) @(::start-x state))
          change (int (if (pos? pixel-distance) (/ moved pixel-distance) moved))]
      (reset! (::dragged? state) false)
      (println "moved " moved)
      (println "change " change)
      (println "step " step)
      (println "start-v " @(::start-value state) " is string? " (string? @(::start-value state)))
      (publish output-stream (clamp lb (+ @(::start-value state) (* change step)) ub)))))

(defn handle-up [event state output-stream validate lb ub]
  (let [handlers @(::handlers state)]
    (js/removeEventListener "mousemove" (:move handlers))
    (js/removeEventListener "mouseup" (:up handlers)))
  (when @(::mouse-down? state)
    (reset! (::mouse-down? state) false)
    (.preventDefault event)
    (if @(::dragged? state)
      (reset! (::dragged? state) false)
      (publish output-stream (clamp lb (validate (.. event -target -value)) ub))
      )))

(def handle-out handle-up)

(defn derived-args [minimum maximum step parse]
  (let [ub (if (< minimum maximum) maximum (+ (* step 10)))]
    {:lb       minimum
     :step     (if (pos? step) step 1)
     :ub       ub
     :validate (comp #(clamp minimum % ub) parse)}))

(defn create-handlers [state]
  ;; create and save transient event handlers in local state
  (let [args (:rum/args state)
        [value output-stream & {:keys [minimum maximum step
                                       pixel-distance parse]
                                :or   {minimum        -Infinity
                                       maximum        Infinity
                                       step           1
                                       class          "react-tangle-input"
                                       pixel-distance 4
                                       format         identity
                                       parse          #(js/parseInt %)}}] args
        {:keys [lb step ub validate]} (derived-args minimum maximum step parse)
        ]

    (println "lb" lb "ub" ub "step" step "pxd" pixel-distance)

    (reset! (::handlers state)
            {::move #(handle-move %1 state output-stream lb ub step pixel-distance)
             ::up   #(handle-up %1 state output-stream validate lb ub)}))

  ; be sure to return state from mixin
  state)

(defn handle-change [event output-stream parse lb ub]
  (println "new value: " (parse (.. event -target -value)))
  (publish output-stream (clamp lb (parse (.. event -target -value)) ub)))

(rum/defcs tangle-numeric < rum/reactive
                            (rum/local false ::mouse-down?)
                            (rum/local false ::dragged?)
                            (rum/local 0 ::start-x)
                            (rum/local 0 ::start-value)
                            (rum/local {} ::handlers)
                            {:did-mount create-handlers}

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
            pixel-distance 4
            format         identity
            parse          #(js/parseInt %)}}]]

  (let [{:keys [lb step ub validate]} (derived-args minimum maximum step parse)]

    ;lb minimum                                          ;lower bound
    ;step (if (pos? step) step 1)
    ;ub (if (< lb maximum) maximum (+ (* step 10)))
    ;validate (comp #(clamp lb % ub) parse)]             ;upper bound

    [:input {:class           class
             :type            "text"
             :value           (format (rum/react value))
             :min             lb
             :max             ub
             :step            step
             :style           {:width "50px"}               ; todo - find a good way to size things
             :on-change       #(handle-change % output-stream validate lb ub)
             :on-mouse-down   #(handle-down %1 state value)
             ;:on-mouse-up     #(handle-up %1 state output-stream validate lb ub)
             ;:on-mouse-out    #(handle-out %1 state output-stream validate lb ub)
             ;:on-mouse-move   #(handle-move %1 state output-stream lb ub step pixel-distance)
             :on-double-click #(.focus (.-target %))
             :on-blur         #(handle-change % output-stream validate lb ub)}]))

(rum/defc inline-tangle < rum/static [value output-stream]
  [:span "Embedding a tangle " (tangle-numeric value output-stream) " inline. To change the value, drag the number
  to left or right. Edit by double clicking on it to gain focus. When the input has focus, up and down keys will step the value up or down and numeric keys will be entered. The widget emits the final value when focus is lost and when it changes."]
  )