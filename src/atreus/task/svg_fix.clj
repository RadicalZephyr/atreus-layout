(ns atreus.task.svg-fix
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn adjust-all-positions [positions]
  (let [delta (mapv #(- % 20) (first positions))]
    (mapv #(mapv - % delta) positions)))
