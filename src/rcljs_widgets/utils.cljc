(ns rcljs-widgets.utils)


(defn clamp [lb value ub & [default]]
  "return a numeric value adjusted if necessary to be in range. Invalid values set the value to the default, or
  to the mid range value if there is no default."
  (if (not #?(:clj (not (number? value)) :cljs (js/isNaN value)))
    (if (< value lb) lb (if (> value ub) ub value))
    (if default default (/ (+ ub lb) 2))))

