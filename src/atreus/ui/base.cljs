(ns atreus.ui.base)

(def char-names
  {"<" "lt"
   ">" "gt"
   "'" "tick"
   "\"" "quote"
   "&" "amp"})

(defn label [name]
  [:svg.label
   [:use {"xlinkHref" (str "/img/key-sprites.svg#label_" (get char-names name name))}]])
