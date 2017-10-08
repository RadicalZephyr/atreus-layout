(ns atreus.compiler-test
  (:require [clojure.test :refer :all]
            [atreus.compiler :as sut]))

(deftest test-compiler
  (is (= "" (sut/compile [[{}]]))))
