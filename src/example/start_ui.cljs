(ns example.start-ui
  (:require
    [devcards.core]
    [cards.shiny-dimensions]
    [cards.rectangles]
    [cards.tangles]
    [cards.js-tangletext]
    [cards.js-wrapped-tangletext]
    [cards.app-state]
    [cards.funnel]
    [cards.markers]
    [cards.margin-convention]
    ;[tests.check-tests]
    [tests.utils]
    [tests.scales]
    [tests.dbinom]
    [tests.r-calls]
    [tests.binom-limits]
    ))

(defn ^:export main []
  (devcards.core/start-devcard-ui!))