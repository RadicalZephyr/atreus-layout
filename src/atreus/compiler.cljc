(ns atreus.compiler
  (:refer-clojure :exclude [compile])
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [atreus.command]))

(defmulti -binding->key-symbol
  (fn [binding]
    (first (s/conform :atreus/command binding))))

(defmethod -binding->key-symbol :character
  [binding]
  (str "KC_" (str/upper-case binding)))

(defmethod -binding->key-symbol :modifier
  [binding]
  "KC_LSHIFT")

(defn binding->key-symbol [binding]
  (-binding->key-symbol binding))

(defn compile [layout]
  "")
