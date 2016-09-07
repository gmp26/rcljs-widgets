(ns rWrappers.tangleRectangle
  (:require [rum.core :as rum]
            [rcljswidgets.rectangles :refer [square rect]]))

(defn ^:export renderSquare [el fill width]
  "render a square on an html element: exported for r"
  (rum/mount (square fill width) el))

(defn ^:export renderRectangle [el fill width height]
  "render a rectangle on an html element: exported for r"
  (rum/mount (rect fill width height) el))

;; Do I need a dummy main function that refers to things I need in the library? No.
(defn ^:export main []  "dummy")