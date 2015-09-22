(ns landing.components.switch
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [landing.components.helpers :refer [handle-switch]]))


(defn active-class? [is?]
  (if is? " active" ""))

(defn mxr-switch [data owner {:keys [bool-key] :as opts}]
  (reify
    om/IRender
    (render[_]
      (dom/div #js {:className "switch-container"
                    :onClick #(handle-switch data bool-key owner)}
               (dom/div #js {:className "track"} nil)
               (dom/div #js {:className (str "head" (active-class? (bool-key data)))} nil)))))
