(ns atreus.task.index
  (:require [boot.core :as core]
            [boot.pod :as pod]
            [boot.util :as util]
            [clojure.java.io :as io]))

(core/deftask write-index []
  (let [tmp (core/tmp-dir!)]
    (core/with-pre-wrap fileset
      (core/empty-dir! tmp)
      (let [pod (pod/make-pod (core/get-env))
            index-file (io/file tmp "public/index.html")]
        (io/make-parents index-file)
        (util/info "Writing index.html...\n")
        (pod/with-eval-in pod
          (require 'atreus.core)
          (spit ~(str index-file) (atreus.layout/main-page)))
        (pod/destroy-pod pod)
        (-> fileset
            (core/add-resource tmp)
            core/commit!)))))
