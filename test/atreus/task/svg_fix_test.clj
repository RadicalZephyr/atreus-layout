(ns atreus.task.svg-fix-test
  (:require [atreus.task.svg-fix :as sut]
            [clojure.test :refer :all]))

(deftest test-things
  (is (= [[20 20]]
         (sut/adjust-all-positions [[150 200]])))
  (is (= [[20 20] [30 25]]
         (sut/adjust-all-positions [[150 200] [160 205]])))

  (is (= [[20 20] [30 25] [40 40]]
         (sut/adjust-all-positions [[150 200] [160 205] [170 220]]))))
