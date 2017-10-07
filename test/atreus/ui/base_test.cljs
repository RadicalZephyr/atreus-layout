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
    [sut/label "bksp"]]

   [:div
    [sut/label "tab"]
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
    [sut/label "enter"]]

   [:div
    [sut/label "lshift"]
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
    [sut/label "rshift"]]

   [:div
    [sut/label "lctrl"]
    [sut/label "super"]
    [sut/label "lalt"]
    [sut/label "spc"]
    [sut/label "ralt"]
    [sut/label "menu"]
    [sut/label "rctrl"]]

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
    [sut/label "bksp"]]

   [:div
    [sut/label "tab"]
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
    [sut/label "capslock"]
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
    [sut/label "lshift"]
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
    [sut/label "rshift"]]

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
    [sut/label "home"]
    [sut/label "end"]
    [sut/label "pgup"]
    [sut/label "pgdn"]
    [sut/label "ins"]
    [sut/label "del"]
    [sut/label "pause"]
    [sut/label "break"]
    [sut/label "prtscr"]]

   "Rotated Symbols"
   [:div
    [sut/label "a"  :left]
    [sut/label "a"  :right]
    [sut/label "up" :left]
    [sut/label "up" :right]]])

(deftest test-index
  (testing "left side indices"
    (is (= 0 (sut/index :left 0 0)))
    (is (= 1 (sut/index :left 0 1)))
    (is (= 10 (sut/index :left 1 0))))

  (testing "right side indices"
    (is (= 9 (sut/index :right 0 0)))
    (is (= 8 (sut/index :right 0 1)))
    (is (= 19 (sut/index :right 1 0)))

    (testing "the last row is odd"
      (is (= 41 (sut/index :right 3 0)))
      (is (= 40 (sut/index :right 3 1)))
      (is (= 39 (sut/index :right 3 2)))
      (is (= 38 (sut/index :right 3 3)))
      (is (= 37 (sut/index :right 3 4))))))

(defcard-rg atreus-layout
  "The layout is done with an SVG and image map."
  (fn [data _]
    [:div#layout-root {:style {:border "1px solid black"}
                       :onClick #(let [root-el (.getElementById js/document "layout-root")
                                       root-rect (.getBoundingClientRect root-el)]
                                     (swap! data assoc
                                            :x (- (.-clientX %) (.-left root-rect))
                                            :y (- (.-clientY %) (.-top root-rect))))}
     [sut/layout-background (fn [index side]
                              (swap! data assoc
                                     :index index
                                     :side side))
      (:labels @data)]])
  {:labels (vec (repeat 42 nil))}
  {:inspect-data true
   :history true})

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
