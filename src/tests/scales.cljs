(ns tests.scales
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [svg.scales :refer [->Identity ->Linear i->o o->i nice-linear ticks]]
    ))

(deftest
  scales
  (testing
    (let [id (->Identity [0 100] 10)]
      (is (= ((i->o id) 50) 50) "identity scale")
      (is (= ((o->i id) 30) 30) "inverse identity scale"))

    (let [linear (->Linear [0 100] [0 200] 10)]
      (is (= ((i->o linear) 50) 100)  "linear scale")
      (is (= ((o->i linear) 100) 50) "inverse linear scale"))

    (let [linear (->Linear [0 100] [-200 0] 10)]
      (is (= ((i->o linear) 50) -100)  "linear scale")
      (is (= ((o->i linear) -100) 50) "inverse linear scale"))

    (let [linear (->Linear [100 200] [-200 -100] 10)]
      (is (= ((i->o linear) 150) -150)  "linear scale")
      (is (= ((o->i linear) -150) 150) "inverse linear scale"))

    (is (= (ticks (nice-linear [2.3 12.3] [0 100] 10))
           (range 2 14)) "scales have sensible ticks")

    (is (= (ticks (nice-linear [12.5 2.3] [0 100] 10))
           (range 13 1 -1)) "nice reverse scales have sensible ticks")

    (is (= (ticks (nice-linear [-10 10] [0 100] 10)) [-10 -8 -6 -4 -2 0 2 4 6 8 10]))
    ))



