(ns landing.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omdev.core :as omdev]
            [landing.components.button :refer [mxr-button]]
            [landing.components.input :refer [mxr-input]]
            [landing.components.checkbox :refer [mxr-checkbox]]
            [landing.components.switch :refer [mxr-switch]]
            [landing.components.slider :refer [mxr-slider]]))

(enable-console-print!)

(defonce app-state (atom {:submit-text "SUBMIT"
                          :submit-state ""
                          :name ""
                          :name-input-state ""
                          :email-address ""
                          :email-input-state ""
                          :terms-of-service? false
                          :heading-ln1 "Plug Into Limitless"
                          :heading-ln2 "Musical Collaboration"
                          :sub-heading-ln1 "WavMxr is almost ready."
                          :sub-heading-ln2 "Want a Beta Alert?"
                          :t&c-agreement "I have read and accept the terms and conditions"
                          :slider-value 0.5
                          :switch-on? false
                          :learning-more? false
                          :copy {:create "Just plug in your instrument or mic and start recording, or upload your existing work. Mix and edit individual tracks until your songs are perfect – no additional software needed. You can also store multiple versions in one secure place."
                                 :collaborate "Start a band or collaborate on a song online as easily as if you were in the same room. WAVMXR is designed for real-time co-creation of high-caliber music. It’s the only online musical collaboration tool that requires nothing but a browser."
                                 :unleash "Need some feedback? Share your music with the entire WAVMXR community of musicians or just a select group. When you’re ready, grow your fan base by making your work available to the public for browsing and discovery."}
                          :dismiss-arrow? false}))

(defn toggle-dismiss [app owner]
  (om/transact! app :dismiss-arrow? (fn [_] (not (:dismiss-arrow? app))))
  (.setTimeout js/window (fn [_] (om/transact! app :learning-more? (fn [_] (not (:learning-more? app))))) 10))

(defn login-form [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id "wrapper"}
               (dom/img #js {:src "/svg/icons/WavMxr-circle-white.svg"
                             :width 75
                             :style #js {:position "fixed"
                                         :top "20px"
                                         :left "20px"}})
               (dom/div #js {:className "wrap"}
                        (dom/h2 nil (:heading-ln1 app) (dom/br nil) (:heading-ln2 app))
                        (dom/h3 nil (:sub-heading-ln1 app) (dom/br nil) (:sub-heading-ln2 app))
                        (dom/div #js {:id "form-container"}
                                 (om/build mxr-input app
                                           {:opts {:value-key :name
                                                   :component-state (:name-input-state app)
                                                   :label "NAME"}})
                                 (om/build mxr-input app
                                           {:opts {:value-key :email-address
                                                   :component-state (:email-input-state app)
                                                   :label "EMAIL"}})
                                 (om/build mxr-checkbox app
                                           {:opts {:bool-key :terms-of-service?
                                                   :label (:t&c-agreement app)}})
                                 (om/build mxr-button {:title  (:submit-text app)
                                                       :state  (:state app)
                                                       :action (fn [e] (js/alert "test"))})))
               (dom/img #js {:src       "/svg/icons/WavMxr-arrow-white.svg"
                             :width     40
                             :id        "pulsing-arrow"
                             :className (cond (:learning-more? app)
                                              "dismissed"
                                              (:dismiss-arrow? app)
                                              ""
                                              :else "animated")
                             :onClick   #(toggle-dismiss app owner)
                             :style     #js {:position "fixed"
                                             :bottom   0
                                             :left     0
                                             :right    0
                                             :margin   "0 auto 60px auto"}})
               (dom/div #js {:id "learn-more"
                             :onClick   #(toggle-dismiss app owner)
                             :className (if (not (:learning-more? app)) "dismissed" "")}
                        (dom/div #js {:id "ccu-wrap"}
                                 (dom/div #js {:id "ccu-header"}
                                          (dom/h2 nil "CREATE MUSIC :: COLLABORATE WITH OTHERS :: UNLEASH YOUR SOUND")
                                          (dom/div #js {:id "divider"} nil))
                                 (dom/div #js {:id "create"}
                                          (dom/div nil
                                                   (dom/img #js {:src "/img/landing/circle-images-create.jpg"})
                                                   (dom/div #js {:className "txt-container"}
                                                            (dom/h2 nil "CREATE")
                                                            (dom/p nil (:create (:copy app))))))
                                 (dom/div #js {:id "collaborate"}
                                          (dom/div nil
                                                   (dom/div #js {:className "txt-container"}
                                                            (dom/h2 nil "COLLABORATE")
                                                            (dom/p nil (:collaborate (:copy app))))
                                                   (dom/img #js {:src "/img/landing/circle-images-collaborate.jpg"
                                                                 :className "right"})))
                                 (dom/div #js {:id "unleash"} (dom/div nil
                                                   (dom/img #js {:src "/img/landing/circle-images-unleash.jpg"})
                                                   (dom/div #js {:className "txt-container"}
                                                            (dom/h2 nil "UNLEASH")
                                                            (dom/p nil (:unleash (:copy app))))))))))))


(defn main []
  ;(omdev/dev-component login-form app-state {:target(. js/document (getElementById "app"))
  ;                                           :tx-listen (fn [tx-data root-cursor]
  ;                                                        ;(println "listener 1 " tx-data)
  ;                                                        )})
  (om/root login-form app-state {:target (. js/document (getElementById "app"))}))
