(ns atreus.ui.base)

(defn button [text handler]
  [:a.button {:on-click #(handler)} text])

(defn button-panel [config]
  [:div.row
   [:div.column.fourth
    [button "Button A" identity]
    [button "Button B" identity]]

   [:div.column.fourth
    [button "Button C" identity]
    [button "Button D" identity]]

   [:div.column.fourth
    [button "Button E" identity]
    [button "Button F" identity]]

   [:div.column.fourth
    [button "Button G" identity]
    [button "Button H" identity]]])
