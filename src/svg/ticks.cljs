(ns svg.ticks)

(def e10 (Math.sqrt 50))
(def e5 (Math.sqrt 10))
(def e2 (Math.sqrt 2))

(defn tick-step [start stop preferred-count]
  (let [step0 (/ (Math.abs (- stop start)) (max 0 preferred-count))
        step1 (Math.pow 10 (Math.floor (/ (Math.log step0) Math.LN10)))
        error (/ step0 step1)
        step (cond
               (>= error e10) (* 10 step1)
               (>= error e5) (* 5 step1)
               (>= error e2) (* 2 step1)
               :else step1)]
    (if (< stop start) (- step) step)))

(defn ticks [start stop preferred-count]
  (let [step (tick-step start stop preferred-count)]
    (range
      (* (Math.ceil (/ start step)) step)
      (+ (* (Math.floor (/ stop step)) step) (/ step 2))
      step)))

