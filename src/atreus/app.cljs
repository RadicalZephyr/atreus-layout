(ns atreus.app
  (:require [reagent.core :as reagent]
            [reagent.ratom :as ratom]
            [re-frame.core :as re-frame]
            [atreus.ui.base :as base]))

(defn setup-re-frame! []
  (re-frame/register-handler
   :initialise-db
   (fn
     [_ _]
     {})))

(defn main-panel []
  [:div
   [base/fixed 20
    [base/modal-root "Hello Modal!" {:show true}]]
   [:h1 "Hello World"]])

(defn init-render! []
  (re-frame/dispatch [:initialise-db])
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export setup! []
  (setup-re-frame!)
  (init-render!))
