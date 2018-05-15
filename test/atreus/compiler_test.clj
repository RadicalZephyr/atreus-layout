(ns atreus.compiler-test
  (:require [clojure.test :refer :all]
            [atreus.compiler :as sut]
            [clojure.spec.alpha :as s]))


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

    (is (= "CTRL(SHIFT(KC_A))" (sut/binding->key-symbol [\a :lshift :lctrl]))))

  (testing "actions"
    (is (= "KC_FN0" (sut/binding->key-symbol {:action/index 0 :action/type :fn})))
    (is (= "KC_FN1" (sut/binding->key-symbol
                     {:action/index 1 :action/type :layer/momentary})))))

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
         (sut/compile-fn-action {:action/index 1
                                 :action/type :layer/momentary
                                 :layer/index 2})))

  (is (= "[1] = ACTION_LAYER_ON(2, 1)"
         (sut/compile-fn-action {:action/index 1
                                 :action/type :layer/on
                                 :layer/index 2})))

  (is (= "[3] = ACTION_LAYER_OFF(1, 1)"
         (sut/compile-fn-action {:action/index 3
                                 :action/type :layer/off
                                 :layer/index 1})))

  (is (= "[5] = ACTION_FUNCTION(FOO)"
         (sut/compile-fn-action {:action/index 5
                                 :action/type :fn
                                 :fn/name "FOO"}))))

(deftest test-compile-fn-actions
  (is (= (str "const uint16_t PROGMEM fn_actions[] = {\n"
              "  [0] = ACTION_FUNCTION(BOOTLOADER)\n"
              "};")
         (sut/compile-fn-actions [{:action/index 0
                                   :action/type :fn
                                   :fn/name "BOOTLOADER"}])))

  (is (= (str "const uint16_t PROGMEM fn_actions[] = {\n"
              "  [0] = ACTION_FUNCTION(BOOTLOADER),\n"
              "  [1] = ACTION_FUNCTION(FOO)\n"
              "};")
         (sut/compile-fn-actions [{:action/index 0
                                   :action/type :fn
                                   :fn/name "BOOTLOADER"}
                                  {:action/index 1
                                   :action/type :fn
                                   :fn/name "FOO"}]))))

(deftest test-compile
  (is (= (str
          "const uint16_t PROGMEM keymaps[][MATRIX_ROWS][MATRIX_COLS] = {\n"
          "  KEYMAP(KC_Q, KC_W, KC_E, KC_R, KC_T, KC_Y, KC_U, KC_I, KC_O, KC_P,"
          " KC_A, KC_S, KC_D, KC_F, KC_G, KC_H, KC_J, KC_K, KC_L, KC_SCOLON,"
          " KC_Z, KC_X, KC_C, KC_V, KC_B, KC_N, KC_M, KC_COMMA, KC_DOT, KC_SLASH,"
          " KC_ESCAPE, KC_TAB, KC_LGUI, KC_LSHIFT, KC_BSPACE, KC_LCTRL, KC_LALT, KC_SPACE, KC_FN0, KC_MINUS, KC_QUOTE, KC_ENTER)\n"
          "};\n"
          "const uint16_t PROGMEM fn_actions[] = {\n"
          "  [0] = ACTION_FUNCTION(BOOTLOADER)\n"
          "};")
         (let [bootloader {:action/type :fn :fn/name "BOOTLOADER"}]
           (sut/compile [{0 \q 1 \w 2 \e 3 \r 4 \t 5 \y 6 \u 7 \i 8 \o 9 \p
                          10 \a 11 \s 12 \d 13 \f 14 \g 15 \h 16 \j 17 \k 18 \l 19 \;
                          20 \z 21 \x 22 \c 23 \v 24 \b 25 \n 26 \m 27 \, 28 \. 29 \/
                          30 :escape 31 :tab 32 :lgui 33 :lshift 34 :backspace 35 :lctrl
                          36 :lalt 37 \  38 bootloader 39 \- 40 \' 41 :enter}])))))
