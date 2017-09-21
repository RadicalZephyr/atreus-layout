(ns atreus.layout
  (:require [compojure.core :as c]
            [compojure.route :as route]
            [garden.core :as garden]
            [hiccup.page :as page]
            [ring.middleware.lint :as lint]
            [ring.middleware.logger :as log]
            [ring.middleware.params :as params]
            [ring.middleware.reload :as reload]
            [ring.middleware.stacktrace :as stacktrace]))

(defn main-page []
  (page/html5
   [:head
    (page/include-css "/css/normalize.css"
                      "/css/app.css")]
   [:body
    [:div.row
     [:div#app]]
    (page/include-js "/js/main.js")]))

(c/defroutes app-routes
  (c/GET "/" [] (main-page))

  (c/GET "/devcards" []
    (page/html5
     [:head
      [:meta {:charset "utf-8"}]
      (page/include-css "/css/normalize.css"
                        "/css/app.css")]
     [:body
      (page/include-js "/js/devcards.js")]))

  (c/GET "/tests" []
    (page/html5
     [:meta {:charset "utf-8"}]
     [:body
      (page/include-js "/js/tests.js")]))

  (route/resources "/"))

(def handler (-> app-routes
                 params/wrap-params
                 log/wrap-with-logger
                 lint/wrap-lint
                 stacktrace/wrap-stacktrace
                 reload/wrap-reload))
