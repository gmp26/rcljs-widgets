(defproject rcljs-widgets "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://github.com/gmp26/rcljs-widgets"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [devcards "0.2.1-7"]
                 [rum "0.10.4"]
                 [cljs-css-modules "0.1.1"]
                 [figwheel-sidecar "0.5.4-6"]
                 [pubsub "0.1.0-SNAPSHOT"]
                 ]

  :plugins [[lein-cljsbuild "1.1.3" :exclusions [org.clojure/clojure]]]

  ;:git-dependencies [["https://github.com/gmp26/pubsub.git"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :source-paths ["src" ".lein-git-deps/pubsub/src"]

  :cljsbuild {
              :builds [{:id           "devcards"
                        :source-paths ["src"]
                        :figwheel     {:devcards true}      ;; <- note this
                        :compiler     {;;:main                 "rcljs-widgets.devcards" ;; <- and this!
                                       :main                 "example.start-ui" ;; <- and this!
                                       :asset-path           "js/compiled/devcards_out"
                                       :output-to            "resources/public/js/compiled/rcljs_widgets_devcards.js"
                                       :output-dir           "resources/public/js/compiled/devcards_out"
                                       :source-map-timestamp true
                                       }}
                       {:id           "dev"
                        :source-paths ["src"]
                        :figwheel     true
                        :compiler     {:main                 "rcljs-widgets.core"
                                       :asset-path           "js/compiled/out"
                                       :output-to            "resources/public/js/compiled/rcljs_widgets.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :source-map-timestamp true}}
                       {:id           "prod"
                        :source-paths ["src"]
                        :compiler     {:main          "rcljs-widgets.core"
                                       :asset-path    "js/compiled/out"
                                       :output-to     "resources/public/js/compiled/rcljs_widgets.js"
                                       :externs       ["externs/exported.js"]
                                       :optimizations :whitespace}}]}

  :figwheel {:css-dirs ["resources/public/css"]})
