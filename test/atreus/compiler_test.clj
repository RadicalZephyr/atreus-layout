(ns atreus.compiler-test
  (:require [clojure.test :refer :all]
            [atreus.compiler :as sut]))


(deftest test-binding->key-symbol
  (testing "an unbound or invalid binding"
    (is (= "KC_NO" (sut/binding->key-symbol nil))))

  (testing "regular characters"
    (is (= "KC_A" (sut/binding->key-symbol \a)))
    (is (= "KC_B" (sut/binding->key-symbol \b)))
    (is (= "KC_SPACE"  (sut/binding->key-symbol \ )))
    (is (= "KC_SCOLON" (sut/binding->key-symbol \;)))
    (is (= "KC_QUOTE"  (sut/binding->key-symbol \')))
    (is (= "KC_COMMA" (sut/binding->key-symbol \,)))
    (is (= "KC_DOT"  (sut/binding->key-symbol \.)))
    (is (= "KC_SLASH" (sut/binding->key-symbol \/)))
    (is (= "KC_BSLASH" (sut/binding->key-symbol \\)))
    (is (= "KC_GRAVE"  (sut/binding->key-symbol \`)))
    (is (= "KC_MINUS" (sut/binding->key-symbol \-)))
    (is (= "KC_EQUAL" (sut/binding->key-symbol \=))))

  (testing "modifiers"
    (is (= "KC_LSHIFT" (sut/binding->key-symbol :lshift)))
    (is (= "KC_RSHIFT" (sut/binding->key-symbol :rshift)))
    (is (= "KC_LCTRL"  (sut/binding->key-symbol :lctrl)))
    (is (= "KC_RCTRL"  (sut/binding->key-symbol :rctrl)))
    (is (= "KC_LALT"   (sut/binding->key-symbol :lalt)))
    (is (= "KC_RALT"   (sut/binding->key-symbol :ralt)))
    (is (= "KC_LGUI"   (sut/binding->key-symbol :lsuper)))
    (is (= "KC_RGUI"   (sut/binding->key-symbol :rsuper)))
    (is (= "KC_LGUI"   (sut/binding->key-symbol :lcmd)))
    (is (= "KC_RGUI"   (sut/binding->key-symbol :rcmd))))

  (testing "special characters"
    (is (= "KC_ESCAPE" (sut/binding->key-symbol :escape)))
    (is (= "KC_BSPACE" (sut/binding->key-symbol :backspace)))
    (is (= "KC_ENTER"  (sut/binding->key-symbol :enter)))
    (is (= "KC_INSERT" (sut/binding->key-symbol :insert)))
    (is (= "KC_DELETE" (sut/binding->key-symbol :delete)))

    (is (= "KC_HOME" (sut/binding->key-symbol :home)))
    (is (= "KC_END"  (sut/binding->key-symbol :end)))
    (is (= "KC_PGUP" (sut/binding->key-symbol :page-up)))
    (is (= "KC_PGDOWN" (sut/binding->key-symbol :page-down)))

    (is (= "KC_MENU" (sut/binding->key-symbol :menu)))
    (is (= "KC_PSCREEN" (sut/binding->key-symbol :print-screen)))
    (is (= "KC_PAUSE"   (sut/binding->key-symbol :pause)))

    (is (= "KC_SCROLLLOCK" (sut/binding->key-symbol :scroll-lock)))
    (is (= "KC_NUMLOCK"    (sut/binding->key-symbol :num-lock)))
    (is (= "KC_CAPSLOCK"   (sut/binding->key-symbol :caps-lock)))

    (is (= "KC_TAB"  (sut/binding->key-symbol :tab)))

    (testing "function keys"
      (is (= "KC_F1"  (sut/binding->key-symbol :f1)))
      (is (= "KC_F2"  (sut/binding->key-symbol :f2)))
      (is (= "KC_F3"  (sut/binding->key-symbol :f3)))
      (is (= "KC_F4"  (sut/binding->key-symbol :f4)))
      (is (= "KC_F5"  (sut/binding->key-symbol :f5)))
      (is (= "KC_F6"  (sut/binding->key-symbol :f6)))
      (is (= "KC_F7"  (sut/binding->key-symbol :f7)))
      (is (= "KC_F8"  (sut/binding->key-symbol :f8)))
      (is (= "KC_F9"  (sut/binding->key-symbol :f9)))
      (is (= "KC_F10" (sut/binding->key-symbol :f10)))
      (is (= "KC_F11" (sut/binding->key-symbol :f11)))
      (is (= "KC_F12" (sut/binding->key-symbol :f12)))))

  (testing "composite commands"
    (is (= "SHIFT(KC_A)" (sut/binding->key-symbol [\a :lshift])))
    (is (= "SHIFT(KC_A)" (sut/binding->key-symbol [\a :rshift])))
    (is (= "CTRL(KC_A)"  (sut/binding->key-symbol [\a :lctrl])))
    (is (= "CTRL(KC_A)"  (sut/binding->key-symbol [\a :rctrl])))
    (is (= "GUI(KC_A)"   (sut/binding->key-symbol [\a :lgui])))
    (is (= "GUI(KC_A)"   (sut/binding->key-symbol [\a :rgui])))
    (is (= "GUI(KC_A)"   (sut/binding->key-symbol [\a :lsuper])))
    (is (= "GUI(KC_A)"   (sut/binding->key-symbol [\a :rsuper])))
    (is (= "GUI(KC_A)"   (sut/binding->key-symbol [\a :lcmd])))
    (is (= "GUI(KC_A)"   (sut/binding->key-symbol [\a :rcmd])))
    (is (= "ALT(KC_A)"   (sut/binding->key-symbol [\a :lalt])))
    (is (= "RALT(KC_A)"  (sut/binding->key-symbol [\a :ralt])))

    (is (= "CTRL(SHIFT(KC_A))" (sut/binding->key-symbol [\a :lshift :lctrl])))))

(deftest test-compile-layer
  (is (= "KEYMAP(KC_NO,"
         (subs (sut/compile-layer {})
               0 13)))

  (is (= "KEYMAP(KC_A, KC_B"
         (subs (sut/compile-layer {0 \a 1 \b})
               0 17)))

  (is (= 42
         (count
          (re-seq #"KC_NO"
                  (sut/compile-layer {}))))))

(deftest test-compile-layers
  (let [res (sut/compile-layers [{}])]
    (is (= (str "const uint16_t PROGMEM keymaps[][MATRIX_ROWS][MATRIX_COLS] = {\n"
                "  KEYMAP(")
           (subs res 0 72)))
    (is (= (str ")\n};")
           (subs res (- (count res) 4))))))

(deftest test-compile-fn-action
  (is (= "[1] = ACTION_LAYER_MOMENTARY(2)"
         (sut/compile-fn-action {:fn/index 1
                                 :action/type :layer/momentary
                                 :layer/index 2})))

  (is (= "[1] = ACTION_LAYER_ON(2, 1)"
         (sut/compile-fn-action {:fn/index 1
                                 :action/type :layer/on
                                 :layer/index 2})))

  (is (= "[3] = ACTION_LAYER_OFF(1, 1)"
         (sut/compile-fn-action {:fn/index 3
                                 :action/type :layer/off
                                 :layer/index 1})))

  (is (= "[5] = ACTION_FUNCTION(FOO)"
         (sut/compile-fn-action {:fn/index 5
                                 :action/type :function
                                 :function/name "FOO"}))))

(deftest test-compile-fn-actions
  (is (= (str "const uint16_t PROGMEM fn_actions[] = {\n"
              "  [0] = ACTION_FUNCTION(BOOTLOADER)\n"
              "};")
         (sut/compile-fn-actions [{:fn/index 0
                                   :action/type :function
                                   :function/name "BOOTLOADER"}])))

  (is (= (str "const uint16_t PROGMEM fn_actions[] = {\n"
              "  [0] = ACTION_FUNCTION(BOOTLOADER),\n"
              "  [1] = ACTION_FUNCTION(FOO)\n"
              "};")
         (sut/compile-fn-actions [{:fn/index 0
                                   :action/type :function
                                   :function/name "BOOTLOADER"}
                                  {:fn/index 1
                                   :action/type :function
                                   :function/name "FOO"}]))))

(deftest test-compiler
  (is (= "" (sut/compile [{}]))))
