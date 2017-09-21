(ns atreus.ui.base)

(defn label [name]
  [:svg.label
   [:use {"xlinkHref" (str "/img/key-sprites.svg#label_" name)}]])
