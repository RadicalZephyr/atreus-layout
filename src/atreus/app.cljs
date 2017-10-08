(ns atreus.app
  (:require [reagent.core :as reagent]
            [clairvoyant.core :refer-macros [trace-forms]]
            [re-frame-tracer.core :refer [tracer]]
            [re-frame.core :as re-frame]
            [atreus.ui.base :as base]
            [atreus.ui.modal :as modal]
            [atreus.ui.layer :as layer]))

(defn empty-layout []
  [[{}]])

(defn setup! []
  (modal/setup!)

  (trace-forms {:tracer (tracer :color "green")}
    (re-frame/reg-event-db
     :initialise-db
     (fn initialize-db-event
       [_ _]
       {:modal-options {}
        :current-layout (empty-layout)}))

    (re-frame/reg-event-db
     :set-key
     (fn set-key-event [db [_ index keyevent]]
       (assoc-in db [:current-layout 0 0 index] (.-key keyevent))))))

(defn modal []
  [modal/modal-root
   @(re-frame/subscribe [:modal-content])
   @(re-frame/subscribe [:modal-options])])

;; TODO: still have to implement capturing modifier + key combinations
(defn character-capture [index]
  [base/register-dom-event js/document "keydown"
   #(do
      (re-frame/dispatch [:close-modal])
      (re-frame/dispatch [:set-key index %1]))
   [:h3 "Press a key"]
   "To assign that key to this button."])

(defn main-panel []
  [:div
   [modal]
   [layer/layer-background #(re-frame/dispatch
                             [:open-modal [character-capture %1]])]])

(defn init-render! []
  (re-frame/dispatch [:initialise-db])
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export start! []
  (setup!)
  (init-render!))
