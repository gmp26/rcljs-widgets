(ns rcljswidgets.tangle
  "A simple tangle is a numeric input which allows the number to be adjusted by dragging. On double click, the tangle
  behaves like an ordinary numeric input, responding to keyboard number entry and up and down keys. The name is due to
  Bret Victor. The concept is  heavily used in Adobe user interfaces."
  (:require
    [rum.core :as rum]
    [pubsub.feeds :refer [publish]]
    [events.pointers :refer [event-x]]
    [rcljswidgets.utils :refer [clamp]]
    [cljs-css-modules.macro :refer-macros [defstyle]]
    ))

;;;
;; Using cljs-css-modules to localise component styles - this allows all html, css and js to be defined for this
;; component in one file. :)
;;;
(defstyle styles
  [
   [".tangle" {:border        0
               :text-align    "center"
               :cursor        "col-resize"
               :font          "inherit"
               :border-bottom "2px dotted #88f"
               :display       "inline"
               :width         "40px"
               :color         "#88f"
               }]

   [".tangle:focus" {:cursor "text"}]
   ])


;;;
;; Basic event handling follows the usual reactjs pattern. However, since this component needs dragging to work
;; beyond its own screen boundary, we have to attach and remove window event handlers manually. State related to dragging
;; is uninteresting to the app so we keep it local.
;;;
(defn- handle-down [event state value]
  (when (and (zero? (.-button event)) (not= (.-target event) (.-activeElement js/document)))
    (.preventDefault event)
    (reset! (::mouse-down? state) true)
    (reset! (::dragged? state) false)
    (reset! (::start-x state) (event-x event))
    (reset! (::start-value state) @value)
    (let [handlers @(::handlers state)]
      (doseq [type ["mousemove" "touchmove"]]
        (js/addEventListener type (::move handlers)))
      (doseq [type ["mouseup" "touchend"]]
        (js/addEventListener type (::up handlers))))))

(defn- handle-move [event state output-stream validate step pixel-distance]
  (when @(::mouse-down? state)
    (let [moved (- (event-x event) @(::start-x state))
          change (if (pos? pixel-distance) (/ moved pixel-distance) moved)]
      (reset! (::dragged? state) false)
      (let [new-value (validate (+ @(::start-value state) (* change step)))]
        (publish output-stream new-value)))))

(defn- handle-up [event state]
  (let [handlers @(::handlers state)]
    (doseq [type ["mousemove" "touchmove"]]
      (js/removeEventListener type (::move handlers)))
    (doseq [type ["mouseup" "touchend"]]
      (js/removeEventListener type (::up handlers))))
  (reset! (::mouse-down? state) false)
  (reset! (::dragged? state) false)
  (.preventDefault event))

(defn- handle-change [event output-stream parse lb ub]
  ;(println "new value: " (parse (.. event -target -value)))
  (publish output-stream (clamp lb (parse (.. event -target -value)) ub)))

(rum/defcs tangle-numeric < rum/reactive
                            (rum/local false ::mouse-down?)
                            (rum/local false ::dragged?)
                            (rum/local 0 ::start-x)
                            (rum/local 0 ::start-value)
                            (rum/local {} ::handlers)
  [state value output-stream &                              ;value is a number, output-stream is a pubsub topic
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
    (reset! (::handlers state)
            {::move #(handle-move %1 state output-stream validate step pixel-distance)
             ::up   #(handle-up %1 state)})
    [:input {:class-name      (:tangle styles)
             :type            "text"
             :value           (format (rum/react value))
             :min             lb
             :max             ub
             :step            step
             :on-change       #(handle-change % output-stream validate lb ub)
             :on-mouse-down   #(handle-down %1 state value)
             :on-touch-start  #(handle-down %1 state value)
             :on-double-click #(.focus (.-target %))
             :on-blur         #(handle-change % output-stream validate lb ub)}]))

