(defproject rcljs-widgets "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://github.com/gmp26/rcljs-widgets"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [cljsjs/react-dom "0.14.3-1"]              ; see https://github.com/bhauman/devcards/issues/106 for why
                 [devcards "0.2.1-7"]
                 [rum "0.10.4"]
                 [cljs-css-modules "0.1.1"]
                 [figwheel-sidecar "0.5.4-6"]
                 [pubsub "0.2.1"]
                 ]

  :plugins [[lein-cljsbuild "1.1.3" :exclusions [org.clojure/clojure]]]

  ;:git-dependencies [["https://github.com/gmp26/pubsub.git"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :source-paths ["src" "rlib"]

  :cljsbuild {
              :builds [{:id           "devcards"
                        :source-paths ["src"]
                        :figwheel     {:devcards true}      ;; <- note this
                        :compiler     {;;:main                 "rcljswidgets.devcards" ;; <- and this!
                                       :main                 "example.start-ui" ;; <- and this!
                                       :asset-path           "js/compiled/devcards_out"
                                       :output-to            "resources/public/js/compiled/rcljswidgets_devcards.js"
                                       :output-dir           "resources/public/js/compiled/devcards_out"
                                       :source-map-timestamp true
                                       :optimizations        :none
                                       }}
                       {:id           "rlib"
                        :source-paths ["rlib" "src"]
                        :compiler     {:main                 "rWrappers.tangleRectangle"
                                       :asset-path           "js/compiled/rWrappers-out"
                                       :output-to            "resources/public/js/compiled/rWrappers.js"
                                       :output-dir           "resources/public/js/compiled/rWrappers-out"
                                       :source-map-timestamp true
                                       :optimizations        :whitespace}}
                       {:id           "shinydev"
                        :source-paths ["src"]
                        :compiler     {:asset-path           "js/compiled/shiny-tangle-out"
                                       :output-to            "resources/public/js/compiled/shiny-tangle.js"
                                       :output-dir           "resources/public/js/compiled/shiny-tangle-out"
                                       :source-map-timestamp true
                                       :optimizations        :none}}
                       {:id           "dev"
                        :source-paths ["src"]
                        :figwheel     true
                        :compiler     {:main                 "rcljswidgets.core"
                                       :asset-path           "js/compiled/out"
                                       :output-to            "resources/public/js/compiled/rcljswidgets.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :source-map-timestamp true
                                       ;; To console.log CLJS data-structures make sure you enable devtools in Chrome
                                       ;; https://github.com/binaryage/cljs-devtools
                                       :preloads             [devtools.preload]}}
                       {:id           "prod"
                        :source-paths ["src"]
                        :compiler     {:main          "rcljswidgets.core"
                                       :asset-path    "js/compiled/out"
                                       :output-to     "resources/public/js/compiled/rcljswidgets.js"
                                       :externs       ["externs/exported.js"]
                                       :optimizations :whitespace}}]}

  :figwheel {:css-dirs ["resources/public/css"]})
