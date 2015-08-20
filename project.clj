(defproject cljsnode "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.0"]]

  :npm {:dependencies [[express "4.13.3"]
                       [xmlhttprequest "*"]
                       [source-map-support "*"]
                       [react "*"]]
        :root :target-path}

  :plugins [[org.bodil/lein-noderepl "0.1.11"]
            [lein-cljsbuild "1.0.6"]
            [lein-npm "0.6.1"]]

  :min-lein-version "2.1.2"

  :hooks [leiningen.cljsbuild]

  :main "out/server.js"

  :source-paths ["src/cljs/shared"]

  :clean-targets ^{:protect false}
                 [[:cljsbuild :builds :server :compiler :output-to]
                  [:cljsbuild :builds :app :compiler :output-to]
                  :target-path :compile-path]

  :cljsbuild {:builds
              {:server
               {:source-paths ["src/node"]
                :compiler {:target :nodejs
                           :output-to "target/out/server.js"
                           :jar true
                           :optimizations :simple
                           :pretty-print true}}
               :app
               {:source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/out/app.js"
                           :output-dir "resources/public/js/out/lib"
                           :asset-path "js/out/lib"
                           :main app.core
                           :optimizations :none
                           :pretty-print true}}}}

  :profiles {:dev
             {}
             :prod
             {:env {:production true}
              :cljsbuild
              {:builds
               {:app
                {:compiler {:output-dir "target/out/prod"
                            :optimizations :advanced
                            :pretty-print false}}}}}})
