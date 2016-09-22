(ns cards.markers
  (:require [rum.core :as rum]
            [devcards.core :refer-macros [defcard-doc defcard deftest]]
            [svg.markers :refer [dot odot square osquare diamond odiamond cross plus]]
            ))


(defcard
  "#Markers
```
(rum/defc markers []\n     [:svg {:width 100 :height 100}\n      (dot 3.5 10 50)\n      (odot 3 20 50)\n      (square 3.5 30 50)\n      (osquare 3 40 50)\n      (diamond 3 50 50)\n      (odiamond 2.5 60 50)\n      (cross 4 70 50)\n      (plus 3.5 80 50)\n      ])
```"
  ((rum/defc markers []
     [:svg {:width 100 :height 100}
      (dot 3.5 10 50)
      (odot 3 20 50)
      (square 3.5 30 50)
      (osquare 3 40 50)
      (diamond 3 50 50)
      (odiamond 2.5 60 50)
      (cross 4 70 50)
      (plus 3.5 80 50)
      ])))