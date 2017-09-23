(ns atreus.ui.base
  (:require [clojure.string :as str]))

(defn- font-size-for [name]
  (case (count name)
    (2 3 4) "14px"
    "12px"))


(def ^:private
  transforms
  #:rotation
  {:left "rotate(-9.99999 20 20)"
   :right "rotate(9.99999 20 20)"})

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

(defn label
  ([name]
   (-label name :rotation/none))
  ([name rotation]
   (-label name rotation)))

(def ^:private
  deltas
  {:square [[0 0] [56 10] [47 61] [-9 51.5]]
   :row [[0 0] [68 0.5] [138 -5.25] [201 24] [264 57]]
   :stack [[0 0] [-12 66.25] [-23.75 132.75] [-35.5 199]]})

(def ^:private
  op-for-side
  {:left +
   :right -})

(defn- coords [[x-root y-root] side deltas]
  (let [op (op-for-side side)]
   (mapv (fn [[x y]]
           [(op x-root x)
            (+ y-root y)])
         deltas)))

(defn index [side row-index col-index]
  (case side
    :left (+ col-index (* 10 row-index))
    :right (- (* 10 (inc row-index))
              (inc col-index)
              (if (= 3 row-index) -2 0))))

(defn- area [click-handler index x-y side]
  [:area {:onClick (fn [e]
                     (.preventDefault e)
                     (click-handler index side))
          :tabIndex index
          :shape "poly"
          :coords (coords x-y side (deltas :square))}])

(defn- row [click-handler row-index x-y side]
  (into [:div]
        (map-indexed #(area click-handler (index side row-index %1) %2 side)
                     (coords x-y side (deltas :row)))))

(defn- stack [click-handler x-y side]
  (into [:div]
        (map-indexed #(row click-handler %1 %2 side)
                     (coords x-y side (deltas :stack)))))

(defn layout-background [click-handler]
  [:div
   [:map {:name "layout"}
    [stack click-handler [46,7] :left]

    [area click-handler 35 [349.5,240.25] :left]
    [area click-handler 36 [473.5,240] :right]

    [stack click-handler [777,6] :right]]
   [:img {:useMap "#layout"
          :src "/img/layout-blank.svg"}]])
