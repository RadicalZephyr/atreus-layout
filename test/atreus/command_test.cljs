(ns atreus.command-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [atreus.command :as sut]))

(deftest test-commands
  (is (sut/command? \a))
  (is (sut/command? "a"))
  (is (not (sut/command? "aa")))
  (is (sut/command? :lshift))
  (is (sut/command? :esc))
  (is (sut/command? :mute))
  (is (sut/command? :mouse-up))
  (is (sut/command? [\a :lshift]))
  (is (sut/command? [\a :lshift :rctrl])))
