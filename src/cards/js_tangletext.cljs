(ns cards.js-tangletext
  (:require
    [rum.core :as rum]
    [rcljswidgets.core :as core]
    [rcljswidgets.tangle :refer [tangle-numeric]]
    [rcljswidgets.wrapped-react-tangle-js :refer [js-tangletext wrap-js-tangletext]]
    [example.data :refer [bref* cref* d* update-b* update-c* update-d*]]
    [clojure.string :refer [replace]]
    [pubsub.feeds :refer [create-feed ->Topic]]
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [defcard-doc defcard deftest]]
    ))


(defcard
  " # Using a `react-tangle` directly
  ## - a javascript reactjs library

  Load it after the `cljs` javascript so we know React is loaded
  ```html
      <script src=\"/js/react-tangle.js\" type=\"text/javascript\"></script>
  ```
  Then call `React.createElement` on the component
  ```clj
  (rum/defc js-tangletext []
       (.createElement js/React js/TangleText
                       #js {:value 6 :min 0 :max 10 :step 0.1 :onChange #(.log js/console %1)}))


  (js-tangletext)
  ```
  resulting in:"
  (js-tangletext)
  #_(wrap-js-tangletext cref* update-c*
                      {:minimum 0 :maximum 10 :step 0.1})
  )
