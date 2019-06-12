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
     (:require [clojure.java.io :refer [make-parents]]))

(doseq [experiment-num [1 2 3 4 5 6]]
(doseq [method [:lmh]]
(doseq [num-rect [30 90 150]]
(let [num-iter 15000
      intermediate-result (fn [x] (or (= (mod x 1000) 0)))]
  (doseq [[image image-str] [[(ImageUtil. "images/anglican.png") "anglican"]
                             [(ImageUtil. "images/hands_20.jpg") "hands"]
                             [(ImageUtil. "images/monalisa_10.jpg") "monalisa"]
                             [(ImageUtil. "images/soju_8.jpg") "soju"]
                             [(ImageUtil. "images/starrynight_20.jpg") "starrynight"]]]
    (let [w (.getWidth image)
          h (.getHeight image)
         
          draw (fn [im v] (.computeSSIM im v))]
         
          (let [ofname-base (format "results/ssim/%s-nr%d-%s-expnum%d"
                                    image-str
                                    num-rect
                                    (name method)
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
                                        (observe (normal 1 0.0001) sim-val)
                                        [values sim-val])))
               
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
                                         (float sim-val))))
                     
                      (recur (drop 1 internal-results)
                             (inc i)
                             (+ elapsed-time (- after-time prev-time))))
                    (spit ofname-log (format "Elapsed time: %d ms\n" elapsed-time) :append true))))))))))
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"}
;; <=

;; @@

;; @@
