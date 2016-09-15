(ns tests.dbinom
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [alg.binom :refer [fabs dbinom dbinom-mult]]
    ))

(defn close0 [a]
  (< (fabs (- a 0)) 1e-6))

(defn close [a b]
  (if (close0 b)
    (close0 a)
    (close0 (fabs (dec (/ a b))))))

(deftest
  loaders-algorithm-for-dbinom
  (testing
    "agreement with R dbinom"
    (is (close (dbinom 1 1 1) 1) "x = 1, n = 1, p = 1")
    (is (close (dbinom 0 1 0) 1) "x = 0, n = 1, p = 0")
    (is (close (dbinom 0 1000 0) 1) "x = 0, n = 1000, p = 0")
    (is (close (dbinom 1000 1000 1) 1) "x = 0, n = 1000, p = 1")
    (is (close (dbinom 0 20 0.8) 1.048576e-14) "x = 0, n = 20, p = 0.8")
    (is (close (dbinom 20 20 0.8) 0.01152922) "x = 20, n = 20, p = 0.8")
    (is (close (dbinom 1 20 0.8) 8.388608e-13) "x = 1, n = 20, p = 0.8")
    (is (close (dbinom 6 20 0.8) 1.664729e-06) "x = 6, n = 20, p = 0.8")
    (is (close (dbinom 14 20 0.2) 1.664729e-06) "x = 14, n = 20, p = 0.2")
    (is (close (dbinom 370 1000 0.4) 0.003938405) "x = 370, n = 1000, p = 0.4")
    (is (close (dbinom 420 870 0.45) 0.004132344) "x = 420, n = 870, p = 0.45")
    ))
