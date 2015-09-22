(ns reagent-pexeso.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))
;;
;; State
(def board-size 16)
(def alpha-chars (mapv char (range 97 123)))
(defn generate-symbols []
  (shuffle (flatten (repeat 2 (take (/ board-size 2) (shuffle alpha-chars))))))
(def state (atom {:cards [] :last-symbol nil}))
(defn generate-card [symb]
  (atom {:symbol symb
         :visible false
         :matched false}))

(defn generate-cards []
  (mapv generate-card (generate-symbols)))

(defn matched? [card]
  (:matched @card))

(defn visible? [card]
  (:visible @card))

(defn revealed-cards-count []
  (count (filter visible? (:cards @state))))

(defn matched-cards-count []
  (count (filter matched? (:cards @state))))

(defn hide-nonmatch! []
  (doseq [card-state (:cards @state)]
    (swap! card-state assoc :visible false))
  (swap! state assoc :last-symbol ""))

(defn mark-match! [symbol]
  (doseq [matched-card (filterv #(= (:symbol @%) symbol)
                                (:cards @state))]
    (swap! matched-card assoc :matched true)))

(defn reveal-card! [card-state]
  (swap! card-state assoc :visible true))

(defn start-game []
  (swap! state assoc :cards (generate-cards)))


;; -------------------------
;; Views

(defn card [card-state]
  (letfn [(handle-card-click! [event]
                              (if (= (revealed-cards-count) 2)
                                (hide-nonmatch!))
                              (reveal-card! card-state)

                              (if (and (= (revealed-cards-count) 2)
                                       (= (:last-symbol @state)
                                          (:symbol @card-state)))
                                (mark-match! (:symbol @card-state)))

                              (swap! state assoc :last-symbol (:symbol @card-state)))]
         [:div.card
          {:onClick handle-card-click!
           :key (.random js/Math)
           :class (if (@card-state :matched)
                    "card-matched"
                    "card")}
          [:span.card.value
           {:class (if (@card-state :visible)
                     "card-value"
                     "card-value-hidden")}
           (:symbol @card-state)]]))

(defn home-page []
  [:div.pexeso
    [:h2 "Pexeso"]
    [:div.status (if (= (matched-cards-count) board-size)
                   "Game is finished, congratulations!"
                   )]
   [:br]

   [:button.button {:onClick start-game} "Restart Game" ]

   [:div.board
      (doall (for [card-state (@state :cards)] (card card-state)))]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
