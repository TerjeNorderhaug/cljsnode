(defproject cljsnode "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.227"]
                 [org.clojure/core.async "0.2.385"]
                 [reagent "0.6.0-rc"]
                 [secretary "1.2.3"]
                 [kioo "0.5.0-SNAPSHOT" :exclusions [cljsjs/react]]]

  :npm {:dependencies [[express "4.14.0"]
                       [xmlhttprequest "*"]
                       [xmldom "0.1.19"]
                       [source-map-support "*"]
                       [react "15.2.1"]]
        :root :root}

  :plugins [[lein-cljsbuild "1.1.2"]
            [lein-npm "0.6.2"]]

  :min-lein-version "2.1.3"

  :hooks [leiningen.cljsbuild]

  :aliases {"start" ["npm" "start"]}

  :main "main.js"

  :source-paths ["src/cljs"]

  :clean-targets ^{:protect false}
  [[:cljsbuild :builds :server :compiler :output-to]
   [:cljsbuild :builds :app :compiler :output-dir]
   "node_modules"
   :target-path :compile-path]

  :figwheel {:http-server-root "public"
             :load-all-builds false
             :css-dirs ["resources/public/css"]
             :server-logfile "logs/figwheel.log"}

  :cljsbuild {:builds
              {:app
               {:source-paths ["src/browser" "src/cljs"]
                :compiler {:output-to "resources/public/js/out/app.js"
                           :output-dir "resources/public/js/out"
                           :asset-path "js/out"
                           :main app.start
                           :optimizations :none}}

               :server
               {:source-paths ["src/node" "src/cljs"]
                :compiler {:target :nodejs
                           :output-to "main.js"
                           :output-dir "target"
                           :main server.core
                           :optimizations :none}
                }}}

  :profiles {:dev
             {:plugins
              [[lein-figwheel "0.5.4-7"]]
              :cljsbuild
              {:builds
               {:app
                {:compiler {:pretty-print true
                            :source-map true}
                 :figwheel true}
                :server
                {:compiler {:pretty-print true
                            :source-map true}
                 :figwheel {:heads-up-display false}}}}
              :npm {:dependencies [[ws "*"]]}}

             :prod
             {:env {:production true}
              :cljsbuild
              {:builds
               {:server
                {:compiler {:optimizations :simple
                            :foreign-libs [{:file "src/node/polyfill/simple.js"
                                            :provides ["polyfill.simple"]}]
                            :pretty-print false}}
                :app
                {:compiler {:output-dir "target/app/out"
                            :optimizations :advanced
                            :pretty-print false}}}}}})
