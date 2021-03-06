(ns atreus.ui.base
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [reagent.core :as reagent]))

(defn- font-size-for [name]
  (case (count name)
    (2 3 4) "14px"
    "12px"))


(def ^:private
  transforms
  {:left "rotate(9.99999 20 20)"
   :right "rotate(-9.99999 20 20)"})

(defn- transform-for [rotation]
  (get transforms rotation ""))

(defn- raw-label [name rotation font-size coords]
  [:svg.label
   [:text {:transform (transform-for rotation)
           :text-anchor "middle"
           :style {:font-size font-size
                   :line-height "125%"
                   :font-family "Anonymous Pro"
                   :letter-spacing "0px"
                   :fill "#000000"
                   :fill-opacity "1"
                   :stroke "none"}}
    [:tspan coords name]]])

(def ^:private
  arrow-names
  #{"left" "right" "up" "down"})

(defn- arrow-key? [name]
  (contains? arrow-names name))

(defmulti -label
  (fn [name _rotation]
    (cond
      (= 1 (count name)) :symbol-key
      (arrow-key? name) :arrow-key
      (re-matches #"[fF]\d+" name) :f-key
      :else :default)))

(defmethod -label :default [name rotation]
  (raw-label name rotation (font-size-for name) {:x "20" :y "24"}))

(defmethod -label :symbol-key [name rotation]
  (raw-label (str/capitalize name) rotation "40px" {:x "20" :y "30"}))

(defmethod -label :f-key [name rotation]
  (raw-label name rotation "18px" {:x "20" :y "26"}))

(defmethod -label :arrow-key [name rotation]
  [:svg.label
   [:use {:transform (transform-for rotation)
          :xlinkHref (str "/img/key-sprites.svg#label_" name)}]])

(defmulti -render-key
  (fn [binding]
    (let [res (s/conform :atreus/command binding)]
      (when-not (s/invalid? res)
        (first res)))))

(defmethod -render-key :default
  [binding]
  binding)

(defmethod -render-key :modifier
  [binding]
  (subs (name binding) 1))

(defmethod -render-key :space
  [_]
  "space")

(defmethod -render-key :special-characters
  [binding]
  (name binding))

(defmethod -render-key :action
  [binding]
  (case (:action/type binding)
    :fn (str "F" (:fn/name binding))
    :layer/momentary (str "LM" (:fn/name binding) (inc (:layer/index binding)))
    :layer/off (str "LOFF" (inc (:layer/index binding)))
    :layer/on (str "LON" (inc (:layer/index binding)))))

(def ^:private
  modifiers
  {:lshift "s"
   :rshift "s"
   :lctrl "C"
   :rctrl "C"
   :lalt "M"
   :ralt "M"
   :lgui "S"
   :rgui "S"
   :lsuper "u"
   :rsuper "u"
   :lcmd "c"
   :rcmd "c"})

(defmethod -render-key :composite
  [binding]
  (reduce #(str (modifiers %2) "-" %1)
          (first binding)
          (rest binding)))

(defn render-key [binding]
  (-render-key binding))

(defn label
  ([binding]
   (label binding :rotation/none))
  ([binding rotation]
   (-label (render-key binding) rotation)))

(defn register-dom-event [target event-name on-key-fn & content]
  (reagent/create-class
   {:component-will-mount
    (fn []
      (.addEventListener target event-name on-key-fn))
    :component-will-unmount
    (fn []
      (.removeEventListener target event-name on-key-fn))
    :reagent-render
    (fn []
      (into [:div] content))}))
