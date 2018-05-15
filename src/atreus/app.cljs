(ns atreus.app
  (:require [reagent.core :as reagent]
            [antizer.reagent :as ant]
            [re-frame.core :as re-frame]
            [atreus.ui.base :as base]
            [atreus.ui.modal :as modal]
            [atreus.ui.layer :as layer]
            [atreus.compiler :as compiler]
            [atreus.download :refer [download]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn setup! []
  (modal/setup!)

  (re-frame/reg-fx
   :download-file
   (fn [[filename content]]
     (download filename content)))

  (re-frame/reg-event-db
   :initialise-db
   (fn-traced
    initialize-db-event
    [_ _]
    {:modal-options {}
     :layer-index 0
     :binding-index 0
     :current-layout (layer/empty-layout)}))

  (re-frame/reg-event-fx
   :set-key
   (fn-traced
    set-key-event [cofx [_ index keyevent]]
    {:db (assoc-in (:db cofx) [:current-layout 0 0 index] (.-key keyevent))
     :dispatch [:close-modal]}))

  (re-frame/reg-event-fx
   :compile-layout
   (fn-traced
    compile-layout-event [cofx _]
    (let [content (compiler/compile
                   (get-in cofx [:db :current-layout]))]
      {:download-file ["keymap.c" content]})))

  ;; Subscriptions
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
     (get-in current-layout [layer-index binding-index]))))

(defn modal []
  [ant/modal
   @(re-frame/subscribe [:modal-options])
   @(re-frame/subscribe [:modal-content])])

;; TODO: still have to implement capturing modifier + key combinations
(defn character-capture [index]
  [base/register-dom-event js/document "keydown"
   #(re-frame/dispatch [:set-key index %1])
   [:h3 "Press a key"]
   "To assign that key to this button."])

(defn header []
  [ant/layout-header {:class "banner"}
   (reagent/as-element
    [ant/row
     [ant/col {:span 12}
      [:h2.banner-header "Atreus Layout"]]
     [ant/col {:span 1 :offset 11}
      [ant/button {:icon "download"
                   :size "large"
                   :on-click #(re-frame/dispatch [:compile-layout])}]]])])

(defn menu []
  [ant/menu {:mode "inline" :theme "dark" :style {:height "100%"}}
   [ant/menu-item {:disabled true} "Layer"]])

(defn content []
  [ant/layout-content {:class "content-area"}
   [layer/layer-background
    #(re-frame/dispatch
      [:open-modal [character-capture %1]])
    @(re-frame/subscribe [:current-bindings])]])

(defn main-panel []
  [ant/locale-provider {:locale (ant/locales "en_US")}
   [ant/layout
    [modal]
    [header]
    [ant/layout
     [ant/layout-sider
      [menu]]
     [ant/layout {:style {:width "60%"}}
      [content]]]]])

(defn init-render! []
  (re-frame/dispatch [:initialise-db])
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export start! []
  (setup!)
  (init-render!))

(defn dev-reload []
  (re-frame/clear-subscription-cache!)
  (start!))
