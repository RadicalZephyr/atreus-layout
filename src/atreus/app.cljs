(ns atreus.app
  (:require [reagent.core :as reagent]
            [reagent.ratom :as ratom]
            [re-frame.core :as re-frame]
            [atreus.ui.base :as base]
            [atreus.ui.modal :as modal]))

(defn setup! []
  (re-frame/reg-event-db
   :initialise-db
   (fn
     [_ _]
     {:modal-options {}})))

(defn modal []
  [base/fixed 20
   [modal/modal-root "Hello Modal!" {:show true}]])

(defn main-panel []
  [:div
   [modal]
   [:h1 "Hello World"]])

(defn init-render! []
  (re-frame/dispatch [:initialise-db])
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export start! []
  (setup!)
  (init-render!))
