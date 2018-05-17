(ns atreus.ui.base-test
  (:require [cljs.test :refer-macros [is testing]]
            [devcards.core :refer-macros [deftest defcard defcard-rg]]
            [atreus.ui.base :as sut]
            [reagent.core :as r]))

(defcard-rg key-labels
  "All key labels are made with SVG sprites, and a little bit of magic."

  [:div
   "Normal keyboard"
   [:div
    [sut/label "`"]
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
    [sut/label "="]
    [sut/label :backspace]]

   [:div
    [sut/label :tab]
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
    [sut/label "'"]
    [sut/label :enter]]

   [:div
    [sut/label :lshift]
    [sut/label "z"]
    [sut/label "x"]
    [sut/label "c"]
    [sut/label "v"]
    [sut/label "b"]
    [sut/label "n"]
    [sut/label "m"]
    [sut/label ","]
    [sut/label "."]
    [sut/label "/"]
    [sut/label :rshift]]

   [:div
    [sut/label :lctrl]
    [sut/label :lsuper]
    [sut/label :lalt]
    [sut/label " "]
    [sut/label :ralt]
    [sut/label :menu]
    [sut/label :rctrl]]

   "Shifted Keyboard"
   [:div
    [sut/label "~"]
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
    [sut/label "+"]
    [sut/label :backspace]]

   [:div
    [sut/label :tab]
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
    [sut/label :caps-lock]
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
    [sut/label :lshift]
    [sut/label "z"]
    [sut/label "x"]
    [sut/label "c"]
    [sut/label "v"]
    [sut/label "b"]
    [sut/label "n"]
    [sut/label "m"]
    [sut/label "<"]
    [sut/label ">"]
    [sut/label "?"]
    [sut/label :rshift]]

   "Arrow Keys"
   [:div
    [sut/label "left"]
    [sut/label "right"]
    [sut/label "up"]
    [sut/label "down"]]

   "Function Keys"
   [:div
    [sut/label "f1"]
    [sut/label "f2"]
    [sut/label "f3"]
    [sut/label "f4"]
    [sut/label "f5"]
    [sut/label "f6"]
    [sut/label "f7"]
    [sut/label "f8"]
    [sut/label "f9"]
    [sut/label "f10"]
    [sut/label "f11"]
    [sut/label "f12"]]

   "Special Keys"
   [:div
    [sut/label :home]
    [sut/label :end]
    [sut/label :page-up]
    [sut/label :page-down]
    [sut/label :insert]
    [sut/label :delete]
    [sut/label :pause]
    [sut/label :break]
    [sut/label :print-screen]]

   "Rotated Symbols"
   [:div
    [sut/label "a"  :left]
    [sut/label "a"  :right]
    [sut/label "up" :left]
    [sut/label "up" :right]]

   "Composite keystrokes"
   [:div
    [sut/label ["a" :lshift]]
    [sut/label ["a" :lctrl]]
    [sut/label ["a" :lalt]]
    [sut/label ["a" :lgui]]
    [sut/label ["a" :lsuper]]
    [sut/label ["a" :lcmd]]]])

(defcard-rg keyup-capture-card
  (fn [data _]
    [sut/register-dom-event js/document "keyup"
     (fn [e] (reset! data (.-key e)))
     [:div "Type a key. The ASCII character code will appear below."]])
  (r/atom nil)
  {:inspect-data true
   :history true})

(deftest test-render-key
  (testing "character"
    (is (= "a" (sut/render-key "a"))))

  (testing "modifiers"
    (is (= "shift" (sut/render-key :lshift)))
    (is (= "shift" (sut/render-key :rshift)))
    (is (= "ctrl" (sut/render-key :lctrl)))
    (is (= "ctrl" (sut/render-key :rctrl)))
    (is (= "alt" (sut/render-key :lalt)))
    (is (= "alt" (sut/render-key :ralt)))
    (is (= "gui" (sut/render-key :lgui)))
    (is (= "gui" (sut/render-key :rgui)))
    (is (= "super" (sut/render-key :lsuper)))
    (is (= "super" (sut/render-key :rsuper)))
    (is (= "cmd" (sut/render-key :lcmd)))
    (is (= "cmd" (sut/render-key :rcmd))))

  (testing "special characters"
    (is (= "escape" (sut/render-key :escape)))
    (is (= "tab" (sut/render-key :tab)))
    (is (= "enter" (sut/render-key :enter))))

  (testing "composite"
   (is (= "C-a" (sut/render-key ["a" :lctrl])))
   (is (= "C-a" (sut/render-key ["a" :rctrl])))
   (is (= "C-b" (sut/render-key ["b" :lctrl])))
   (is (= "M-b" (sut/render-key ["b" :lalt])))
   (is (= "M-b" (sut/render-key ["b" :ralt])))
   (is (= "s-b" (sut/render-key ["b" :lshift])))
   (is (= "s-b" (sut/render-key ["b" :rshift])))
   (is (= "S-b" (sut/render-key ["b" :lgui])))
   (is (= "S-b" (sut/render-key ["b" :rgui])))
   (is (= "u-b" (sut/render-key ["b" :lsuper])))
   (is (= "u-b" (sut/render-key ["b" :rsuper])))
   (is (= "c-b" (sut/render-key ["b" :lcmd])))
   (is (= "c-b" (sut/render-key ["b" :rcmd]))))

  (testing "multiple composite"
    (is (= "C-M-a" (sut/render-key ["a" :lalt :lctrl])))
    (is (= "M-C-a" (sut/render-key ["a" :lctrl :lalt])))
    (is (= "s-C-c" (sut/render-key ["c" :lctrl :lshift])))))
