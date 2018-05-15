(ns atreus.compiler
  (:refer-clojure :exclude [compile])
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [atreus.command]))

(def ^:private
  renames
  {\             "SPACE"
   \;            "SCOLON"
   \'            "QUOTE"
   \,            "COMMA"
   \.            "DOT"
   \/            "SLASH"
   \\            "BSLASH"
   \-            "MINUS"
   \=            "EQUAL"
   \`            "GRAVE"
   :lsuper       "LGUI"
   :rsuper       "RGUI"
   :lcmd         "LGUI"
   :rcmd         "RGUI"
   :page-up      "PGUP"
   :page-down    "PGDOWN"
   :backspace    "BSPACE"
   :print-screen "PSCREEN"})

(declare binding->key-symbol)

(defmulti -binding->key-symbol
  (fn [binding]
    (let [res (s/conform :atreus/command binding)]
      (when-not (s/invalid? res)
        (first res)))))

(defmethod -binding->key-symbol nil
  [_]
  "KC_NO")

(defn- key-symbol-for [k f]
  (str "KC_" (str/upper-case (get renames k (f k)))))

(defmethod -binding->key-symbol :character
  [binding]
  (key-symbol-for binding identity))

(defmethod -binding->key-symbol :modifier
  [binding]
  (key-symbol-for binding name))

(defmethod -binding->key-symbol :special-characters
  [binding]
  (key-symbol-for binding #(str/replace (name %)
                                        "-"
                                        "")))

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

(defmethod -binding->key-symbol :action
  [f]
  (str "KC_FN" (:action/index f)))

(defn binding->key-symbol [binding]
  (-binding->key-symbol binding))

(defn compile-layer [layer]
  (str "KEYMAP("
       (str/join ", "
                 (map #(binding->key-symbol
                        (get layer % []))
                      (range 42)))
       ")"))

(defn compile-layers [layers]
  (str "const uint16_t PROGMEM keymaps[][MATRIX_ROWS][MATRIX_COLS] = {\n  "
       (str/join ",\n  " (map compile-layer layers))
       "\n};"))

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

(defmethod -compile-fn-action :fn
  [action]
  (str "FUNCTION(" (:fn/name action) ")"))

(defn compile-fn-action [action]
  (str "["
       (:action/index action)
       "] = ACTION_"
       (-compile-fn-action action)))

(defn compile-fn-actions [actions]
  (str "const uint16_t PROGMEM fn_actions[] = {\n  "
       (str/join ",\n  "
                 (map compile-fn-action actions))
       "\n};"))

(defn- add-action-index [idx el]
  (if (s/valid? :atreus.command/action el)
    (assoc el :action/index (swap! idx inc))
    el))

(defn process [layout]
  (let [cur-idx (atom -1) ;; start at -1 because swap! returns the new value
        layout (walk/prewalk #(add-action-index cur-idx %) layout)
        actions (->> layout
                     first
                     (filter #(s/valid? :atreus.command/action (second %)))
                     (map second))]
    [layout actions]))

(defn compile [layout]
  (let [[layers actions] (process layout)]
    (str
     (compile-layers layers)
     "\n"
     (compile-fn-actions actions))))
