(ns rcljswidgets.r-call)


(defn cycled-parameters
  "All R parameters are vectors, so when calling a function with a parameter list, we must first
  create a table of parameter values with as many entries as the longest vector. Shorter vectors are
  recycled."
  [& params]

  )
