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
    [:link {:rel "apple-touch-icon" :sizes "180x180" :href "/apple-touch-icon.png"}]
    [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/favicon-32x32.png"}]
    [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/favicon-16x16.png"}]
    [:link {:rel "manifest" :href "/manifest.json"}]
    [:link {:rel "mask-icon" :href "/safari-pinned-tab.svg" :color "#5bbad5"}]
    [:meta {:name "theme-color" :content "#ffffff"}]
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
