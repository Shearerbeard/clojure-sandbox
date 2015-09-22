(ns landing.components.input
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [landing.components.helpers :refer [handle-change display]]
            [clojure.string :refer [blank?]]))

(defn handle-blur [e owner cb]
  (let [text (.. e -target -value)]
    (om/set-state! owner :selected
                   (not (blank? (.. e -target -value))))
    (if cb (cb text))))

(defn set-selected [owner]
  (om/set-state! owner :selected true))

(defn input-class [selected state]
  (str (if (and selected (not= state "disabled"))
         "input-container selected " "input-container ") state))

(defn selected-on-init [value-key data]
  {:selected (not (blank? (value-key data)))})

(defn mxr-input [data owner {:keys [value-key label component-state cb]
                             :as opts}]
  (reify
    om/IInitState
    (init-state [_] (selected-on-init value-key data))
    om/IRenderState
    (render-state [_ state]
      (let [text (get data value-key)]
        (dom/div #js {:className (input-class (:selected state) text)}
                 (dom/label nil label)
                 (dom/input #js {:type     "text"
                                 :style    (display (not= component-state "disabled"))
                                 :onFocus  #(set-selected owner)
                                 :onBlur   #(handle-blur % owner cb)
                                 :value    text
                                 :onChange #(handle-change % data value-key owner)}))))))
