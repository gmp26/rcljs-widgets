(ns tests.check-tests
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    ))


(deftest
  just-checking
  (testing
    "success and failure"
    (is true "success")
    "failure"
    (is false "failure")))