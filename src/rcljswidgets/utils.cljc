(ns rcljswidgets.utils)


(defn clamp
  "return a numeric value adjusted if necessary to be in range. Invalid values set the value to the default, or
  to the mid range value if there is no default."
  [lb value ub & [default]]
  (if (not #?(:clj (not (number? value)) :cljs (js/isNaN value)))
    (if (< value lb) lb (if (> value ub) ub value))
    (if default default (/ (+ ub lb) 2))))

(def epsilon 1e-6)

(defn fabs [r]
  "|r|"
  (if (pos? r) r (- r)))

(defn close0?
  "true if |a| < epsilon"
  [a]
  (< (fabs (- a 0)) epsilon))

(defn close?
  "true if |a - b| < epsilon"
  [a b]
  (if (close0? b)
    (close0? a)
    (close0? (fabs (dec (/ a b))))))

(defn all-close?
  "true if floating point values in s1 are close to floating point values in s2"
  [s1 s2]
  (every? true? (map close? s1 s2)))

(defn r-wrap
  "x as a vector or sequence if it isn't one already"
  [x]
  (if (or (vector? x) (seq? x)) x [x]))

(defn r-unwrap
  "if x is a vector or sequence with 1 value, return that value, else return x unchanged"
  [x]
  (if (= (rest x) ()) (first x) x))
