(ns example.start-ui
  (:require
    [devcards.core]
    [cards.rectangles]
    [cards.tangles]
    [cards.js-tangletext]
    [cards.js-wrapped-tangletext]
    [tests.check-tests]
    [cards.app-state]
    ))

(defn ^:export main []
  (devcards.core/start-devcard-ui!))