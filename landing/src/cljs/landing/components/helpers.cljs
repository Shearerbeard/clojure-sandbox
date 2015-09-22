(ns landing.components.helpers
  (:require [om.core :as om :include-macros true]))

(defn handle-change [e data value-key owner]
  (om/transact! data value-key (fn [_] (.. e -target -value))))

(defn handle-switch [data bool-key owner]
  (om/transact! data bool-key (fn [_] (not (bool-key data)))))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))
