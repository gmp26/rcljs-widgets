(ns rcljswidgets.r-call)

(defn r-wrap
  "x as a vector or sequence if it isn't one already"
  [x]
  (if (or (vector? x) (seq? x)) x [x]))

(defn r-unwrap
  "if x is a vector or sequence with 1 value, return that value, else return x unchanged"
  [x]
  (if (empty? (rest x)) (first x) x))

(defn n-cycle
  "lazily cycle values in v until up to length n"
  [n v]
  (take n (flatten (cycle (r-wrap v)))))

(defn cycled-parameters
  "All R parameters are vectors, so when calling a function with a parameter list,
  we must first create a lazy table of parameter tuples."
  [& params]
  (let [n (apply max (map (comp count r-wrap) params))]
    (map #(n-cycle n %) params)))

(defn transpose [m]
  (apply mapv vector m))

(defn cycled-apply
  "apply f to params, harmonising their lengths by cycling short vectors"
  [f & params]
  (r-unwrap (map #(apply f %) (transpose (apply cycled-parameters params)))))
