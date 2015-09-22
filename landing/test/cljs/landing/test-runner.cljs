(ns landing.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [landing.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'landing.core-test))
    0
    1))
