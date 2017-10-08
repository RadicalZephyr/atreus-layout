(ns atreus.app
  (:require [reagent.core :as reagent]
            [clairvoyant.core :refer-macros [trace-forms]]
            [re-frame-tracer.core :refer [tracer]]
            [re-frame.core :as re-frame]
            [atreus.ui.base :as base]
            [atreus.ui.modal :as modal]
            [atreus.ui.layer :as layer]
            [atreus.compiler :as compiler]))

(defn setup! []
  (modal/setup!)

  (trace-forms {:tracer (tracer :color "green")}
    (re-frame/reg-event-db
     :initialise-db
     (fn initialize-db-event
       [_ _]
       {:modal-options {}
        :layer-index 0
        :binding-index 0
        :current-layout (layer/empty-layout)}))

    (re-frame/reg-event-fx
     :set-key

     (fn set-key-event [cofx [_ index keyevent]]
       {:db (assoc-in (:db cofx) [:current-layout 0 0 index] (.-key keyevent))
        :dispatch [:close-modal]}))

    (re-frame/reg-event-db
     :compile-layout
     (fn compile-layout-event [db _]
       (->> (:current-layout db)
            compiler/compile
            (assoc db :compiled-layout)))))

  ;; Subscriptions
  (trace-forms {:tracer (tracer :color "brown")}
    (re-frame/reg-sub
     :current-layout
     (fn current-layout-sub [db _]
       (:current-layout db)))

    (re-frame/reg-sub
     :layer-index
     (fn layer-index-sub [db _]
       (:layer-index db)))

    (re-frame/reg-sub
     :binding-index
     (fn binding-index-sub [db _]
       (:binding-index db)))

    (re-frame/reg-sub
     :current-bindings
     :<- [:current-layout]
     :<- [:layer-index]
     :<- [:binding-index]
     (fn binding-sub [[current-layout layer-index binding-index] _]
       (get-in current-layout [layer-index binding-index])))))

(defn modal []
  [modal/modal-root
   @(re-frame/subscribe [:modal-content])
   @(re-frame/subscribe [:modal-options])])

;; TODO: still have to implement capturing modifier + key combinations
(defn character-capture [index]
  [base/register-dom-event js/document "keydown"
   #(re-frame/dispatch [:set-key index %1])
   [:h3 "Press a key"]
   "To assign that key to this button."])

(defn main-panel []
  [:div
   [modal]
   [:div#app-content
    [layer/layer-background
     #(re-frame/dispatch
        [:open-modal [character-capture %1]])
     @(re-frame/subscribe [:current-bindings])]
    [:input {:type "button"
             :value "Compile!"
             :onClick #(re-frame/dispatch [:compile-layout])}]]])

(defn init-render! []
  (re-frame/dispatch [:initialise-db])
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export start! []
  (setup!)
  (init-render!))
