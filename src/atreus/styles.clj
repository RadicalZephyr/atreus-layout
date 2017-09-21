(ns atreus.styles
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [rule]]
            [garden.units :as u]))

(defstyles screen
  [:* {:box-sizing "border-box"}]

  [:body {:font-family "Helvetica"}]
  [:svg.label {:width "40px" :height "40px"}]
  [:#com-rigsomelight-devcards-main
   [:svg.label {:border "solid black 1px"
                :margin "5px"}]]
  [:.test {:background-color "tomato"}])
