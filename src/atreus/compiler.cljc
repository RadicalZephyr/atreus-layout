(ns atreus.compiler
  (:refer-clojure :exclude [compile])
  (:require [clojure.string :as str]))

(defmulti -binding->key-symbol
  (fn [binding]
    :atreus.command/character))

(defmethod -binding->key-symbol :atreus.command/character
  [binding]
  (str "KC_" (str/upper-case binding)))

(defn binding->key-symbol [binding]
  (-binding->key-symbol binding))

(defn compile [layout]
  "")
