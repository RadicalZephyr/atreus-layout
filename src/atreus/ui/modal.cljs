(ns atreus.ui.modal
  (:require [clojure.string :as str]))

(def ^:private
  modal-defaults
  {:container-class-name nil
   :modal-class-name nil
   :max-width 600
   :min-width 280
   :overlay true
   :show false
   :close-fn #()})

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
