(ns atreus.task.svg-fix-test
  (:require [atreus.task.svg-fix :as sut]
            [clojure.test :refer :all]
            [clojure.string :as str]))

(deftest test-adjust-all-positions
  (is (= [[20 20]]
         (sut/adjust-all-positions [[150 200]])))
  (is (= [[20 20] [30 25]]
         (sut/adjust-all-positions [[150 200] [160 205]])))

  (is (= [[20 20] [30 25] [40 40]]
         (sut/adjust-all-positions [[150 200] [160 205] [170 220]]))))

(deftest test-read-positions
  (is (= [[20 20]]
         (sut/read-positions "d=\"m 20,20\"")))
  (is (= [[30 20]]
         (sut/read-positions "d=\"m 30,20\"")))
  (is (= [[20 20] [30 20]]
         (sut/read-positions (str/join "\n" ["d=\"m 20,20\""
                                             "d=\"m 30,20\""]))))
  (is (= [[20 20] [30 20]]
         (sut/read-positions (str/join "\ngarbage and random stuff\n"
                                       ["d=\"m 20,20\""
                                        "d=\"m 30,20\""])))))
