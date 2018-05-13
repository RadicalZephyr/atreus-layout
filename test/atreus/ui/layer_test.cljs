(ns atreus.ui.layer-test
  (:require [cljs.test :refer-macros [is testing]]
            [clojure.spec.alpha :as s]
            [devcards.core :refer-macros [deftest defcard defcard-rg]]
            [reagent.core :as r]
            [atreus.ui.layer :as sut]))

(deftest test-layout-spec
  (is (not (s/valid? :atreus/layout
                     [])))
  (is (not (s/valid? :atreus/layout
                     [{}])))
  (is (s/valid? :atreus/layout
                [{0 \c}]))
  (is (not (s/valid? :atreus/layout
                     [{0 "ca"}])))
  (is (s/valid? :atreus/layout
                [{0 :escape}])))

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

(defcard-rg atreus-layer
  "The layer is done with an SVG and image map."
  (fn [data _]
    [:div#layer-root {:style {:border "1px solid black"}
                      :onClick #(let [root-el (.getElementById js/document "layer-root")
                                      root-rect (.getBoundingClientRect root-el)]
                                  (swap! data assoc
                                         :x (- (.-clientX %) (.-left root-rect))
                                         :y (- (.-clientY %) (.-top root-rect))))}
     [sut/layer-background (fn [index side]
                             (swap! data assoc
                                    :index index
                                    :side side))
      {}]])
  {}
  {:inspect-data true
   :history true})

(defcard-rg atreus-layer-with-labels
  (fn [data _]
    [:div
     [sut/layer-background (fn [index side]
                             (swap! data assoc-in [:bindings index] (str index)))
      (:bindings @data)]])
  (r/atom {:bindings {}}))
