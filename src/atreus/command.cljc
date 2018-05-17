(ns atreus.command
  (:require [clojure.spec.alpha :as s]))

(s/def ::character char?)

(s/def ::arrow #{"up" "down" "left" "right"})

(s/def ::function #{"f1" "f2" "f3" "f4" "f5" "f6"
                    "f7" "f8" "f9" "f10" "f11" "f12"})

(s/def ::space #{" "})

(s/def ::modifier #{:lshift :rshift
                    :lctrl :rctrl
                    :lalt :ralt
                    :lgui :rgui
                    :lsuper :rsuper
                    :lcmd :rcmd})

(s/def ::special-characters #{:escape :tab
                              :enter :backspace
                              :insert :delete
                              :f1 :f2 :f3 :f4
                              :f5 :f6 :f7 :f8
                              :f9 :f10 :f11 :f12
                              :home :end
                              :page-up :page-down
                              :menu :print-screen
                              :pause :break
                              :scroll-lock
                              :num-lock
                              :caps-lock
                              :power :sleep
                              :wake :mute
                              :volume-up :volume-down
                              :next-track :prev-track
                              :stop :play-pause
                              :select :eject})

(s/def ::mouse #{:mouse-up :mouse-down
                 :mouse-left :mouse-right
                 :mouse-btn-1 :mouse-btn-2
                 :mouse-btn-3 :mouse-btn-4
                 :mouse-btn-5
                 :mouse-wheel-up :mouse-wheel-down
                 :mouse-wheel-left :mouse-wheel-right
                 :mouse-accel-0 :mouse-accel-1
                 :mouse-accel-2})

(s/def ::composite (s/cat :character ::character
                          :modifiers (s/+ ::modifier)))

(s/def :action/index integer?)

(s/def :action/type #{:fn :layer/momentary :layer/on :layer/off})

(s/def :fn/name string?)

(s/def :layer/index integer?)

(s/def ::action (s/keys :req #{:action/type}
                        :opt #{:action/index :fn/name :layer/index}))

(s/def :atreus/command (s/or :modifier            ::modifier
                             :arrow               ::arrow
                             :function            ::function
                             :space               ::space
                             :special-characters  ::special-characters
                             :mouse               ::mouse
                             :composite           ::composite
                             :action              ::action
                             ;; character is last because it's too broad
                             :character           ::character))

(defn command? [c]
  (s/valid? :atreus/command c))
