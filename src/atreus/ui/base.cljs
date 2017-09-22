(ns atreus.ui.base
  (:require [clojure.string :as str]))

(defn- font-size-for [name]
  (case (count name)
    1 "40px"
    (2 3 4) "14px"
    "12px"))

(defn- coords-for [name]
  (cond
    (= 1 (count name)) {:x "9" :y "31"}
    :else {:x (str (- 12 (* 2 (count name))))
           :y "24"}))

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

(defmulti -label
  (fn [name]
    (cond
      (str/starts-with? name "F") :f-key
      :else :default)))

(defmethod -label :default [name]
  (raw-label (str/capitalize name) (font-size-for name) (coords-for name)))

(defmethod -label :f-key [name]
  (raw-label name "18px" {:x "6" :y "26"}))

(defn label [name]
  (-label name))
