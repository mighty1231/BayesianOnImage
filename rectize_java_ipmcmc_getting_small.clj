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
(doseq [num-rect [30 90 150 300]]
(doseq [method [:lmh]]
(doseq [num-step [2 5 10]]
(doseq [[image image-str] [[(ImageUtil. "images/anglican.png" 4) "anglican"]
                           [(ImageUtil. "images/hands_20.jpg" 4) "hands"]
                           [(ImageUtil. "images/monalisa_10.jpg" 4) "monalisa"]
                           [(ImageUtil. "images/soju_8.jpg" 4) "soju"]
                           [(ImageUtil. "images/starrynight_20.jpg" 4) "starrynight"]]]
  (let [num-iter 20000
        intermediate-result (fn [x] (or (<= x 4) (= (mod x 1000) 0)))
        w (.getWidth image)
        h (.getHeight image)
        draw (fn [im v] (.computeMSE im v))
        
        step-unit (quot num-rect num-step) ;; draw STEP-UNIT rectangles and observe, and repeat it
        
        ofname-base (format "results/java_ipmcmc_smaller/%s-nr%d-%s-%d-expnum%d"
                            image-str
                            num-rect
                            (name method)
                            num-step
                            experiment-num)
        ofname-log (format "%s.log" ofname-base)
        
        iwdistr (map (fn [i] (uniform-discrete (/ (* i w) num-step) (/ (* (inc i) w) num-step))) (range num-step))
        ihdistr (map (fn [i] (uniform-discrete (/ (* i h) num-step) (/ (* (inc i) h) num-step))) (range num-step))
        
        query-stmt (with-primitive-procedures [draw]
                     (query []
                            (loop [i (dec num-step) sampled []]
                              (let [values (repeatedly step-unit 
                                                       (fn [] (map #(sample %) [(uniform-discrete 0 w)
                                                                                (uniform-discrete 0 h)
                                                                                (nth iwdistr i)
                                                                                (nth ihdistr i)
                                                                                (uniform-discrete 0 256)])))
                                    total-values (concat sampled values)]
                                
                                (if (> i 0)
                                  (recur (- i 1)
                                         total-values)
                                  (let [sim-val (draw image total-values)]
                                    (observe (normal 0 1) sim-val)
                                    [total-values sim-val]))))))
        
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
