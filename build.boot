(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies    '[[org.clojure/clojurescript "1.9.854"]

                    [re-frame "0.9.4" :exclusions
                     [org.clojure/clojurescript]]
                    [reagent "0.7.0"]

                    [wrest-xml "0.1.1"]

                    ;; Dev/Release build code-gen
                    [hiccup "1.0.5"]
                    [garden "1.3.2"]

                    ;; Why do I need these?? devcards 0.2.3 is broken...
                    [cljsjs/react "15.3.1-0"]
                    [cljsjs/react-dom "15.3.1-1"]

                    ;; Boot dev environment
                    [ring "1.6.2"]
                    [compojure "1.6.0"]
                    [devcards "0.2.3" :exclusions
                     [org.clojure/clojurescript]]
                    [day8/re-frame-tracer "0.1.1-SNAPSHOT"]
                    [org.clojars.stumitchell/clairvoyant "0.2.0"]
                    [radicalzephyr/ring.middleware.logger "0.6.0"
                     :exclusions [[org.slf4j/slf4j-log4j12 :extension "jar"]]]
                    [adzerk/boot-cljs              "2.1.1"]
                    [adzerk/boot-cljs-repl         "0.3.3"]
                    [adzerk/boot-reload            "0.5.1"]
                    [pandeiro/boot-http            "0.8.3"]
                    [org.martinklepsch/boot-garden "1.3.2-0"]
                    [samestep/boot-refresh "0.1.0"]
                    [powerlaces/boot-cljs-devtools "0.2.0"]

                    ;; cljs-repl
                    [com.cemerick/piggieback "0.2.2"
                     :exclusions [org.clojure/clojurescript]]
                    [weasel                  "0.7.0"
                     :exclusions [org.clojure/clojurescript]]
                    [org.clojure/tools.nrepl "0.2.13"]
                    [binaryage/devtools "0.9.2"]

                    [crisptrutski/boot-cljs-test "0.3.4"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[powerlaces.boot-cljs-devtools :refer [cljs-devtools]]
 '[org.martinklepsch.boot-garden :refer [garden]]
 '[samestep.boot-refresh :refer [refresh]])

;; Pipeline configuration tasks

(deftask production
  "Configure a task pipeline with production options"
  []
  (task-options! cljs {:ids #{"public/js/main"}
                       :optimizations :advanced})
  identity)

(deftask development
  "Configure a task pipeline with development options"
  []
  (task-options! cljs {:optimizations :none
                       :source-map true
                       :compiler-options {:devcards true
                                          :closure-defines {"clairvoyant.core.devmode" true}}}
                 reload {:asset-path "public"}
                 serve {:handler 'atreus.layout/handler
                        :port 8080})
  identity)

(deftask testing
  "Configure a task pipeline with testing options"
  []
  (set-env! :source-paths #(conj % "test"))
  identity)


;; Low-level do-the-stuff tasks

(deftask build
  "Compile clojurescript->js and garden->css"
  []
  (comp (notify :audible true)
        (garden :styles-var 'atreus.styles/screen
                :output-to "public/css/app.css")
        (cljs)))

(deftask run
  "Run a local server with automatic code rebuilding"
  []
  (comp (serve)
        (watch)
        (cljs-repl)
        (cljs-devtools)
        (reload)
        (build)))


;; High-level daily usage tasks

(deftask release
  "Create a time-stamped zip file for deploying"
  []
  (require 'atreus.task.index
           'atreus.task.time)
  (comp (production)
        ((resolve 'atreus.task.index/write-index))
        (build)
        (sift :include #{#"\.out" #"\.cljs.edn$"} :invert true)
        (zip :file (str "atreus-layout-" ((resolve 'atreus.task.time/now)) ".zip"))
        (target :dir #{"target"})))

(deftask dev
  "Run local development"
  []
  (comp (development)
        (testing)
        (run)
        (refresh)))

;;; Silence name collision warning
(ns-unmap 'boot.user 'test)
(deftask test
  "Run the clojurescript tests"
  []
  (comp (testing)
        (test-cljs :js-env :phantom
                   :exit?  true)))

(deftask auto-test
  "Setup an automatic test runner"
  []
  (comp (testing)
        (watch)
        (test-cljs :js-env :phantom
                   :namespaces #{"atreus.devcards"
                                 #"atreus.*-test"})))
