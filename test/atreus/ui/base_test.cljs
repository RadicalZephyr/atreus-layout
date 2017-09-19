(ns atreus.ui.base-test
  (:require [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [deftest defcard-rg]]
            [atreus.ui.base :as sut]
            [reagent.core]))

(defcard-rg buttons
  "The basic buttons used in the UI."
  [:div {:style {:width "12em"}}
   [sut/button "Press me." (fn [])]])

(defcard-rg button-panel
  "This is the core button panel."
  [:div
   [sut/button-panel {}]
   [:p "expand"]])

(defcard-rg grid
  [:div {:style {:border "3px cornflowerblue solid"}}
   [:div
    [:div.test.column.half [:p " "]]
    [:div.test.column.half [:p " "]]
    [:br]]

   [:div
    [:div.test.column.third [:p " "]]
    [:div.test.column.third [:p " "]]
    [:div.test.column.third [:p " "]]
    [:br]]

   [:div
    [:div.test.column.third [:p " "]]
    [:div.test.column.two-thirds [:p " "]]
    [:br]]

   [:div
    [:div.test.column.fourth [:p " "]]
    [:div.test.column.fourth [:p " "]]
    [:div.test.column.fourth [:p " "]]
    [:div.test.column.fourth [:p " "]]
    [:br]]

   [:div
    [:div.test.column.three-fourths [:p " "]]
    [:div.test.column.fourth [:p " "]]
    [:br]]

   [:div
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:br]]

   [:div
    [:div.test.column.three-fifths [:p " "]]
    [:div.test.column.two-fifths [:p " "]]
    [:br]]
   [:div
    [:div.test.column.two-fifths [:p " "]]
    [:div.test.column.three-fifths [:p " "]]
    [:br]]

   [:div
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:br]]

   [:div
    [:div.test.column.four-fifths [:p " "]]
    [:div.test.column.fifth [:p " "]]
    [:br]]])
