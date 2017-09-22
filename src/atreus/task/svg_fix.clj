(ns atreus.task.svg-fix
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn adjust-all-positions [positions]
  (let [delta (mapv #(- % 20) (first positions))]
    (mapv #(mapv - % delta) positions)))

(def pos-re #"d=\"m ([0-9.]+),([0-9.]+)")

(defn read-int [s]
  (Integer/parseInt s))

(defn read-positions [text]
  (->> (re-seq pos-re text)
       (mapv #(->> %
                   (drop 1)
                   (mapv read-int)))))
