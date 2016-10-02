(ns tests.dbinom
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [alg.binom :refer [dbinom pbinom qbinom]]
    [rcljswidgets.utils :refer [fabs close? all-close?]]
    ))

(deftest
  loaders-algorithm-for-dbinom
  (testing
    "agreement with R dbinom"
    (is (close? (dbinom 1 1 1) 1) "x = 1, n = 1, p = 1")
    (is (close? (dbinom 0 1 0) 1) "x = 0, n = 1, p = 0")
    (is (close? (dbinom 0 1000 0) 1) "x = 0, n = 1000, p = 0")
    (is (close? (dbinom 1000 1000 1) 1) "x = 0, n = 1000, p = 1")
    (is (close? (dbinom 0 20 0.8) 1.048576e-14) "x = 0, n = 20, p = 0.8")
    (is (close? (dbinom 20 20 0.8) 0.01152922) "x = 20, n = 20, p = 0.8")
    (is (close? (dbinom 1 20 0.8) 8.388608e-13) "x = 1, n = 20, p = 0.8")
    (is (close? (dbinom 6 20 0.8) 1.664729e-06) "x = 6, n = 20, p = 0.8")
    (is (close? (dbinom 14 20 0.2) 1.664729e-06) "x = 14, n = 20, p = 0.2")
    (is (close? (dbinom 370 1000 0.4) 0.003938405) "x = 370, n = 1000, p = 0.4")
    (is (close? (dbinom 420 870 0.45) 0.004132344) "x = 420, n = 870, p = 0.45")
    ))

(deftest
  with-vector-quantiles
  (testing "pairs"
    (is (all-close? (dbinom [0 20 1 6] 20 0.8) [1.048576e-14 0.01152922 8.388608e-13 1.664729e-06]))))

(deftest
  check-pbinom
  (testing "pbinom agrees with R pbinom"
    (is (close? (pbinom 2 10 0.3) 0.3827828))
    (is (close? (pbinom 2 10 0.3 false) 0.6172172))
    (is (all-close? (pbinom [2 5 8], 10, 0.3) [0.3827828 0.9526510 0.9998563]))))

(deftest
  check-qbinom
  (testing "qbinom agrees with R qbinom"
    (is (close? (qbinom 0.34 300 0.7 true) 207))
    (is (all-close? (qbinom 0.34 300 [0.7 0.8 0.9] true) [207 237 268]))
    (is (all-close? (qbinom 0.34 300 [0.7 0.8 0.9] false) [213 243 272]))
    (is (all-close? (qbinom [0.2 0.34] 300 [0.7 0.8 0.9] false) [217 243 274]))
    ))