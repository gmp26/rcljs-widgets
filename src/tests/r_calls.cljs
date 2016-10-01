(ns tests.r-calls
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [rcljswidgets.r-call :refer [cycled-parameters]]
    ))

(deftest
  recycling-parameters
  (testing "recycle"
    (testing "scalars"
      (is (= (cycled-parameters '(1 2 3)) '(1 2 3)))
      )
    (testing "vectors"
      (is (= (cycled-parameters '([1] [2] [3])) '([1] [2] [3])))
      (is (= (cycled-parameters '([1 2 3] [2] [3])) '([1 2 3] [2 2 2] [3 3 3])))
      )
    ))
