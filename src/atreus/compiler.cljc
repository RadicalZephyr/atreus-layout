(ns atreus.compiler
  (:refer-clojure :exclude [compile])
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [atreus.command]))

(def ^:private
  renames
  {:lsuper :lgui
   :rsuper :rgui
   :lcmd :lgui
   :rcmd :rgui})

(defmulti -binding->key-symbol
  (fn [binding]
    (first (s/conform :atreus/command binding))))

(defmethod -binding->key-symbol :character
  [binding]
  (str "KC_" (str/upper-case binding)))

(defmethod -binding->key-symbol :modifier
  [binding]
  (str "KC_" (str/upper-case (name (get renames binding binding)))))

(defn binding->key-symbol [binding]
  (-binding->key-symbol binding))

(defn compile [layout]
  "")
