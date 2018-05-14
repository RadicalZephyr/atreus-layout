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
    (first (s/conform :atreus/command binding))))

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

(defn compile [layout]
  "")
