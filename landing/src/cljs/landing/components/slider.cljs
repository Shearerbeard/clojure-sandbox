(ns landing.components.slider
  (:require [om.core :as om]
            [om.dom :as dom]))

(defn calc-transform [val]
  (let [perc (.toString (* (- 1.0 val) 100.0))]
    (str "translate3d(-" perc "%, 0, 0)")))

(defn find-offest [node]
  (.. node getBoundingClientRect -left))

(defn find-width [node]
  (.. node getBoundingClientRect -width))

(defn toggle-transition [owner]
  (om/set-state! owner :transition true)
  (js/setTimeout (fn [] (om/set-state! owner :transition false)) 300))

(defn calc-value [pageX owner]
  (let [node (om/get-node owner "container")
        pxLeft (find-offest node)
        width (find-width node)
        click-pos (- pageX pxLeft)]
    (/ click-pos width)))

(defn handle-click [pageX owner data value-key]
  (toggle-transition owner)
  (om/transact! data value-key (fn [_] (calc-value pageX owner)))
  (om/set-state! owner :sliding true))

(defn head-class [state]
  (str "head-wrap" (if (:transition state) " transition" "")))

(defn mxr-slider [data owner {:keys [value-key] :as opts}]
  (reify
    om/IInitState
    (init-state [_] {:sliding false
                     :transition false})
    om/IRenderState
    (render-state [_ state]
      (dom/div #js {:className   "slider-container"
                    :ref         "container"
                    :onMouseDown #(handle-click (.-pageX %) owner data value-key)
                    :onDrag #(om/transact! data value-key (fn [_] (calc-value (.-pageX %) owner)))}
               (dom/div #js {:className (head-class state)
                             :style     #js {:transform (calc-transform (value-key data))}}
                        (dom/div #js {:className "head"}))
               (dom/div #js {:className   "track"}
                        (dom/div #js {:className "fill"
                                      :style     #js {:width (str (.toString (* (value-key data) 100)) "%")}}))))))