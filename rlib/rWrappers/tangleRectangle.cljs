(ns rWrappers.tangleRectangle
  (:require [rum.core :as rum]
            [pubsub.feeds :refer [->Topic create-feed subscribe publish]]
            [clojure.string :refer [replace]]
            [rcljswidgets.tangle :refer [tangle-numeric]]
            [rcljswidgets.rectangles :refer [square rect]]))

(defn ^:export renderSquare [el fill width]
  "render a square on an html element: exported for r"
  (rum/mount (square fill width) el))

(defn ^:export renderRectangle [el fill width height]
  "render a rectangle on an html element: exported for r"
  (rum/mount (rect fill width height) el))

(defonce *feed (create-feed))
(defonce *width (->Topic :width *feed))
(defonce *height (->Topic :height *feed))
(defonce *dimensions (atom {:width 100 :height 200}))

(def dim-feeds {:width *width :height *height})

;(defonce *width (->Topic :width *feed))
;(defonce *height (->Topic :height *feed))

(defn adjuster [key value]
  "render a tangle to control rectangle width"
  (swap! *dimensions assoc key value)

  ;; set
  (subscribe (key dim-feeds) #(swap! *dimensions assoc key %2))
  (tangle-numeric (rum/cursor-in *dimensions [key])
                  (key dim-feeds)
                  {:minimum        0 :maximum 400 :step 5
                   :pixel-distance 5
                   :format         #(str (js/Math.round %) "px")
                   :parse          #(js/parseInt (replace (str %) #"px" ""))}))

(defn ^:export renderWidthTangle [el value]
  "render a tangle to control rectangle width"
  (rum/mount (adjuster :width value) el))

(defn ^:export renderHeightTangle [el value]
  "render a tangle to control rectangle width"
  (rum/mount (adjuster :height value) el))


(defn getWidth [] (:width @*dimensions))
(defn setWidth [w] (assoc *dimensions :width w))

(defn getHeight [] (:height @*dimensions))
(defn setHeight [h] (assoc *dimensions :height h))

;; Do I need a dummy main function in the library?
(defn ^:export main [] "dummy")