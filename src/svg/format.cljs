(ns svg.format
  (:require [cljs.pprint :refer [cl-format]]
            [clojure.string :refer [index-of]]))


(defn scientific [x & [p]]
  "returns the number in scientific notation - (scientific 6) => \"6e+0\""
  (if (nil? p)
    (.toExponential (js/Number. x))
    (.toExponential (js/Number. x) p)))

(defn formatDecimal [x & [p]]
  "Computes the decimal coefficient and exponent of the specified number x with   significant digits p, where x is positive and p is in [1, 21] or undefined.
  For example, formatDecimal(1.23) returns [\"123\", 0]."
  (let [x (if (nil? p)
            (scientific x)
            (scientific x (dec p)))
        i (index-of x "e")]
    (if (< i 0)
      nil
      (let [coefficient (subs x 0 i)]
        [(if (> (count coefficient) 1)
           (str (get coefficient 0) (subs coefficient 2))
           coefficient
           ) (subs x (inc i))]
        ))))
