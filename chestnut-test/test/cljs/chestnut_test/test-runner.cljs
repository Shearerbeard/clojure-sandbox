(ns chestnut_test.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [chestnut_test.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'chestnut_test.core-test))
    0
    1))
