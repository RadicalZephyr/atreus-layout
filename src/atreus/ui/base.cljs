(ns atreus.ui.base
  (:require [clojure.string :as str]))

(def char-names
  {"<" "lt"
   ">" "gt"
   "'" "tick"
   "\"" "quote"
   "&" "amp"})

(defn- font-size-for [name]
  (case (count name)
    1 "40px"
    2 "26px"
    "15px"))

(defn- coords-for [name]
  (case (count name)
    1 {:x "9" :y "30"}
    2 {:x "8" :y "30"}
    3 {:x "8" :y "24"}
    {:x "9" :y "30"}))

(defn label [name]
  [:svg.label
   [:text {:style {:font-size (font-size-for name)
                   :line-height "125%"
                   :font-family "Anonymous Pro"
                   :letter-spacing "0px"
                   :fill "#000000"
                   :fill-opacity "1"
                   :stroke "none"}}
    [:tspan (coords-for name)
     (str/capitalize name)]]])
