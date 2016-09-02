(ns events.pointers)

(defn- touch-x-y
  "get position of first changed touch"
  [event]
  (let [touch (aget (.-changedTouches event) 0)]
    [(.-clientX touch) (.-clientY touch)]))

(defn- mouse-x-y
  "get mouse position"
  [event]
  [(.-clientX event) (.-clientY event)])

(defn event-x-y
  "get touch or mouse position from touch or mouse event"
  [event]

  (let [type (subs (.-type event) 0 5)]
    (condp = type
      "mouse" (mouse-x-y event)
      "touch" (touch-x-y event)

      ; move this to a {:pre } clause?
      ; (deb (str "strange event " (.type event)) [0 0])
      )))

(defn event-x [event]
  (first (event-x-y event)))

(defn event-y [event]
  (first (event-x-y event)))
