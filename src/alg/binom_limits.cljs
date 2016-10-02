(ns alg.binom-limits
  (:require [alg.binom :refer [dbinom1 pbinom1 qbinom1]]
            [rcljswidgets.utils :refer [epsilon]]
            [rcljswidgets.r-call :refer [cycled-apply]]))


;####################################################################
;# p-percentile of binomial proportion with denom n and prob target.
;####################################################################
;qbinom.interp<-function(p, denom, target,tail)
;#  p-percentile of binomial proportion with denom n and prob target.
;# NB target is a vector of same length as n
;# different depending on whether p is lower or upper tail
;# need to check can't go above 100 or below 0.
;{
; if(tail=="lower"){
;                   rp = qbinom(p, denom, target) # this is lowest R such that df>p
;                   alpha = (pbinom(rp,denom,target) - p)/dbinom(rp,denom,target)
;                   x<-  pmax( (rp - alpha)/denom , 0.00001)
;                   }
;   if(tail=="upper"){
;                     rp = qbinom(1-p, denom, target) # this is highst x such that P(>=x) >p
;                     alpha = (pbinom(rp,denom,target) - (1-p)) / dbinom(rp,denom,target)
;                     x<- pmin(  (rp + 1 - alpha)/denom , 0.99999)
;                     }
; x
; }

(defn qbinom-interp1
  "p-percentile of binomial proportion with denom n and prob target.
  NB. target will become a vector of same length as n (denom) in public qbinom-interp
  Different depending on whether p is lower or upper tail.
  Need to check can't go above 100 or below 0."

  [p denom target tail]
  (if (= tail :lower)
    (let [rp (qbinom1 p denom target)                       ; lowest R such that df > p
          alpha (/ (- (pbinom1 rp denom target) p) (dbinom1 rp denom target))]
      (max (/ (- rp alpha) denom) epsilon))                 ; why not zero?
    ;; else (= tail :upper)
    (let [rp (qbinom1 (- 1 p) denom target)                 ; highest such that P(>= x) > p
          alpha (/ (+ (pbinom1 rp denom target) -1 p) (dbinom1 rp denom target))]
      (min (/ (- rp -1 alpha) denom) (- 1 epsilon)))))

(def m-qbinom-interp (memoize qbinom-interp1))

(defn qbinom-interp
  "with R vector parameters"

  [p denom target tail]
  (cycled-apply qbinom-interp1 p denom target tail))

;;
;; R-code
;;
;binomial.limits=function (precisions, target, tails)
;{
; num.limits<-length (tails)
; lowerlimits <- upperlimits <- matrix (0, length (precisions), num.limits)
; for (j in 1:num.limits) {
;   lowerlimits [, j] <- qbinom.interp (tails [j], precisions, target, tail= "lower")
;   upperlimits [, j] <- qbinom.interp (tails [j], precisions, target, tail= "upper")
; }
; cbind (lowerlimits, upperlimits)
;}
