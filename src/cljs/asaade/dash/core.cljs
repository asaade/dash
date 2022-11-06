(ns asaade.dash.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]
   [cljs.pprint :as pprint]))

;; -------------------------
;; Views

(defn calc-bmi [{:keys [height weight bmi] :as data}]
  (let [h (/ height 100)]
    (if (nil? bmi)
      (assoc data :bmi (/ weight (* h h)))
      (assoc data :weight (* bmi h h)))))


(defn slider [param value min max invalidates bmi-data]
  [:input {:type "range" :value value :min min :max max
           :style {:width "100%"}
           :id (name param)
           :on-change (fn [e]
                        (let [new-value (js/parseInt (.. e -target -value))]
                          (swap! bmi-data
                                 (fn [data]
                                   (-> data
                                       (assoc param new-value)
                                       (dissoc invalidates)
                                       calc-bmi)))))}])


(defn bmi-component []
  (let [bmi-data (r/atom (calc-bmi {:height 180 :weight 80}))]
    (fn []
      (let [{:keys [weight height bmi]} @bmi-data
            [color diagnose] (cond
                               (< bmi 18.5) ["orange"  "Underweight"]
                               (< bmi 25)   ["inherit" "Normal"]
                               (< bmi 30)   ["orange"  "Overweight"]
                               :else        ["red"     "Obese"])]
        [:div.container
         [:h3 "BMI calculator"]
         [:div
          [:div
           [:label {:for "height"} (str "Height: " (int height) "cm")]
           [slider :height height 100 220 :bmi bmi-data]]
          [:div
           [:label {:for "weight"} (str "Weight: " (int weight) "kg")]
           [slider :weight weight 30 150 :bmi bmi-data]]
          [:div
           [:label {:for "bmi"} (str "BMI: " (int bmi) " ")]
           [slider :bmi bmi 10 50 :weight bmi-data]]
          [:div
           [:span {:style {:color color}} [:strong diagnose]]
           [:span.float-right [:strong (pprint/cl-format nil "~,1f" bmi)]]]]]))))


(defn app []
  [bmi-component])

;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-root []
  (d/render [app] (.getElementById js/document "app")))


(defn ^:export ^:dev/once init! []
  (mount-root))
