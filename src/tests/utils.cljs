(ns tests.utils
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [rcljswidgets.utils :refer [clamp fabs epsilon close0? close? all-close? r-wrap r-unwrap]]
    ))

(deftest
  clamping
  (testing "clamping"
    (testing "good params"
      (is (= 3 (clamp 1 3 10)))
      (is (= -5 (clamp -20 -5 0)))
      (is (= 0 (clamp (.-MIN_SAFE_INTEGER js/Number.) 0 10)))
      (is (= 0 (clamp (.-MIN_SAFE_INTEGER js/Number.) 0 (.-MAX_SAFE_INTEGER js/Number)))))
    (testing "out of range"
      (is (= 1 (clamp 1 -1 10)))
      (is (= 10 (clamp 1 20 10)))
      (is (= -5 (clamp -5 -10 -2))))
    (testing "not a number"
      (is (= 5 (clamp 0 "a" 10))))
    (testing "default when not a number"
      (is (= 3 (clamp 0 "a" 10 3))))))

(deftest
  floating-point
  (testing "proximity"
    (is (not (close0? epsilon)))
    (is (close0? (* epsilon -0.9999)))
    (is (close? 3.14159 (.-PI js/Math)))
    (is (all-close? [1 2 3 (/ 1 2)] [1 2 3 0.5]))))

(deftest
  R-vector-value
  (testing "wrap value"
    (is (= [2] (r-wrap 2)))
    (is (= [2] (r-wrap [2])))
    (is (= '(2) (r-wrap '(2))))
    (is (= [1 2 3] (r-wrap [1 2 3])))
    (is (seq? (r-wrap '(2))))
    (is (seq? (r-wrap '(1 2 3))))
    (is (vector? (r-wrap [2])))
    )
  (testing "non-numerics"
    (is (= [nil] (r-wrap nil)))
    (is (= ["a"] (r-wrap "a")))
    )
  (testing "unwrap value"
    (is (= 2 (r-unwrap [2])))
    (is (= 2 (r-unwrap '(2))))
    (is (= [1 2] (r-unwrap [1 2])))))


(comment
  (defn close0? [a]
    "true if |a| < epsilon"
    (< (fabs (- a 0)) epsilon))

  (defn close? [a b]
    "true if |a - b| < epsilon"
    (if (close0? b)
      (close0? a)
      (close0? (fabs (dec (/ a b))))))

  (defn all-close? [s1 s2]
    "true if floating point values in s1 are close to floating point values in s2"
    (every? true? (map close? s1 s2)))

  (defn r-wrap [x]
    "x as a vector or sequence if it isn't one already"
    (if (or (vector? x) (seq? x)) x [x]))

  (defn r-unwrap [x]
    "if x is a vector or sequence with 1 value, return that value, else return x unchanged"
    (if (= (rest x) ()) (first x) x)))
