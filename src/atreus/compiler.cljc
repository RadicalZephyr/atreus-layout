(ns atreus.compiler
  (:refer-clojure :exclude [compile])
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [atreus.command]))

(def ^:private
  renames
  {:lsuper       :lgui
   :rsuper       :rgui
   :lcmd         :lgui
   :rcmd         :rgui
   :page-up      :pgup
   :page-down    :pgdown
   :backspace    :bspace
   :print-screen :pscreen})

(declare binding->key-symbol)

(defmulti -binding->key-symbol
  (fn [binding]
    (let [res (s/conform :atreus/command binding)]
      (when-not (s/invalid? res)
        (first res)))))

(defmethod -binding->key-symbol nil
  [_]
  "KC_NO")

(defmethod -binding->key-symbol :character
  [binding]
  (str "KC_" (str/upper-case binding)))

(defmethod -binding->key-symbol :modifier
  [binding]
  (str "KC_" (str/upper-case (name (get renames binding binding)))))

(defmethod -binding->key-symbol :special-characters
  [binding]
  (str "KC_" (str/upper-case (str/replace (name (get renames binding binding))
                                          "-"
                                          ""))))

(def ^:private
  modifier-macro
  {:lshift "SHIFT"
   :rshift "SHIFT"
   :lctrl  "CTRL"
   :rctrl  "CTRL"
   :lgui   "GUI"
   :rgui   "GUI"
   :lsuper "GUI"
   :rsuper "GUI"
   :lcmd   "GUI"
   :rcmd   "GUI"
   :lalt   "ALT"
   :ralt   "RALT"})

(defmethod -binding->key-symbol :composite
  [[k & mods]]
  (reduce (fn [res mod]
            (str (modifier-macro mod) "(" res ")"))
          (binding->key-symbol k)
          mods))

(defn binding->key-symbol [binding]
  (-binding->key-symbol binding))

(defn compile-layer [layer]
  (str "KEYMAP("
       (str/join ", "
                 (map #(binding->key-symbol
                        (get layer % []))
                      (range 42)))
       ")"))

(defmulti -compile-fn-action :action/type)

(defmethod -compile-fn-action :layer/momentary
  [action]
  (str "LAYER_MOMENTARY(" (:layer/index action) ")"))

(defmethod -compile-fn-action :layer/on
  [action]
  (str "LAYER_ON(" (:layer/index action) ", 1)"))

(defmethod -compile-fn-action :layer/off
  [action]
  (str "LAYER_OFF(" (:layer/index action) ", 1)"))

(defmethod -compile-fn-action :function
  [action]
  (str "FUNCTION(" (:function/name action) ")"))

(defn compile-fn-action [action]
  (str "["
       (:fn/index action)
       "] = ACTION_"
       (-compile-fn-action action)))

(defn compile-fn-actions [actions]
  (str "const uint16_t PROGMEM fn_actions[] = {\n  "
       (str/join ",\n  "
                 (map compile-fn-action actions))
       "\n};"))

(defn compile [layout]
  "")
