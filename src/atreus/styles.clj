(ns atreus.styles
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [rule]]
            [garden.units :as u]))

(defstyles screen
  [:* {:box-sizing "border-box"}]

  [:body {:font-family "Helvetica"}]

  [:.test {:background-color "tomato"}])
