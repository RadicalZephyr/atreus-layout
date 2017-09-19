(ns atreus.task.time
  (:import java.time.Instant
           java.time.format.DateTimeFormatter))

(defn now []
  (.format DateTimeFormatter/ISO_INSTANT
           (Instant/now)))
