(ns atreus.compiler-test
  (:require [clojure.test :refer :all]
            [atreus.compiler :as sut]))


(deftest test-binding->key-symbol
  (is (= "KC_A" (sut/binding->key-symbol \a)))
  (is (= "KC_B" (sut/binding->key-symbol \b)))
  (is (= "KC_LSHIFT" (sut/binding->key-symbol :lshift))))

(deftest test-compiler
  (is (= "" (sut/compile [[{}]]))))
