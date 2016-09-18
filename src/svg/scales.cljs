(ns svg.scales
  (:require [svg.ticks :as ts]))

(defprotocol IScale
  (i->o [_])
  (o->i [_])
  (in [_])
  (out [_])
  (ticks [_])
  )

(defn- scale-ticks [a-scale tick-count]
  (apply ts/ticks (conj (:in a-scale) tick-count)))

(defrecord Identity [in tick-count]
  IScale
  (i->o [this] identity)
  (o->i [this] identity)
  (in [this] (:in this))
  (out [this] (:in this))
  (ticks [this] (scale-ticks this tick-count)))

(defn- linear [[x1 x2] [y1 y2]] (fn [x] (+ y1 (* (/ (- x x1) (- x2 x1)) (- y2 y1)))))

(defn- linear-nice [[start stop :as input] & [p-count]]
  "Return a nice domain given an "
  (let [n (if (nil? p-count) 10 p-count)
        step (ts/tick-step start stop n)]
    (if (not (or (js/isNaN step) (nil? step)))

      (let [step (ts/tick-step (* (Math.floor (/ start step)) step)
                               (* (Math.ceil (/ stop step)) step)
                               n)]
        [(* (Math.floor (/ start step)) step)
         (* (Math.ceil (/ stop step)) step)])

      input))
  )

(defn nice-identity [in tick-count]
  (->Identity (linear-nice in tick-count) tick-count))

;scale.nice = function (count)
;{
; var d = domain (),
;     i = d.length - 1,
; n   = count == null ? 10 : count,
;     start = d [0],
;     stop = d [i],
;     step = tickStep (start, stop, n)                       ;
;
;     if (step) {
;                step   = tickStep (Math.floor (start / step) * step, Math.ceil (stop / step) * step, n) ;
;                d      [0] = Math.floor (start / step) * step ;
;                       d [i] = Math.ceil (stop / step) * step ;
;                domain (d)                                  ;
;                }


(defrecord Linear [in out tick-count]
  IScale
  (i->o [this] (linear (:in this) (:out this)))
  (o->i [this] (linear (:out this) (:in this)))
  (in [this] (:in this))
  (out [this] (:out this))
  (ticks [this] (scale-ticks this tick-count)))

(defn nice-linear [in out tick-count]
  (->Linear (linear-nice in tick-count) out tick-count))

