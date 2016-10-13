(ns tests.r-calls
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [alg.binom :refer [dbinom1 dbinom]]
    [rcljswidgets.r-call :refer [n-cycle cycled-parameters cycled-apply]]))

(deftest
  cycling-parameters
  (testing "single up to n"
    (is (= 1 1))
    (is (= (n-cycle 4 1) [1 1 1 1]))
    (is (= (n-cycle 4 [1]) [1 1 1 1]))
    (is (= (n-cycle 4 [1 2]) [1 2 1 2]))
    (is (= (n-cycle 4 [1 2 3]) [1 2 3 1]))
    (is (= (n-cycle 4 [1 2 3 4]) [1 2 3 4])))

  (testing "cycle many"
    (testing "scalars"
      (is (= (cycled-parameters 1 2 3) '((1) (2) (3)))))
    (testing "vectors"
      (is (= (cycled-parameters [1] [2] [3]) '([1] [2] [3])))
      (is (= (cycled-parameters [1 2 3] [2] [3]) '([1 2 3] [2 2 2] [3 3 3]))))
    (testing "apply"
      (is (= (cycled-apply dbinom1 1 1 1) 1))
      (is (= (cycled-apply dbinom1 [1 2] 1 1) [1 0]))
      )))
