(ns atreus.ui.base-test
  (:require [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [deftest defcard defcard-rg]]
            [atreus.ui.base :as sut]
            [reagent.core]))

(defcard-rg key-labels
  "All key labels are made with SVG sprites, and a little bit of magic."

  [:div
   "Normal keyboard"
   [:div
    [sut/label "1"]
    [sut/label "2"]
    [sut/label "3"]
    [sut/label "4"]
    [sut/label "5"]
    [sut/label "6"]
    [sut/label "7"]
    [sut/label "8"]
    [sut/label "9"]
    [sut/label "0"]
    [sut/label "-"]
    [sut/label "="]]

   [:div
    [sut/label "q"]
    [sut/label "w"]
    [sut/label "e"]
    [sut/label "r"]
    [sut/label "t"]
    [sut/label "y"]
    [sut/label "u"]
    [sut/label "i"]
    [sut/label "o"]
    [sut/label "p"]
    [sut/label "["]
    [sut/label "]"]
    [sut/label "\\"]]

   [:div
    [sut/label "a"]
    [sut/label "s"]
    [sut/label "d"]
    [sut/label "f"]
    [sut/label "g"]
    [sut/label "h"]
    [sut/label "j"]
    [sut/label "k"]
    [sut/label "l"]
    [sut/label ";"]
    [sut/label "'"]]

   [:div
    [sut/label "z"]
    [sut/label "x"]
    [sut/label "c"]
    [sut/label "v"]
    [sut/label "b"]
    [sut/label "n"]
    [sut/label "m"]
    [sut/label ","]
    [sut/label "."]
    [sut/label "/"]]

   "Shifted Keyboard"
   [:div
    [sut/label "!"]
    [sut/label "@"]
    [sut/label "#"]
    [sut/label "$"]
    [sut/label "%"]
    [sut/label "^"]
    [sut/label "&"]
    [sut/label "*"]
    [sut/label "("]
    [sut/label ")"]
    [sut/label "_"]
    [sut/label "+"]]

   [:div
    [sut/label "q"]
    [sut/label "w"]
    [sut/label "e"]
    [sut/label "r"]
    [sut/label "t"]
    [sut/label "y"]
    [sut/label "u"]
    [sut/label "i"]
    [sut/label "o"]
    [sut/label "p"]
    [sut/label "{"]
    [sut/label "}"]
    [sut/label "|"]]

   [:div
    [sut/label "a"]
    [sut/label "s"]
    [sut/label "d"]
    [sut/label "f"]
    [sut/label "g"]
    [sut/label "h"]
    [sut/label "j"]
    [sut/label "k"]
    [sut/label "l"]
    [sut/label ":"]
    [sut/label "\""]]

   [:div
    [sut/label "z"]
    [sut/label "x"]
    [sut/label "c"]
    [sut/label "v"]
    [sut/label "b"]
    [sut/label "n"]
    [sut/label "m"]
    [sut/label "<"]
    [sut/label ">"]
    [sut/label "?"]]])
