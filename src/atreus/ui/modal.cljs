(ns atreus.ui.modal
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [clairvoyant.core :refer-macros [trace-forms]]
            [re-frame-tracer.core :refer [tracer]]))

(defn setup! []
  ;; Event Handlers
  (trace-forms {:tracer (tracer :color "green")}
    (re-frame/reg-event-db
     :show-modal
     (fn show-modal-event [db [_ content]]
       (-> db
           (assoc-in [:modal-options :show] true)
           (assoc :modal-content content))))

    (re-frame/reg-event-db
     :hide-modal
     (fn hide-modal-event [db _]
       (assoc-in db [:modal-options :show] false)))

    (re-frame/reg-event-db
     :set-modal-options
     (fn set-modal-options-event [db [_ options]]
       (assoc db :modal-options options))))

  ;; Subscriptions
  (trace-forms {:tracer (tracer :color "brown")}
    (re-frame/reg-sub
     :modal-content
     (fn modal-content-sub [db _]
       (:modal-content db)))

    (re-frame/reg-sub
     :modal-options
     (fn modal-options-sub [db _]
       (:modal-options db)))))

(def ^:private
  modal-defaults
  {:container-class-name nil
   :modal-class-name nil
   :max-width 600
   :min-width 280
   :overlay true
   :show false
   :close-fn #(re-frame/dispatch [:hide-modal])})

(def ^:private modal-container-id
  "modal-container")

(def ^:private modal-id
  "modal")

(defn- compact [coll]
  (keep identity coll))

(defn- ->css-classes [& class-names]
  (str/join " " (compact class-names)))

(defn modal-root [content options]
  (let [options (merge modal-defaults options)
        container-classes (cond-> [modal-container-id]
                            (:overlay options) (conj "modal-container--overlay")
                            (:show options) (conj "modal-container--show"))]
    (if (:show options)
      [:div.uber-container
       [:div.scotch-overlay.fade-and-drop.scotch-open]
       [:div.scotch-modal.fade-and-drop.scotch-open
        {:style (select-keys options [:max-width :min-width])}
        [:button.scotch-close.close-button
         {:onClick (:close-fn options)}
         "x"]
        [:div.scotch-content content]]]
      [:div])))
