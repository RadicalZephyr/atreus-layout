(ns atreus.ui.base
  (:require [clojure.string :as str]))

(defn- font-size-for [name]
  (case (count name)
    (2 3 4) "14px"
    "12px"))

(defn- coords-for [name]
  {:x (str (- 12 (* 2 (count name))))
   :y "24"})

(defn- raw-label [name font-size coords]
  [:svg.label
   [:text {:style {:font-size font-size
                   :line-height "125%"
                   :font-family "Anonymous Pro"
                   :letter-spacing "0px"
                   :fill "#000000"
                   :fill-opacity "1"
                   :stroke "none"}}
    [:tspan coords name]]])

(defn- arrow-key? [name]
  (contains? #{"left" "right" "up" "down"} name))

(defmulti -label
  (fn [name]
    (cond
      (= 1 (count name)) :symbol-key
      (arrow-key? name) :arrow-key
      (re-matches #"[fF]\d+" name) :f-key
      :else :default)))

(defmethod -label :default [name]
  (raw-label name (font-size-for name) (coords-for name)))

(defmethod -label :symbol-key [name]
  (raw-label (str/capitalize name) "40px" {:x "9" :y "31"}))

(defmethod -label :f-key [name]
  (raw-label name "18px" {:x "6" :y "26"}))

(defmethod -label :arrow-key [name]
  [:svg.label
   [:use {:xlinkHref (str "/img/key-sprites.svg#label_" name)}]])

(defn label [name]
  (-label name))
