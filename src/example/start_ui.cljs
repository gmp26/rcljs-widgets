(ns example.start-ui
  (:require
    [devcards.core]
    [cards.rectangles]
    [cards.tangles]
    ))

;; The main function here is actually used in a documentation
;; generator that I'm experimenting with. This is not needed
;; with a standard Devcards setup!!

(defn ^:export main []
  (devcards.core/start-devcard-ui!))