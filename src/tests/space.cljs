(ns tests.space
  (:require
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [deftest]]
    [svg.scales :refer [->Identity ->Linear i->o o->i]]
    ))

(deftest
  scales
  (testing
    (let [id (->Identity [0 100])]
      (is (= ((i->o id) 50) 50) "identity scale")
      (is (= ((o->i id) 30) 30) "inverse identity scale"))

    (let [linear (->Linear [0 100] [0 200])]
      (is (= ((i->o linear) 50) 100)  "linear scale")
      (is (= ((o->i linear) 100) 50) "inverse linear scale"))))
