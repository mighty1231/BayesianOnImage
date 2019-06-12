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


(doseq [experiment-num [1 2 3 4 5 6 7 8 9 10]]
(doseq [num-rect [30 90 150]]
(doseq [method [:plmh]]
(doseq [[image image-str] [[(ImageUtil. "images/anglican.png") "anglican"]
                           [(ImageUtil. "images/hands_20.jpg") "hands"]
                           [(ImageUtil. "images/monalisa_10.jpg") "monalisa"]
                           [(ImageUtil. "images/soju_8.jpg") "soju"]
                           [(ImageUtil. "images/starrynight_20.jpg") "starrynight"]]]
  (doseq [sigma [1 2 4 6 8 10 12 14 16 18 20 25 30 35 40 45 50 60 70 80 90]]
    (let [num-iter 5000
          intermediate-result (fn [x] (= x 5000))
          w (.getWidth image)
          h (.getHeight image)
          draw (fn [im v] (.computeMSE im v))]
          
          (let [ofname-base (format "results/java-get-acceptance3/%s-nr%03d-sigma%02d-expnum%d"
                                    image-str
                                    num-rect
                                    sigma
                                    experiment-num)
                ofname-log (format "%s.log" ofname-base)
                
                query-stmt (with-primitive-procedures [draw]
                             (query []
                                    (let [values (repeatedly num-rect
                                                             (fn [] (map #(sample %) [(uniform-discrete 0 w)
                                                                                      (uniform-discrete 0 w)
                                                                                      (uniform-discrete 0 h)
                                                                                      (uniform-discrete 0 h)
                                                                                      (uniform-discrete 0 256)])))
                                          sim-val (draw image values)]
                                        (observe (normal 0 sigma) sim-val)
                                        [values sim-val])))
                
                results (doquery method query-stmt [])]
            
            (make-parents ofname-base)
            
            (spit ofname-log "")
            
            (loop [internal-results results
                   i 0
                   elapsed-time 0
                   prev-sim-val 0
                   accept-cnt 0]
	              (if (< i num-iter)
	                (let [prev-time (System/currentTimeMillis)
                          [shapes sim-val] (:result (first (take 1 internal-results)))
                          after-time (System/currentTimeMillis)
                          accepted (not= prev-sim-val sim-val)]
                      (spit ofname-log (format "%f\n" sim-val) :append true)
                      
                      (if (intermediate-result (inc i))
                        (.render image shapes
                                 (format "%s-it%d-f%.2f.png"
                                         ofname-base
                                         (inc i)
                                         (float sim-val))))
                      
                      (recur (drop 1 internal-results)
                             (inc i)
                             (+ elapsed-time (- after-time prev-time))
                             sim-val
                             (if accepted (inc accept-cnt) accept-cnt)))
                    (spit ofname-log (format "Elapsed time: %d ms\nTotal accept count: %d\n" elapsed-time accept-cnt) :append true))))))))))
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"}
;; <=

;; @@
(use 'nstools.ns)
(ns+ template
     (:like anglican-user.worksheet)
     (:import [ImageUtil]))

(let [im (ImageUtil. "images/anglican.png")]
  (println (.computeMSE im [[0 100 0 100 100] [0 100 0 100 100] [0 100 0 100 100]]))
  (println (.computeMSE im [[0 100 0 100 100] [0 100 0 100 100] [0 100 0 100 101]])))

;; @@
;; ->
;;; 39819.39348388953
;;; 39786.75852272727
;;; 
;; <-
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[nil,nil],nil]"}
;; <=

;; @@

(-> {1 2, 3 4 10 245 [3 4] 3434} (get [3 4]) (+ 1))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>3435</span>","value":"3435"}
;; <=

;; @@

(doseq [num-rect [30 90 150 300]]
(doseq [[image image-str] [[(ImageUtil. "images/anglican.png") "anglican"]
                           [(ImageUtil. "images/hands_20.jpg") "hands"]
                           [(ImageUtil. "images/monalisa_10.jpg") "monalisa"]
                           [(ImageUtil. "images/soju_8.jpg") "soju"]
                           [(ImageUtil. "images/starrynight_20.jpg") "starrynight"]]]
  (doseq [sigma (-> {"anglican" {30 [20 25 30 35 40 45 50],
                                 90 [10 12 14 16 18 20]
                                 150 [1 2 4 6 8 10]
                                 300 [0.2 0.4 0.6 0.8 1.0]}
                     "starrynight" {30 [50 60 70 80 90 100]
                                    90 [20 25 30 35 40 45 50]
                                    150 [1 2 4 6 8 10]
                                    300 [0.2 0.4 0.6 0.8 1.0]}
                     "anglican" {30 [50 60 70 80 90 100]
                                 90 [20 25 30 35 40 45 50]
                                 150 [1 2 4 6 8 10]
                                 300 [0.2 0.4 0.6 0.8 1.0]}
                     "hands" {30 [50 60 70 80 90 100]
                              90 [20 25 30 35 40 45 50]
                              150 [1 2 4 6 8 10]
                              300 [0.2 0.4 0.6 0.8 1.0]}
                     "soju" {30 [20 25 30 35 40 45 50]
                             90 [1 2 4 6 8 10]
                             150 [1 2 4 6 8 10]
                             300 [0.2 0.4 0.6 0.8 1.0]}} (get image-str) (get num-rect))]
    (print sigma))))
;; @@

;; @@
{"anglican" {30 [20 25 30 35 40 45 50],
                                 90 [10 12 14 16 18 20]
                                 150 [1 2 4 6 8 10]
                                 300 [0.2 0.4 0.6 0.8 1.0]}
                     "starrynight" {30 [50 60 70 80 90 100]
                                    90 [20 25 30 35 40 45 50]
                                    150 [1 2 4 6 8 10]
                                    300 [0.2 0.4 0.6 0.8 1.0]}
                     "anglican" {30 [50 60 70 80 90 100]
                                 90 [20 25 30 35 40 45 50]
                                 150 [1 2 4 6 8 10]
                                 300 [0.2 0.4 0.6 0.8 1.0]}
                     "hands" {30 [50 60 70 80 90 100]
                              90 [20 25 30 35 40 45 50]
                              150 [1 2 4 6 8 10]
                              300 [0.2 0.4 0.6 0.8 1.0]}
                     "soju" {30 [20 25 30 35 40 45 50]
                             90 [1 2 4 6 8 10]
                             150 [1 2 4 6 8 10]
                             300 [0.2 0.4 0.6 0.8 1.0]}}
;; @@

;; @@

;; @@
