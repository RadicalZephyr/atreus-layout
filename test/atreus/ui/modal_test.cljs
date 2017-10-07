(ns atreus.ui.modal-test
  (:require [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [deftest defcard defcard-rg]]
            [atreus.ui.modal :as sut]
            [reagent.core :as r]))

(defcard-rg modal
  "Modals are cool."
  (fn [data _]
    [:div {:style {:width 600 :height 200}}
     [:button {:onClick #(swap! data update-in [:options :show] not)}
      "Toggle Modal"]
     [sut/modal-root "Hello Modals!" (:options @data)]])
  (r/atom {:modal-content ""
           :options {:show false}}))

(defcard-rg modal-shown
  (fn [data _]
    [:div {:style {:width 400 :height 500}}
     [sut/modal-root "Visible Modal!" (merge {:close-fn #(swap! data update-in [:options :show] not)}
                                             (:options @data))]])
  (r/atom {:modal-content ""
           :options {:show true}}))
