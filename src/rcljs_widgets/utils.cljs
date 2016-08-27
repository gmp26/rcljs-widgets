(ns rcljs-widgets.utils)


(defn clamp [lb value ub & [default]]
  "return a numeric value adjusted if necessary to be in range. Invalid values set the value mid range"
  (if (not (js/isNaN value))
    (if (< value lb) lb (if (> value ub) ub value))
    (if default default (/ (+ ub lb) 2))))

