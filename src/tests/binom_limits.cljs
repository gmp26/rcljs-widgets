(ns tests.binom-limits
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [alg.binom-limits :refer [qbinom-interp]]
    [rcljswidgets.utils :refer [fabs close? all-close?]]
    ))
(deftest
  upper-lower-limit-calculation
  (testing "scalar agreement with R"
    (is (close? (qbinom-interp 0.975 1 0.8 :upper) 0.125))
    (is (close? (qbinom-interp 0.975 1 0.8 :lower) 0.96875))
    (is (close? (qbinom-interp 0.975 30 0.8 :upper) 0.6653926))
    (is (close? (qbinom-interp 0.975 30 0.8 :lower) 0.9189948))
    (is (close? (qbinom-interp 0.975 500 0.94 :upper) 0.9193284))
    (is (close? (qbinom-interp 0.975 500 0.94 :lower) 0.9590222))
    )
  (testing "vector agreement with R"
    (is (all-close? (qbinom-interp 0.975 1 [0.9 0.91 0.92] :upper) [0.2500000 0.2777778 0.3125000]))
    (is (all-close? (qbinom-interp 0.975 1 [0.9 0.91 0.92] :lower) [0.9722222 0.9725275 0.9728261]))
    (is (all-close? (qbinom-interp 0.975 30 [0.9 0.91 0.92] :upper) [0.7984726 0.8099055 0.8265404]))
    (is (all-close? (qbinom-interp 0.975 30 [0.9 0.91 0.92] :lower) [0.9803418 0.9858884 0.9898332]))
    (is (all-close? (qbinom-interp 0.975 500 [0.9 0.91 0.92] :upper) [0.8739851 0.8851109 0.8964178]))
    (is (all-close? (qbinom-interp 0.975 500 [0.9 0.91 0.92] :lower) [0.9245525 0.9333339 0.9419426]))
    ))
