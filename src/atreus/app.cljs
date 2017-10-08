(ns atreus.app
  (:require [reagent.core :as reagent]
            [reagent.ratom :as ratom]
            [re-frame.core :as re-frame]
            [atreus.ui.base :as base]
            [atreus.ui.modal :as modal]
            [atreus.ui.layer :as layer]))

(defn setup! []
  (modal/setup!)

  (re-frame/reg-event-db
   :initialise-db
   (fn
     [_ _]
     {:modal-options {}})))

(defn modal []
  [modal/modal-root
   @(re-frame/subscribe [:modal-content])
   @(re-frame/subscribe [:modal-options])])

(defn main-panel []
  [:div
   [modal]
   [layer/layer-background #(re-frame/dispatch [:show-modal (str "index " %1)])]])

(defn init-render! []
  (re-frame/dispatch [:initialise-db])
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export start! []
  (setup!)
  (init-render!))
