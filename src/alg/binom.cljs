(ns alg.binom
  (:require [rcljswidgets.utils :refer [fabs]]
            [rcljswidgets.r-call :refer [r-wrap r-unwrap cycled-apply]]))



;;;
;; Loaders algorithm for the binomial distribution
;;
;; http://savannah.gnu.org/bugs/download.php?file_id=24016
;;;

;; Accuracy is excessive for V8 at the moment (limit of 17 sig fig)
(def PI2 6.283185307179586476925286)
(def S0 0.083333333333333333333)                            ; 1/12 
(def S1 0.00277777777777777777778)                          ; 1/360
(def S2 0.00079365079365079365079365)                       ; 1/1260
(def S3 0.000595238095238095238095238)                      ; 1/1680
(def S4 0.0008417508417508417508417508)                     ; 1/1188
(def sfe [0 0.081061466795327258219670264
          0.041340695955409294093822081 0.0276779256849983391487892927
          0.020790672103765093111522771 0.0166446911898211921631948653
          0.013876128823070747998745727 0.0118967099458917700950557241
          0.010411265261972096497478567 0.0092554621827127329177286366
          0.008330563433362871256469318 0.0075736754879518407949720242
          0.006942840107209529865664152 0.0064089941880042070684396310
          0.005951370112758847735624416 0.0055547335519628013710386899])


;;;
;; stirlerr(n) = log(n!) - log( sqrt(2*pi*n)*(n/e)^n ) */
;;;
(defn stirlerr [n]
  (if (< n 16)
    (sfe (int n))
    (let [nn (* n n)]
      (cond
        (> n 500) (/ (- S0 (/ S1 nn)) n)
        (> n 80) (/ (- S0 (/ (- S1 (/ S2 nn)) nn)) n)
        (> n 35) (/ (- S0 (/ (- S1 (/ (- S2 (/ S3 nn)) nn)) nn)) n)
        :else (/ (- S0 (/ (- S1 (/ (- S2 (/ (- S3 (/ S4 nn)) nn)) nn)) nn)) n)))))

;;;
;; Evaluate the deviance term
;; bd0(x,np) = x log(x/np) + np - x
;;;
(defn bd0 [x np]
  (let [x-np (- x np)
        x+np (+ x np)]
    (if (< (fabs x-np) (* 0.1 x+np))
      (let [
            v (/ x-np x+np)
            v2 (* v v)]
        (loop [ej (* 2 x v v v) j 1 s (/ (* x-np x-np) x+np)]
          (let [s1 (+ s (/ ej (inc (* 2 j))))]
            (if (= s s1)
              s1
              (recur (* ej v2) (inc j) s1)))))
      (+ (* x (Math.log (/ x np))) np (- x)))))

;;;
;; Evaluate dbinom on 1 quantile
;;;
(defn dbinom1 [x n p]
  (if (zero? p)
    (if (zero? x) 1 0)
    (if (= 1 p)
      (if (= n x) 1 0)
      (if (zero? x)
        (Math.exp (* n (Math.log (- 1 p))))
        (if (= x n)
          (Math.exp (* n (Math.log p)))
          (* (Math.exp (- (stirlerr n)
                          (stirlerr x)
                          (stirlerr (- n x))
                          (bd0 x (* n p))
                          (bd0 (- n x) (* n (- 1 p)))))
             (Math.sqrt (/ n (* PI2 x (- n x))))))))))

;;;
;; Evaluate dbinom on 1 or more quantiles.
;;
;; Note that R's dbinom, pbinom, and qbinom take vector parameters which are recycled up to the length
;; of the longest vector, so we need cycled-apply.
;;
;;;
(defn dbinom [x n p]
  (cycled-apply dbinom1 x n p))

#_(defn dbinom [x n p]
  "x is a vector of n-quantile indexes
  Return their densities"
  (let [x (r-wrap x)
        result (map (fn [q] (dbinom1 q n p)) x)]
    (r-unwrap result)
    ))

;;;
;; Evaluate distribution function
;;;
(defn quantiles
  "probably better to call a memoized version of this function where possible"
  [n p]
  (into [] (reductions + (map #(dbinom % n p) (range (inc n))))))

(def m-quantiles (memoize quantiles))

(defn pbinom1 [x n p tails]
  "x is a single quantile index.
  We first calculate all quantiles up to (max x), and return only
  those indicated by x"
  ((if tails identity #(- 1 %)) ((m-quantiles n p) x)))

(defn pbinom
  "vectorised version of pbinom1 - allowing R-style vector parameters"
  [x n p & [tails]]
  (let [tails (if (nil? tails) true tails)]
    (cycled-apply pbinom1 x n p tails)))

;;;
;; Evaluate qbinom.
;;;
(defn qbinom1 [p size prob tails]
  "The quantile is defined as the smallest value x such that F(x) ≥ p,
  where F is the distribution function."
  (if tails
    (count (take-while #(< % p) (map #(pbinom1 % size prob tails) (range (inc size)))))
    (- size (dec (count (take-while #(< % p) (map #(pbinom1 % size prob tails) (range size -1 -1))))))))

(defn qbinom
  "vectorised version of qbinom1 - allowing R-style vector parameters"
  [p size prob & [tails]]
  (let [tails (if (nil? tails) true tails)]
    (cycled-apply qbinom1 p size prob tails)))

;;;
;; Evaluate dpois
;;;
(defn dpois [x lb]
  (if (zero? lb)
    (if (zero? x) 1 0)
    (if (zero? x)
      (Math.exp (- lb))
      (/ (Math.exp (- (stirlerr x) (bd0 x lb))) (Math.sqrt (* PI2 x))))))

;;;
;; direct multiplication for checking
;;;
(defn dbinom-mult [x n p]
  (if (> (* 2 x) n)
    (dbinom-mult (- n x) (- 1 p))
    (loop [f 1 j0 0 j1 0 j2 0]
      (if (or (< j0 x) (< j1 x) (< j2 (- n x)))
        (if (and (< j0 x) (< f 1))
          (recur (* f (/ (+ (- n x) j0) j0)) (inc j0) j1 j2)
          (if (< j1 x)
            (recur (* f p) j0 (inc j1) j2)
            (recur (* f (- 1 p)) j0 j1 (inc j2))))
        f))))
