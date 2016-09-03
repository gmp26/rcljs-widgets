(ns cards.app-state
  (:require
    [rum.core :as rum]
    [rcljs-widgets.core :as core]
    [rcljs-widgets.tangle :refer [tangle-numeric inline-tangle]]
    [rcljs-widgets.wrapped-react-tangle-js :refer [js-tangletext wrap-js-tangletext]]
    [example.data :refer [bref* cref* d* update-b* update-c* update-d*]]
    [clojure.string :refer [replace]]
    [pubsub.feeds :refer [create-feed ->Topic]]
    [cljs.test :refer-macros [is testing]]
    [devcards.core :refer-macros [defcard-doc defcard deftest]]
    ))

(defcard
  "
 # App state

 Rum allows you to store app state however you want. If you use a deeply nested structure,
 there are a few ways for a component to access parts of the state and be updated when the state changes. See for example
[cursor paths](https://github.com/tonsky/rum), [derived atoms](https://github.com/tonsky/rum), and [derivatives](https://github.com/martinklepsch/derivatives).\n\n  Here `db*` is an atom containing `{:a {:b 7 :c 9}}` and we use `rum/cursor-in` to define cursors pointing to the values at `:b` and `:c`.\n\n  ### Updating the value reference\n  The parse function we supply (by default `#(js/parseInt %)`) should convert the input string\n  to a numeric value. Changes to the number will be returned on the topic feed we give as second parameter.\n\n  We subscribe a handler to the topic feed that has the job of updating the value reference with the new value.\n
```clj
(ns example.data\n  (:require [rum.core :as rum]\n            [pubsub.feeds :refer [create-feed ->Topic subscribe]]))\n\n(defonce db* (atom {:a {:b 7 :c 9 :d 11}}))\n(defonce bref* (rum/cursor-in db* [:a :b]))                 ; allow rum component to watch :b\n(defonce cref* (rum/cursor-in db* [:a :c]))                 ; allow rum component to watch :b\n(defonce d* (atom 11))\n\n(defonce feed* (create-feed))                               ; just the one feed, carrying\n(defonce update-b* (->Topic :bref feed*))                   ; an 'update :b' topic\n(defonce update-c* (->Topic :cref feed*))                   ; and an 'update :c' topic\n(defonce update-d* (->Topic :dref feed*))                   ; and an 'update d*' topic\n\n(subscribe update-b*\n           (fn [_ value]\n             (println \"update b \" value)\n             (swap! db* update-in [:a :b] (fn [_] (js/Math.round value)))))\n\n(subscribe update-c*\n           (fn [_ value]\n             (println \"update c \" value)\n             (swap! db* update-in [:a :c] (fn [_] value))))\n\n(subscribe update-d*\n           (fn [_ value]\n             (println \"update d \" value)\n             (reset! d* value)))
```")