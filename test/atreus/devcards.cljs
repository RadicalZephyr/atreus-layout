(ns atreus.devcards
  (:require [devcards.core :as dc :include-macros true]
            [atreus.ui.base-test]
            [atreus.ui.modal-test]
            [atreus.ui.layer-test]))

(defn run-devcards! []
  (dc/start-devcard-ui!))
