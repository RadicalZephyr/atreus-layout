(ns atreus.ui.layer
  (:require [re-frame.core :as re-frame]
            [clairvoyant.core :refer-macros [trace-forms]]
            [re-frame-tracer.core :refer [tracer]]
            [clojure.spec.alpha :as s]
            [atreus.command]
            [atreus.ui.base :as base]))

(defn empty-layout []
  [[{}]])

(s/def ::index (s/int-in 0 42))
(s/def ::layer (s/map-of ::index :atreus/command
                         :min-count 1
                         :max-count 42))
(s/def :atreus/layout (s/coll-of ::layer
                                 :kind vector?
                                 :min-count 1
                                 :max-count 32))

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

(defn- styles-for [[x y] side]
  {:top (+ 6 y)
   :left (if (= side :right)
           (- x 48)
           (- x 2))})

(defn- get-current-layer [layout layer-index binding-index]
  (get-in layout [layer-index binding-index]))

(defn- area [click-handler bindings index x-y side]
  [:div
   (when (contains? bindings index)
     [:span.layer-label {:style (styles-for x-y side)}
      [base/label (get bindings index) side]])
   [:area {:onClick (fn [e]
                      (.preventDefault e)
                      (click-handler index side))
           :tabIndex index
           :shape "poly"
           :coords (coords x-y side (deltas :square))}]])

(defn- row [click-handler bindings row-index x-y side]
  (into [:div]
        (map-indexed (fn [column-index column-x-y]
                       [area click-handler
                             bindings
                             (index side row-index column-index)
                             column-x-y
                             side])
                     (coords x-y side (deltas :row)))))

(defn- stack [click-handler bindings x-y side]
  (into [:div]
        (map-indexed (fn [row-index row-x-y]
                       [row click-handler
                            bindings
                            row-index
                            row-x-y
                            side])
                     (coords x-y side (deltas :stack)))))

(defn layer-background [click-handler bindings]
  (let [layer-id (gensym "layer")]
    [:div.layer-root
     [:map {:name layer-id}
      [stack click-handler bindings [46,7] :left]

      [area click-handler bindings 35 [349.5,240.25] :left]
      [area click-handler bindings 36 [473.5,240] :right]

      [stack click-handler bindings [777,6] :right]]
     [:img {:useMap (str "#" layer-id)
            :src "/img/layer-blank.svg"}]]))
