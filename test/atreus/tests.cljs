(ns atreus.tests
  (:require-macros [cljs.test :refer [deftest is testing]])
  (:require [cljs.test :as t]
            [atreus.app :as app]))

(deftest test-tests []
  (is (= 1 1))
  (is (= 2 3)))
