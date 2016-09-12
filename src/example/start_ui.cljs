(ns example.start-ui
  (:require
    [devcards.core]
    [cards.shiny-dimensions]
    [cards.rectangles]
    [cards.tangles]
    [cards.js-tangletext]
    [cards.js-wrapped-tangletext]
    [tests.check-tests]
    [cards.app-state]
    [cards.funnel]
    ))

(defn ^:export main []
  (devcards.core/start-devcard-ui!))