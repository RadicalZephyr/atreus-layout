(ns atreus.styles
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :as s]
            [garden.units :as u]))

(defstyles screen
  [:* {:box-sizing "border-box"}]

  [:body {:font-family "Helvetica"}]
  [:#layout-root {:position "relative"
                  :z-index 1}]
  [:area {:cursor "pointer"}]
  [:.layout-label {:position "absolute"
                   :z-index -1}]
  [:svg.label {:width "40px"
               :height "40px"}]
  [:#com-rigsomelight-devcards-main
   [:svg.label {:border "solid black 1px"
                :margin "5px"}]]
  [:.test {:background-color "tomato"}]
  (s/at-font-face
   {:font-family "Anonymous Pro"
    :src "url(\"/fonts/anonymous-pro.ttf\")"}))
