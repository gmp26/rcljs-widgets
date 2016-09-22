(ns rcljswidgets.funnel-plots
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.axis :refer [axisBottom axisTop axisLeft axisRight]]
            [svg.scales :refer [->Identity nice-linear i->o o->i in out ticks]]
            [svg.markers :refer [dot]]
            [tests.funnel-data :refer [CABG]]
            ))

(def data CABG)

(defstyle styles
          [[".outer" {:fill   "none"
                      :stroke "#000"}]
           [".inner" {:fill   "#fff"
                      :stroke "none"}]
           [".annotation" {
                           :font-size "10pt"
                           }]
           [".arrow" {
                      :stroke       "#000"
                      :stroke-width "1.5px"
                      }]])


(def error-messages {:no-data "There is no data for at least one hospital"})


(defn make-scales [x]
  "Given a vector of hospital records, derive data to plot and scales"
  (let [n (map :Cases x)
        r (map - n (map :Deaths x))
        p (map - n (map #(/ % 100) (map * (map :EMR x) n)))]
    (if (every? pos? n)
      {:error     nil
       :n         n
       :r         r
       :p         p
       :obs-prop  (map / r n)
       :pred-prop (map / p n)
       :x-range   [0 (apply max n)]
       :y-range   [(apply min (map #(- % 0.01) (map / r n))) 1]}
      {:error "no-data"})))

;; In the original R we have:
;;
;; numer = obs.prop * denom = r/n * n = r
;; denom = n
;; y = numer/denom (and it plots y against denom)
;;
;; so we simply plot obs-prop (alias y) against n (alias denom)

;
;if(riskadj==F){
;  y <- numer/denom  # do something to check no zeros in denominator?
;  if(mean.target == T) {
;    target <- sum(numer[])/sum(denom[])  # scalar
;  }
;  #    ps.and.qs(y,denom,target,names,tails)  #  calculate p and q values
;
;  if(plot=="funnel") {
;    # calculate limits for different precisions - need to be integers!
;    # have to do this outside as may need to transform
;    precisions <- round(xrange[1] + ((1:Npoints) * (xrange[2] - xrange[1]))/Npoints)
;
;    limits= binomial.limits(precisions,rep(target,Npoints),tails)
;
;    # transform limits? Not with basic Binomial
;    plot.funnel(y,denom, precisions, target, plot.target,title, xrange, yrange,
;                tails, limits, xlab, ylab,pointsymbol,  legend, ypercent, bandcols  )
;  }
;}


(defn data-space [outer margin padding data]
  (let [inner {:width  (- (:width outer) (:left margin) (:right margin))
               :height (- (:height outer) (:top margin) (:bottom margin))}
        width (- (:width inner) (:left padding) (:right padding))
        height (- (:height inner) (:top padding) (:bottom padding))
        {:keys [error x-range y-range]} (make-scales data)]
    {:outer   outer
     :inner   inner
     :margin  margin
     :padding padding
     :width   width
     :height  height
     :x       (if error nil (nice-linear x-range [0 width] 10))
     :y       (if error nil (nice-linear y-range [height 0] 10))
     :data    data
     :error   error
     }))


(rum/defc funnel [{:keys [error outer margin inner padding width height x y data]}]
  (let [inner (if (nil? inner) {:width  (- (:width outer) (:left margin) (:right margin))
                                :height (- (:height outer) (:top margin) (:bottom margin))}
                               inner)
        width (if (nil? width) (- (:width inner) (:left padding) (:right padding)) width)
        height (if (nil? height) (- (:height inner) (:top padding) (:bottom padding)) height)
        x-ticks (ticks x)
        y-ticks (ticks y)
        ]

    (if error
      [:h2 (error error-messages)]
      [:svg {:width  (:width outer)
             :height (:height outer)}

       [:g {:key       0
            :transform "translate(20, 20)"}

        [:rect {:key        1
                :class-name (:outer styles)
                :width      (:width inner)
                :height     (:height inner)}]

        ;;
        ;; define the coordinate system
        ;;
        [:g {:key       2
             :transform (str "translate(" (:left padding) "," (:right padding) ")")}
         [:rect {:key        1
                 :class-name (:inner styles)
                 :width      width
                 :height     height}]

         ;; test axes on all edges
         [:g {:key       "bottom"
              ;:class-name ".xaxis"
              :transform (str "translate(0," (+ (first (out y)) 10) ")")}
          (axisBottom {:scale x :ticks x-ticks})]
         [:g {:key       "left"
              :transform (str "translate(" (- (first (out x)) 10) ",0)")}
          (axisLeft {:scale y :ticks y-ticks})]


         #_[:text {:key        "note"
                   :class-name (:annotation styles)
                   :x          "-30px"
                   :y          "-40px"}
            "translate by ((:left margin), (:top margin))"]
         ]

        ;; add in arrows
        #_[:g {:key 3}
           [:line {:key        0
                   :class-name (:arrow styles)
                   :x2         (:left padding)
                   :y2         (:top padding)
                   :marker-end "url(#triangle-end)"}]
           [:line {:key        1
                   :class-name (:arrow styles)
                   :x2         (/ (:width inner) 2)
                   :x1         (/ (:width inner) 2)
                   :y2         (- (:height inner) (:bottom padding))
                   :y1         (:height inner)
                   :marker-end "url(#triangle-end)"}]
           [:line {:key        2
                   :class-name (:arrow styles)
                   :x2         (:left padding)
                   :y1         (/ (:height inner) 2)
                   :y2         (/ (:height inner) 2)
                   :marker-end "url(#triangle-start)"}]
           [:line {:key        3
                   :class-name (:arrow styles)
                   :x1         (:width inner)
                   :x2         (- (:width inner) (:right padding))
                   :y1         (/ (:height inner) 2)
                   :y2         (/ (:height inner) 2)
                   :marker-end "url(#triangle-end)"}]
           [:text {:key        4
                   :class-name (:annotation styles)
                   :x          0
                   :y          -8} "origin"]
           [:circle {:key        5
                     :class-name (:origin styles)
                     :r          4.5}]
           ]]])))