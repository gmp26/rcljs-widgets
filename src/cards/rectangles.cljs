(ns cards.rectangles
  (:require
    [rum.core :as rum]
    [cljs.test :as t]
    [rcljswidgets.rectangles :as rects]
    )
  (:require-macros
    [devcards.core :as dc :refer [defcard-doc defcard deftest]]
    ))


(defcard-doc
  "#Rectangle tests
  Visible container proxies to test resize behaviour in various contexts."
  )

(defcard
  "```
(rects/square \"rgb(150,170,200)\" \"100px\")
```"
  (rects/square "rgb(150,170,200)" "100px"))

(defcard
  "```
(rects/rect \"rgb(150,200,170)\" \"60%\" \"30%\")
```"
  (rects/rect "rgb(150,200,170)" "60%" "30%"))