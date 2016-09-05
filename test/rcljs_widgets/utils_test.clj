(ns rcljs-widgets.utils-test
  (:require [clojure.test :refer :all]
            [rcljs-widgets.utils :refer [clamp]]))

(deftest clamping
  (testing "clamping"
    (testing "good params"
      (is (= 3 (clamp 1 3 10)))
      (is (= -5 (clamp -20 -5 0)))
      (is (= 0 (clamp Integer/MIN_VALUE 0 10)))
      (is (= 0 (clamp Integer/MIN_VALUE 0 Integer/MAX_VALUE))))
    (testing "out of range"
      (is (= 1 (clamp 1 -1 10)))
      (is (= 10 (clamp 1 20 10)))
      (is (= -5 (clamp -5 -10 -2))))
    (testing "not a number"
      (is (= 5 (clamp 0 "a" 10))))
    (testing "default when not a number"
      (is (= 3 (clamp 0 "a" 10 3))))))
