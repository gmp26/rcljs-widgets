(ns svg.scales)

(defprotocol IScale
  (i->o [_])
  (o->i [_])
  (in [_])
  (out [_]))

(defrecord Linear [in out]
  IScale
  (i->o [this]
    (fn [x]
      (let [[x1 x2] (:in this)
            [y1 y2] (:out this)]
        (+ y1 (* (/ (- x x1) (- x2 x1)) (- y2 y1))))))
  (o->i [this]
    (fn [y]
      (let [[x1 x2] (:in this)
            [y1 y2] (:out this)]
        (+ x1 (* (/ (- y y1) (- y2 y1)) (- x2 x1))))))
  (in [this] (:in this))
  (out [this] (:out this)))

(defrecord Identity [in]
  IScale
  (i->o [this] identity)
  (o->i [this] identity)
  (in [this] (:in this))
  (out [this] (:in this)))