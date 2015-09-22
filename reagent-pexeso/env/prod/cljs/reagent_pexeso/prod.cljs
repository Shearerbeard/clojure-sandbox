(ns reagent-pexeso.prod
  (:require [reagent-pexeso.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
