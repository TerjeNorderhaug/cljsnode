(defproject cljsnode "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.0"]
                 [enfocus "2.1.1"]
                 [kioo "0.4.1-SNAPSHOT"]
                 ]

  :npm {:dependencies [[express "4.13.3"]
                       [xmlhttprequest "*"]
                       [xmldom "0.1.19"]
                       [source-map-support "*"]
                       [react "*"]]
        :package {}
        :root :target-path}

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-npm "0.6.1"]
            [lein-figwheel "0.3.8-SNAPSHOT"]]

  :min-lein-version "2.1.2"

  :hooks [leiningen.cljsbuild]

  :aliases {"start" ["npm" "start"]}

  :main "server/lib/polyfill/boot.js"

  :source-paths ["src/cljs"]

  :clean-targets ^{:protect false} [[:cljsbuild :builds :server :compiler :output-to]
                                    [:cljsbuild :builds :app :compiler :output-to]
                                    :target-path :compile-path]

  :figwheel {:http-server-root "public"
             :css-dirs ["resources/public/css"]
             :server-logfile "logs/figwheel.log"}

  :cljsbuild {:builds
              {:app
               {:source-paths ["src/browser" "src/cljs"]
                :compiler {:output-to "resources/public/js/out/app.js"
                           :output-dir "resources/public/js/out/lib"
                           :asset-path "js/out/lib"
                           :main app.start
                           :optimizations :none}}

 ;; ## Eliminate /lib from bnoth app and server - if it still works...

               :server
               {:source-paths ["src/node" "src/cljs"]
                :compiler {:target :nodejs
                           :output-to "target/server/main.js"
                           :output-dir "target/server/lib"
                           :asset-path "server/lib"
                           :main server.core
                           :optimizations :none}
                :notify-command ["bin/dependency-patch.sh"]}}}

  :profiles {:dev
             {:cljsbuild
              {:builds
               {:app
                {:compiler {:pretty-print true}
                 :figwheel true}
                :server
                {:compiler {:pretty-print true}
                 :figwheel {:heads-up-display false}}}}
              :npm {:dependencies [[ws "*"]]}}

             :prod
             {:env {:production true}
              :cljsbuild
              {:builds
               {:server
                {:compiler {:optimizations :none
                            :pretty-print false}}
                :app
                {:compiler {:output-dir "target/app/out"
                            :optimizations :advanced
                            :pretty-print false}}}}}})
