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

  (re-frame/reg-event-db
   :set-layer-index
   (fn-traced
    set-layer-index-event
    [db [_ k]]
    (assoc db :layer-index k)))

  (re-frame/reg-event-db
   :add-layer
   (fn-traced
    add-layer-event
    [db _]
    (let [next-index (count (:current-layout db))]
      (if (>= 31 next-index)
        (-> db
            (assoc :layer-index next-index)
            (update :current-layout conj [{}]))
        db))))

  (re-frame/reg-event-fx
   :set-key
   (fn-traced
    set-key-event
    [cofx [_ index key]]
    (let [db (:db cofx)]
      {:db (assoc-in db [:current-layout (:layer-index db) 0 index] key)
       :dispatch [:close-modal]})))

  (re-frame/reg-event-fx
   :compile-layout
   (fn-traced
    compile-layout-event
    [cofx _]
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
   :layer-count
   :<- [:current-layout]
   (fn layer-count-sub [layers]
     (count layers)))

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
  [modal/modal-root
   @(re-frame/subscribe [:modal-content])
   @(re-frame/subscribe [:modal-options])])

;; TODO: still have to implement capturing modifier + key combinations
(defn character-capture [index]
  [base/register-dom-event js/document "keydown"
   #(re-frame/dispatch [:set-key index (.-key %)])
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

(defn content []
  [ant/layout-content {:class "content-area"}
   [layer/background
    #(re-frame/dispatch
      [:open-modal [character-capture %]])
    @(re-frame/subscribe [:current-bindings])]])

(defn menu [layer-index layer-count]
  [ant/layout
   [ant/layout-sider
    (into
     [ant/menu {:selected-keys [(str layer-index)]
                :mode "inline"
                :on-click #(let [k (.-key %)]
                             (if (= "add-layer" k)
                               (re-frame/dispatch [:add-layer])
                               (re-frame/dispatch [:set-layer-index (js/parseInt k)])))
                :style {:height "100%"}
                :theme "dark"}
      [ant/menu-item {:disabled true} "Layers"]]
     (concat
      (for [i (range 0 layer-count)]
        [ant/menu-item {:key (str i)}
         (str "Layer " (inc i))])
      (if (>= 31 layer-count)
       [[ant/menu-item {:key "add-layer"}
         [:span [ant/icon {:type "plus"}] "Add Layer"]]]
       [])))]
   [ant/layout {:style {:width "60%"}}
    [content]]])

(defn main-panel []
  [ant/locale-provider {:locale (ant/locales "en_US")}
   [ant/row {:type "flex" :justify "center"}
    [ant/col {:sm 24 :lg 22 :xl 18}
     [ant/layout
      [modal]
      [header]
      [menu
       @(re-frame/subscribe [:layer-index])
       @(re-frame/subscribe [:layer-count])]]]]])

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
