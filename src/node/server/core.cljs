(ns server.core
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop alt!]])
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :as async :refer [chan close! timeout put!]]
            [shared.jokes :as jokes :refer [fresh-jokes]]
            [server.compat]
            [shared.views :refer [html5 jokes-page]]
            [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def express (nodejs/require "express"))

(defn handler [jokes-chan req res]
  (if (= "https" (aget (.-headers req) "x-forwarded-proto"))
    (.redirect res (str "http://" (.get req "Host") (.-url req)))
    (go
      (.set res "Content-Type" "text/html")
      (.send res (html5 (jokes-page (<! jokes-chan)))))))

(defn server [handler port success]
  (let [jokes (fresh-jokes 5 2)]
    (doto (express)
      (.get "/" #(handler jokes %1 %2))
      (.get "/js/out/app.js"
            (fn [req res]
              (.sendFile res "cljsbuild-main.js"
                         (clj->js {:root "../target"}))))
      (.use "/js/out/" ;; restrict to js? and devmode only!
            (.static express "../target/cljsbuild-compiler-1/goog"))
      (.use (.static express "../resources/public"))
      (.listen port success))))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server handler port
            #(println (str "Server running at http://127.0.0.1:" port "/")))))

(set! *main-cli-fn* -main)
