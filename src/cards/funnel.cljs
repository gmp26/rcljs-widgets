(ns cards.funnel
  (:require [pubsub.feeds :refer [create-feed ->Topic]]
            [cljs.test :refer-macros [is testing]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [svg.space :refer [space]]
            [rcljswidgets.funnel-plots :refer [funnel non-negative-int? valid-record data-space derived-data]]
            [tests.funnel-data :refer [CABG]]
            [alg.binom :as alg]))

(enable-console-print!)


(def margin {:top 20 :right 20 :bottom 20 :left 20})
(def padding {:top 60 :right 30 :bottom 40 :left 70})
(def outer {:width 700 :height 600})

(defcard
  "a funnel"
  (funnel (data-space outer margin padding
                      (derived-data CABG)
                      [0.001 0.025])))

(deftest
  non-negative
  (testing "non-negative-int? is sane"
    (is (false? (non-negative-int? "a")) "\"a\"")
    (is (false? (non-negative-int? "1")) "\"1\"")
    (is (false? (non-negative-int? -1)) "-1")
    (is (true? (non-negative-int? 0)) "0")
    (is (false? (non-negative-int? nil))) nil))

(deftest
  pre-checks
  (let [h (CABG 0)]
    (testing
      "data pre-checks are sane"
      (is (valid-record h) "record 0 is OK"))
    (testing
      (is (not (valid-record (assoc h :Deaths nil))) "but not with nil Deaths"))
    (testing (is (valid-record (assoc h :Deaths 0)) "zero Deaths are fine"))
    (testing (is (valid-record (assoc h :Deaths nil :Survivors 3)) "nil Deaths but Survivors given is OK"))
    (testing
      "need consistency with case count"
      (is (not (valid-record (assoc h :Deaths 8 :Survivors 3))))
      (is (valid-record (assoc h :Deaths 8 :Survivors 3 :Cases 11))))
    ))


(defcard
  "zero Cases count and nil Survival count"
  (funnel (data-space outer margin padding
                      (derived-data (assoc-in CABG [0 :Cases] 0))
                      [0.001 0.025])))