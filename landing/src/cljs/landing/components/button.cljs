(ns landing.components.button
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn mxr-button [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:hovered false})
    om/IRenderState
    (render-state [this state]
      (dom/div #js {:className (str "button-container " (:state data))
                    :onClick   (:action data)
                    :onMouseOver (fn [_] (om/set-state! owner :hovered true))
                    :onMouseOut (fn [_] (om/set-state! owner :hovered false))}
               (dom/p nil (:title data))))))
