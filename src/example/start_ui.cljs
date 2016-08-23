(ns example.start-ui
  (:require
    [devcards.core]
    [cards.shapes]
    [cards.tangles]
    ;[rcljs-widgets.reactjs-devcards]
    ))

;; The main function here is actually used in a documentation
;; generator that I'm experimenting with. This is not needed
;; with a standard Devcards setup!!

(defn ^:export main []
  (devcards.core/start-devcard-ui!))