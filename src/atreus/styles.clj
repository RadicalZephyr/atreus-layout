(ns atreus.styles
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :as s]
            [garden.units :as u]))

(defstyles screen
  [:* {:box-sizing "border-box"}]

  [:body {:font-family "Helvetica"}]

  [:#layer-root {:position "relative"
                  :z-index 1}]

  [:area {:cursor "pointer"}]

  [:.layer-label {:position "absolute"
                   :z-index -1}]

  [:svg.label {:width "40px"
               :height "40px"}]

  [:#com-rigsomelight-devcards-main
   [:svg.label {:border "solid black 1px"
                :margin "5px"}]]

  [:.test {:background-color "tomato"}]

  (s/at-font-face
   {:font-family "Anonymous Pro"
    :src "url(\"/fonts/anonymous-pro.ttf\")"})

  [:.uber-container
   {:position "fixed"
    :width "100%"
    :height "100%"
    :z-index 2}]

  [:.scotch-overlay
   {:transition "1ms opacity ease"
    :top "0"
    :width "100%"
    :background "rgba(0,0,0,.6)"
    :z-index "9998"
    :opacity "0"
    :position "absolute"
    :-o-transition "1ms opacity ease"
    :-moz-transition "1ms opacity ease"
    :-webkit-transition "1ms opacity ease"
    :height "100%"
    :left "0"
    :-ms-transition "1ms opacity ease"}]

  [:.scotch-modal
   {:transform "translate(-50%, -50%)"
    :transition "1ms opacity ease"
    :top "50%"
    :width "94%"
    :background "#fff"
    :-o-transform "translate(-50%, -50%)"
    :-moz-transform "translate(-50%, -50%)"
    :z-index "9999"
    :opacity "0"
    :padding "24px 20px"
    :-webkit-transform "translate(-50%, -50%)"
    :position "absolute"
    :-o-transition "1ms opacity ease"
    :-moz-transition "1ms opacity ease"
    :border-radius "2px"
    :-webkit-transition "1ms opacity ease"
    :-ms-transform "translate(-50%, -50%)"
    :left "50%"
    :-ms-transition "1ms opacity ease"}]

  [:.scotch-modal.scotch-open.scotch-anchored
   {:top "20px"
    :-webkit-transform "translate(-50%, 0)"
    :-moz-transform "translate(-50%, 0)"
    :-ms-transform "translate(-50%, 0)"
    :-o-transform "translate(-50%, 0)"
    :transform "translate(-50%, 0)"}]

  [:.scotch-modal.scotch-open {:opacity "1"}]

  [:.scotch-overlay.scotch-open {:opacity "1"}]

  [:.scotch-close
   {:line-height "12px"
    :color "#fff"
    :font-size "24px"
    :top "5px"
    :font-weight "700"
    :background "#e74c3c"
    :cursor "pointer"
    :padding "5px 7px 7px"
    :outline "none"
    :right "5px"
    :position "absolute"
    :border "0"
    :font-family "Helvetica,Arial,sans-serif"}]

  [:.scotch-close:hover {:background "#c0392b"}]

  [:.scotch-overlay.fade-and-drop {:display "block", :opacity "0"}]

  [:.scotch-modal.fade-and-drop
   {:top "-300%", :opacity "1", :display "block"}]

  [:.scotch-modal.fade-and-drop.scotch-open
   {:top "50%"
    :-webkit-transition "500ms top 500ms ease"
    :-moz-transition "500ms top 500ms ease"
    :-ms-transition "500ms top 500ms ease"
    :-o-transition "500ms top 500ms ease"
    :transition "500ms top 500ms ease"}]

  [:.scotch-modal.fade-and-drop.scotch-open.scotch-anchored
   {:-webkit-transition "500ms top 500ms ease"
    :-moz-transition "500ms top 500ms ease"
    :-ms-transition "500ms top 500ms ease"
    :-o-transition "500ms top 500ms ease"
    :transition "500ms top 500ms ease"}]

  [:.scotch-overlay.fade-and-drop.scotch-open
   {:top "0"
    :-webkit-transition "500ms opacity ease"
    :-moz-transition "500ms opacity ease"
    :-ms-transition "500ms opacity ease"
    :-o-transition "500ms opacity ease"
    :transition "500ms opacity ease"
    :opacity "1"}]

  [:.scotch-modal.fade-and-drop
   {:-webkit-transition "500ms top ease"
    :-moz-transition "500ms top ease"
    :-ms-transition "500ms top ease"
    :-o-transition "500ms top ease"
    :transition "500ms top ease"}]

  [:.scotch-overlay.fade-and-drop
   {:-webkit-transition "500ms opacity 500ms ease"
    :-moz-transition "500ms opacity 500ms ease"
    :-ms-transition "500ms opacity 500ms ease"
    :-o-transition "500ms opacity 500ms ease"
    :transition "500ms opacity 500ms ease"}]

  [:html {:box-sizing "border-box"}]

  [:* {:box-sizing "inherit"}]

  [:body
   {:font-family "Helvetica,Arial,san-serif"
    :font-size "16px"
    :margin "0"
    :padding "0"}]

  [:#content {:display "none"}]

  [:.trigger-button
   {:box-shadow "0 5px 1px #27ae60"
    :color "#fff"
    :font-size "17px"
    :top "100px"
    :background "#2ecc71"
    :cursor "pointer"
    :padding "10px 30px"
    :outline "none"
    :display "block"
    :position "relative"
    :border "0"
    :border-radius "3px"
    :margin "auto"}]

  [:.trigger-button:hover
   {:background "#27ae60", :box-shadow "0 5px 1px #145b32"}]

  [:.trigger-button:focus
   {:border-top "5px solid white", :box-shadow "none"}])
