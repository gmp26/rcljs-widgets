(ns rcljswidgets.funnel-plots
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.axis :refer [axisBottom axisTop axisLeft axisRight]]
            [svg.scales :refer [->Identity nice-linear i->o o->i in out ticks]]
            [svg.markers :refer [dot odot square osquare diamond odiamond cross plus]]
            [tests.funnel-data :refer [CABG]]
            ))

(def data CABG)

(defstyle styles
          [[".outer" {:fill   "none"
                      :stroke "#000"}]
           [".inner" {:fill   "#fff"
                      :stroke "none"}]
           [".annotation" {:font-size "10pt"}]
           [".arrow" {:stroke       "#000"
                      :stroke-width "1.5px"}]])

(def error-messages {:no-data "There is no data for at least one hospital"})

;;
;; R-code (without error handling)
;;
; N <- x$Cases
; R <- N - x$Deaths
; P <- N - x$EMR * N / 100
; xrange <- c( 0, max(N) )
; yrange <- c( min( R / N ) - 0.01, 1 )
; names <- as.character(x$Hospital)
(defn make-scales [data]
  "Given a vector of hospital records, calculate a few stats
  and the data ranges. Return augmented data"
  (let [n (map :Cases data)
        r (map - n (map :Deaths data))
        p (map - n (map #(/ % 100) (map * (map :EMR data) n)))]
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

(defn augment-data [data]
  "augment data with derived stats"
  (for [hospital (filter #(pos? (:Cases %)) data)]
    (let [n (:Cases hospital)
          r (- n (:Deaths hospital))
          p (- n (/ (* n (:EMR hospital)) 100))]
      (merge hospital
             {:survivors r
              :obs-prop (/ r n)
              :pred-prop (/ p n)
              }))))
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
  "Calculate inner plot space and appropriate scales from the outer plot dimensions,
  margins, padding, and data extent."
  (let [inner {:width  (- (:width outer) (:left margin) (:right margin))
               :height (- (:height outer) (:top margin) (:bottom margin))}
        width (- (:width inner) (:left padding) (:right padding))
        height (- (:height inner) (:top padding) (:bottom padding))
        {:keys [error x-range y-range obs-prop n]} (make-scales data)]
    {:outer    outer
     :inner    inner
     :margin   margin
     :padding  padding
     :width    width
     :height   height
     :x        (if error nil (nice-linear x-range [0 width] 5))
     :y        (if error nil (nice-linear y-range [height 0] 5))
     :obs-prop obs-prop
     :n        n
     :data     data
     :error    error
     }))


(rum/defc funnel [{:keys [error outer margin inner padding width height x y obs-prop n]}]
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

         ;; add axes on all edges
         [:g {:key       "bottom"
              :transform (str "translate(0," (+ (first (out y)) 10) ")")}
          (axisBottom {:scale x :ticks x-ticks})]
         [:g {:key       "left"
              :transform (str "translate(" (- (first (out x)) 10) ",0)")}
          (axisLeft {:scale y :ticks y-ticks})]

         ;; add data
         [:g {:key "data"}
          (mapv #(dot 2.5 %1 %2) (map (i->o x) n) (map (i->o y) obs-prop))
          ]
         ]
        ]])))