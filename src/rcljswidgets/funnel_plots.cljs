(ns rcljswidgets.funnel-plots
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.axis :refer [axisBottom axisTop axisLeft axisRight]]
            [svg.scales :refer [->Identity nice-linear i->o o->i in out ticks]]
            [svg.markers :refer [dot odot square osquare diamond odiamond cross plus]]
            [tests.funnel-data :refer [CABG]]
            ))

;(def data CABG)

(defstyle styles
          [[".outer" {:fill   "none"
                      :stroke "#000"}]
           [".inner" {:fill   "#fff"
                      :stroke "none"}]
           [".annotation" {:font-size "10pt"}]
           [".arrow" {:stroke       "#000"
                      :stroke-width "1.5px"}]])



;;
;; R-code (without error handling)
;;
; N <- x$Cases
; R <- N - x$Deaths
; P <- N - x$EMR * N / 100
; xrange <- c( 0, max(N) )
; yrange <- c( min( R / N ) - 0.01, 1 )
; names <- as.character(x$Hospital)

(defn non-negative-int? [n]
  "We need 2 out of :Deaths :Survivors :Cases to be non-negative integers"
  (and (integer? n) (or (pos? n) (zero? n))))

(defn valid-record [hospital]
  "Take a hospital record and checks that :Deaths, :Survivors and :Cases are consistent,
   returning the record with those 3 fields all filled in.
   If there is insufficient or inconsistent data, return nil"
  (let [{:keys [Deaths Survivors Cases]} hospital
        check (count (filter non-negative-int? [Deaths Survivors Cases]))]
    (cond
      (= check 3) (when (= (+ Deaths Survivors) Cases) hospital)
      (= check 2) (cond
                    (not (non-negative-int? Deaths))
                    (if (< Survivors Cases) (assoc hospital :Deaths (- Cases Survivors)) nil)

                    (not (non-negative-int? Survivors))
                    (if (< Deaths Cases) (assoc hospital :Survivors (- Cases Deaths)) nil)
                    :else (assoc hospital :Cases (+ Deaths Survivors)))
      :else nil)
    ))


(defn make-scales [data]
  "Given a vector of hospital records, calculate a few stats
  and the data ranges. Return augmented data. Assumes data is sane"
  (let [n (map :Cases data)
        r (map - n (map :Deaths data))
        p (map - n (map #(/ % 100) (map * (map :EMR data) n)))]

    {:error     nil
     :n         (into [] n)
     :pred-prop (mapv / p n)
     :x-range   [0 (apply max n)]
     :y-range   [(apply min (map / r n)) 1]}
    ))

(defn derived-data [data]
  "augment data with derived fields"
  ; todo: allow alternative but equivalent sets of supplied fields
  (for [hospital (keep valid-record data)]

    (let [{:keys [Deaths Survivors Cases EMR]} hospital]
      (merge hospital
             {:obs-prop  (/ Survivors Cases)
              :pred-prop (- 1 (/ EMR 100))}))))


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
        {:keys [x-range y-range]} (make-scales data)]
    {:outer   outer
     :inner   inner
     :margin  margin
     :padding padding
     :width   width
     :height  height
     :x       (nice-linear x-range [0 width] 5)
     :y       (nice-linear y-range [height 0] 5)
     :data    data
     }))


(rum/defc funnel [{:keys [outer margin inner padding width height x y data]}]
  (let [inner (if (nil? inner) {:width  (- (:width outer) (:left margin) (:right margin))
                                :height (- (:height outer) (:top margin) (:bottom margin))}
                               inner)
        width (if (nil? width) (- (:width inner) (:left padding) (:right padding)) width)
        height (if (nil? height) (- (:height inner) (:top padding) (:bottom padding)) height)
        ]

    (let [x-ticks (ticks x)
          y-ticks (ticks y)]
      [:svg {:width  (:width outer)
             :height (:height outer)}

       [:g {:key       0
            :transform "translate(20, 20)"}

        [:rect {:key        1
                :class-name (:outer styles)
                :width      (:width inner)
                :height     (:height inner)}]

        ;;
        ;; the plot area
        ;;
        [:g {:key       2
             :transform (str "translate(" (:left padding) "," (:right padding) ")")}
         [:rect {:key        1
                 :class-name (:inner styles)
                 :width      width
                 :height     height}]

         ;; add axes and offset them from plot-are edges
         [:g {:key       "bottom"
              :transform (str "translate(0," (+ (first (out y)) 10) ")")}
          (axisBottom {:scale x :ticks x-ticks})]
         [:g {:key       "left"
              :transform (str "translate(" (- (first (out x)) 10) ",0)")}
          (axisLeft {:scale y :ticks y-ticks})]

         ;; add plotted data
         #_[:g {:key "data"}
            (for [i (range (count data))
                  :let [hospital (nth data i)
                        op (:obs-prop hospital)
                        cases (:Cases hospital)]]
              (rum/with-key (dot 2.5 ((i->o x) cases) ((i->o y) op)) (str "dot" i)))]


         [:g {:key "data"}
          (map-indexed
            #(rum/with-key (apply (partial dot 2.5) %2) (str "dot" %1))

            (map (fn [hospital]
                   [((comp (i->o x) :Cases) hospital) ((comp (i->o y) :obs-prop) hospital)])
                 data))]

         ]
        ]])))