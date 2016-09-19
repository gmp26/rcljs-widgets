(ns svg.format
  (:require [cljs.pprint :refer [cl-format]]))


(defn scientific [x & [p]]
  "returns the number in scientific notation - (scientific 6) => \"6e+0\""
  (if (nil? p)
    (.toExponential (js/Number. x))
    (.toExponential (js/Number. x) p)))

;(defn formatDecimal [x p]
;  "Computes the decimal coefficient and exponent of the specified number x with   significant digits p, where x is positive and p is in [1, 21] or undefined.
;  For example, formatDecimal(1.23) returns [\"123\", 0]."
;
;  (if (< (index-of (scientific x) "e") 0)
;    nil
;    (if (nil? p)
;      (index-of (scientific x) "e")
;      (scientific x (dec p))
;      ))
;
;
;
;  (let [i ])
;  )