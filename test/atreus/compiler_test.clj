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
    (is (= "KC_RGUI" (sut/binding->key-symbol :rcmd))))

  (testing "special characters"
    (is (= "KC_CAPSLOCK" (sut/binding->key-symbol :caps-lock)))
    (is (= "KC_NUMLOCK" (sut/binding->key-symbol :num-lock)))
    (is (= "KC_SCROLLLOCK" (sut/binding->key-symbol :scroll-lock)))

    (is (= "KC_HOME" (sut/binding->key-symbol :home)))
    (is (= "KC_END" (sut/binding->key-symbol :end)))
    (is (= "KC_PGUP" (sut/binding->key-symbol :page-up))) ;;;
    (is (= "KC_PGDOWN" (sut/binding->key-symbol :page-down))) ;;;

    (is (= "KC_INSERT" (sut/binding->key-symbol :insert)))
    (is (= "KC_DELETE" (sut/binding->key-symbol :delete)))
    (is (= "KC_ENTER" (sut/binding->key-symbol :enter)))
    (is (= "KC_ESCAPE" (sut/binding->key-symbol :escape)))
    (is (= "KC_BSPACE" (sut/binding->key-symbol :backspace))) ;;;

    (is (= "KC_PAUSE" (sut/binding->key-symbol :pause)))
    (is (= "KC_PSCREEN" (sut/binding->key-symbol :print-screen))) ;;;

    (is (= "KC_TAB" (sut/binding->key-symbol :tab)))
    (is (= "KC_MENU" (sut/binding->key-symbol :menu)))

    (testing "function keys"
      (is (= "KC_F1" (sut/binding->key-symbol :f1)))
      (is (= "KC_F2" (sut/binding->key-symbol :f2)))
      (is (= "KC_F3" (sut/binding->key-symbol :f3)))
      (is (= "KC_F4" (sut/binding->key-symbol :f4)))
      (is (= "KC_F5" (sut/binding->key-symbol :f5)))
      (is (= "KC_F6" (sut/binding->key-symbol :f6)))
      (is (= "KC_F7" (sut/binding->key-symbol :f7)))
      (is (= "KC_F8" (sut/binding->key-symbol :f8)))
      (is (= "KC_F9" (sut/binding->key-symbol :f9)))
      (is (= "KC_F10" (sut/binding->key-symbol :f10)))
      (is (= "KC_F11" (sut/binding->key-symbol :f11)))
      (is (= "KC_F12" (sut/binding->key-symbol :f12))))))

(deftest test-compiler
  (is (= "" (sut/compile [{}]))))
