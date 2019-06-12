;; gorilla-repl.fileformat = 1

;; **
;;; # Gorilla REPL
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(use 'nstools.ns)
(ns+ template
     (:like anglican-user.worksheet)
     (:import [ImageUtil])
     (:require [clojure.pprint :refer [pprint]])
     (:require [clojure.java.io :refer [make-parents as-file]]))
;; make-parents
;; make directory structure for file: https://clojuredocs.org/clojure.java.io/make-parents


(doseq [experiment-num [1]]
(doseq [num-rect [30 90]]
(doseq [method [:ipmcmc :lmh]]
(doseq [num-step [2 5 10]]
(doseq [[image image-str] [[(ImageUtil. "images/anglican.png") "anglican"]
                           [(ImageUtil. "images/hands_20.jpg") "hands"]
                           [(ImageUtil. "images/monalisa_10.jpg") "monalisa"]
                           [(ImageUtil. "images/soju_8.jpg") "soju"]
                           [(ImageUtil. "images/starrynight_20.jpg") "starrynight"]]]
  (let [num-iter 5000
        intermediate-result (fn [x] (or (<= x 4) (= (mod x 1000) 0)))
        w (.getWidth image)
        h (.getHeight image)
        draw (fn [im v] (.computeMSE im v))
        
        step-unit (quot num-rect num-step) ;; draw STEP-UNIT rectangles and observe, and repeat it
        
        ofname-base (format "results/java_ipmcmc_ind/%s-nr%d-%s-%d-expnum%d"
                            image-str
                            num-rect
                            (name method)
                            num-step
                            experiment-num)
        ofname-log (format "%s.log" ofname-base)
        
        query-stmt (with-primitive-procedures [draw]
                     (query []
                            (loop [i 0 sampled []]
                              (let [values (repeatedly step-unit
                                                       (fn [] (map #(sample %) [(uniform-discrete 0 w)
                                                                                (uniform-discrete 0 w)
                                                                                (uniform-discrete 0 h)
                                                                                (uniform-discrete 0 h)
                                                                                (uniform-discrete 0 256)])))
                                    total-values (concat sampled values)
                                    sim-val (draw image values)]
                                
                                (observe (normal 0 1) sim-val)
                                
                                (if (< i num-rect)
                                  (recur (+ i step-unit)
                                         total-values)
                                  [total-values sim-val])))))
        
        results (doquery method query-stmt [])]
    
    (make-parents ofname-base)
    (spit ofname-log "")
    
    (loop [internal-results results
           i 0
           elapsed-time 0]
      (if (< i num-iter)
        (let [prev-time (System/currentTimeMillis)
              [shapes sim-val] (:result (first (take 1 internal-results)))
              after-time (System/currentTimeMillis)]
          (spit ofname-log (format "%f\n" sim-val) :append true)
          
          (if (intermediate-result (inc i))
            (.render image shapes
                     (format "%s-it%d-f%.2f.png"
                             ofname-base
                             (inc i)
                             (float sim-val)) ))
          
          (recur (drop 1 internal-results)
                 (inc i)
                 (+ elapsed-time (- after-time prev-time))))
        (spit ofname-log (format "Elapsed time: %d ms\n" elapsed-time) :append true)))))))))
  
  
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[nil,nil],nil]"}
;; <=

;; @@
;; inverse


(use 'nstools.ns)
(ns+ template
  (:like anglican-user.worksheet))
(use '[mikera.image.core :rename {flip image-flip}]
     '[mikera.image.colours]
     '[mikera.image.filters]
     '[clojure.pprint :rename {write pprint-write}]
     '[clojure.java.io :only (make-parents as-file)])
;; make-parents
;; make directory structure for file: https://clojuredocs.org/clojure.java.io/make-parents


(defn compute-mse-transparent [image1 image2]
  (let [pixels1 (get-pixels image1)
        pixels2 (get-pixels image2)]
    (assert (= (alength pixels1) (alength pixels2)))
    (loop [idx (dec (alength pixels1))
           result 0]
      (if (>= idx 0)
        (recur (dec idx)
               (+ result
                  (let [[r1 g1 b1 a1] (components-argb (aget pixels1 idx))
                        [r2 g2 b2 a2] (components-argb (aget pixels2 idx))
                        sqdiff (fn [x y] (* (- x y) (- x y)))]
                    (/ (+ (sqdiff (* r1 a1) (* r2 a2))
                          (sqdiff (* g1 a1) (* g2 a2))
                          (sqdiff (* b1 a1) (* b2 a2))) 65025.0) ;; 65025 == 255*255
                       )))
        (/ result (alength pixels1))))))


(defn draw [w h values]
  (let [im0 (new-image w h)
        graphics (.getGraphics im0)]
    (.setColor graphics (to-java-color (rgb-from-components 255 255 255)))
    (.fillRect graphics 0 0 w h)
    (.setComposite graphics (java.awt.AlphaComposite/getInstance java.awt.AlphaComposite/SRC_OVER 0.5))
    (doseq [[x1 x2 y1 y2 r g b] values]
      (let [xm (min x1 x2)
            xM (max x1 x2)
            ym (min y1 y2)
            yM (max y1 y2)]
        (.setColor graphics (to-java-color (rgb-from-components r g b)))
        (.fillRect graphics xm ym (- xM xm) (- yM ym))))
    im0))

(doseq [num-rect [30 60 120 180]]
  (doseq [experiment-num [1 2]]
    (doseq [num-step [5 10 15 30]]
      (let [image (load-image "data/kaist.png")
            image-str "kaist_ipmcmc_inv20000"
            w (width image)
            h (height image)
            num-iter 20000
            
            step-unit (quot num-rect num-step) ;; draw STEP-UNIT rectangles and observe, and repeat it
            
            ofname-base (format "results/%s/%d-%d-%d"
                                image-str
                                num-rect
                                num-step
                                experiment-num)
            
            ofname (format "%s.png" ofname-base)]
        
        ;; cache
        (if (not (.exists (as-file ofname)))
          (println "processing" ofname)
          
          (let [query-stmt
                (with-primitive-procedures
                  [draw compute-mse-transparent]
                  (query []
                         (loop [i 0 sampled []]
                           (let [values (repeatedly step-unit
                                                    (fn [] (map #(sample %) [(uniform-discrete 0 w)
                                                                             (uniform-discrete 0 w)
                                                                             (uniform-discrete 0 h)
                                                                             (uniform-discrete 0 h)
                                                                             (uniform-discrete 0 256)
                                                                             (uniform-discrete 0 256)
                                                                             (uniform-discrete 0 256)])))
                                 
                                 total-values (concat values sampled)
                                 sim-val (compute-mse-transparent image (draw w h total-values))]
                             
                             (observe (normal 0 1) sim-val)
                             
                             (if (< i num-rect)
                               (recur (+ i step-unit)
                                      total-values)
                               [total-values sim-val])))))
                
                
                ;; do query
                results (doquery :ipmcmc query-stmt [])]
            
            (make-parents ofname-base)
            
            (let [[shapes sim-val] (:result (first (take 1 (drop num-iter results))))]
              (save (draw w h shapes) ofname)))
          )))))

(comment 
  (loop [internal-results results
         i 0]
    (if (<= i num-iter)
      (if (= num-iter i)
        (let [[shapes sim-val] (:result (first (take 1 internal-results)))]

          (save (draw w h shapes)
                (format "%s.png"
                        ofname-base))

          (recur (drop 1 internal-results)
                 (inc i)))))))
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[nil,nil],nil]"},{"type":"html","content":"<span class='clj-var'>#&#x27;template/compute-mse-transparent</span>","value":"#'template/compute-mse-transparent"}],"value":"[[[nil,nil],nil],#'template/compute-mse-transparent]"},{"type":"html","content":"<span class='clj-var'>#&#x27;template/draw</span>","value":"#'template/draw"}],"value":"[[[[nil,nil],nil],#'template/compute-mse-transparent],#'template/draw]"}
;; <=

;; @@
;; inverse


(use 'nstools.ns)
(ns+ template
  (:like anglican-user.worksheet))
(use '[mikera.image.core :rename {flip image-flip}]
     '[mikera.image.colours]
     '[mikera.image.filters]
     '[clojure.pprint :rename {write pprint-write}]
     '[clojure.java.io :only (make-parents as-file)])
;; make-parents
;; make directory structure for file: https://clojuredocs.org/clojure.java.io/make-parents

(.exists (as-file "results/kaist_ipmcmc_inv20000/60-10-2.png"))
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[nil,nil],nil]"},{"type":"html","content":"<span class='clj-unkown'>false</span>","value":"false"}],"value":"[[[nil,nil],nil],false]"}
;; <=

;; @@
(if false 0)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@

          
          ofname-base (format "results/%s/%d-%d-%d"
                              image-str
                              num-rect
                              num-step
                              experiment-num)
          
          query-stmt
          (with-primitive-procedures
            [draw compute-mse-transparent]
            (query []
                   (loop [i 0 sampled []]
                     (let [values (repeatedly step-unit
                                              (fn [] (map #(sample %) [(uniform-discrete 0 w)
                                                                       (uniform-discrete 0 w)
                                                                       (uniform-discrete 0 h)
                                                                       (uniform-discrete 0 h)
                                                                       (uniform-discrete 0 256)
                                                                       (uniform-discrete 0 256)
                                                                       (uniform-discrete 0 256)])))
                           
                           total-values (concat sampled values)
                           sim-val (compute-mse-transparent image (draw w h total-values))]
                       
                       (observe (normal 0 1) sim-val)
                       
                       (if (< i num-rect)
                         (recur (+ i step-unit)
                                total-values)
                         [total-values sim-val])))))

          
          ;; do query
          results (doquery :ipmcmc query-stmt [])]
      
      (make-parents ofname-base)
      
      
      (let [[shapes sim-val] (:result (first (take 1 (drop num-iter results))))]
        (save (draw w h shapes)
              (format "%s.png"
                      ofname-base)))
      ))))

;; @@
