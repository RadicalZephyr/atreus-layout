(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies    (template
                   [[org.clojure/clojure ~(clojure-version)]
                    [org.clojure/clojurescript "1.10.238"]

                    [re-frame "0.10.5" :exclusions
                     [org.clojure/clojurescript]]
                    [reagent "0.7.0"]
                    [antizer "0.2.2"]


                    ;; Dev/Release build code-gen
                    [hiccup "1.0.5"]
                    [garden "1.3.5"]

                    ;; Why do I need these?? devcards 0.2.3 is broken...
                    [cljsjs/react "15.6.1-1"]
                    [cljsjs/react-dom "15.6.1-1"]

                    ;; Tracing
                    [day8.re-frame/re-frame-10x "0.3.3" :scope "test"]
                    [day8.re-frame/tracing "0.5.1" :scope "test"]

                    ;; Boot dev environment
                    [ring "1.6.3"]
                    [compojure "1.6.1"]
                    [devcards "0.2.4" :exclusions
                     [org.clojure/clojurescript]]
                    [radicalzephyr/ring.middleware.logger "0.6.0"
                     :exclusions [org.slf4j/slf4j-log4j12]]
                    [metosin/boot-alt-test "0.3.2"]
                    [adzerk/boot-cljs              "2.1.4"]
                    [adzerk/boot-cljs-repl         "0.3.3"]
                    [adzerk/boot-reload            "0.5.2"]
                    [pandeiro/boot-http            "0.8.3"]
                    [org.martinklepsch/boot-garden "1.3.2-1"]
                    [samestep/boot-refresh "0.1.0"]
                    [powerlaces/boot-cljs-devtools "0.2.0"]

                    ;; cljs-repl
                    [com.cemerick/piggieback "0.2.2"
                     :exclusions [org.clojure/clojurescript]]
                    [weasel                  "0.7.0"
                     :exclusions [org.clojure/clojurescript]]
                    [org.clojure/tools.nrepl "0.2.13"]
                    [binaryage/devtools "0.9.10"]

                    [crisptrutski/boot-cljs-test "0.3.4"]]))

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[metosin.boot-alt-test :refer [alt-test]]
 '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[powerlaces.boot-cljs-devtools :refer [cljs-devtools]]
 '[org.martinklepsch.boot-garden :refer [garden]]
 '[samestep.boot-refresh :refer [refresh]])

(task-options!
 pom {:project     'radicalzephyr/atreus-layout
      :version     "0.1.0"
      :description "A GUI for creating layouts for the Atreus keyboard."
      :url         "https://github.com/radicalzephyr/atreus-layout"
      :scm         {:url "https://github.com/radicalzephyr/atreus-layout.git"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

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
                                          :preloads '[day8.re-frame-10x.preload]
                                          :closure-defines {"re_frame.trace.trace_enabled_QMARK_"        true
                                                            "day8.re_frame.tracing.trace_enabled_QMARK_" true}}}
                 reload {:asset-path "public"
                         :on-jsload 'atreus.app/dev-reload}
                 serve {:handler 'atreus.layout/handler
                        :port 8080})
  identity)

(deftask testing
  "Configure a task pipeline with testing options"
  []
  (set-env! :source-paths #(conj % "test"))
  identity)


;; Low-level do-the-stuff tasks

(deftask unpack-antd
  "Extract antd css assets"
  []
  (comp
   (uber :include #{#"^cljsjs/antd"})
   (sift :move {#"^cljsjs/antd/development/antd\.inc\.css" "public/css/antd.css"
                #"^cljsjs/antd/production/antd\.min\.inc\.css" "public/css/antd.min.css"})
   (sift :include #{#"^cljsjs/antd"}
         :invert true)))

(deftask build
  "Compile clojurescript->js and garden->css"
  []
  (comp (unpack-antd)
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
        (notify :audible true)
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
        (notify :audible true)
        (alt-test)))
