(ns landing.components.checkbox
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [landing.components.helpers :refer [display handle-switch]]
            [clojure.string :refer [blank?]]))

(defn selected? [cond]
  (if cond "selected" ""))

(defn mxr-checkbox [data owner {:keys [bool-key label] :as opts}]
  (reify
    om/IRender
    (render [_]
          (dom/div #js {:className "check-container"
                        :onClick #(handle-switch data bool-key owner)}
                   (dom/div   #js {:className "check-border"}
                              (dom/img #js {:className (selected? (not (bool-key data)))
                                            :src "/svg/icons/check.svg"
                                            :width 16
                                            :height 16} nil))
                   ;(dom/div #js {:className (selected? (not (bool-key data)))} nil)
                   (dom/label #js {:style (display (not (blank? label)))} label)))))
