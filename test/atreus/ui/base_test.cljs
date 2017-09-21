(ns atreus.ui.base-test
  (:require [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [deftest defcard defcard-rg]]
            [atreus.ui.base :as sut]
            [reagent.core]))

(defcard-rg key-labels
  "All key labels are made with SVG sprites, and a little bit of magic."
  [:div
   [sut/label "q"]
   [sut/label "w"]
   [sut/label "e"]
   [sut/label "r"]
   ])
