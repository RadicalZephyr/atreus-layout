(ns atreus.compiler-test
  (:require [clojure.test :refer :all]
            [atreus.compiler :as sut]))


(deftest test-binding->key-symbol
  (testing "regular characters"
    (is (= "KC_A" (sut/binding->key-symbol \a)))
    (is (= "KC_B" (sut/binding->key-symbol \b))))

  (testing "modifiers"
    (is (= "KC_LSHIFT" (sut/binding->key-symbol :lshift)))
    (is (= "KC_RSHIFT" (sut/binding->key-symbol :rshift)))
    (is (= "KC_LCTRL" (sut/binding->key-symbol :lctrl)))
    (is (= "KC_RCTRL" (sut/binding->key-symbol :rctrl)))
    (is (= "KC_LALT" (sut/binding->key-symbol :lalt)))
    (is (= "KC_RALT" (sut/binding->key-symbol :ralt)))
    (is (= "KC_LGUI" (sut/binding->key-symbol :lsuper)))
    (is (= "KC_RGUI" (sut/binding->key-symbol :rsuper)))
    (is (= "KC_LGUI" (sut/binding->key-symbol :lcmd)))
    (is (= "KC_RGUI" (sut/binding->key-symbol :rcmd)))))

(deftest test-compiler
  (is (= "" (sut/compile [[{}]]))))
