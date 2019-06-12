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
     (:require [clojure.java.io :refer [make-parents as-file]]))

(def images [[(ImageUtil. "images/anglican.png" 1) "anglican"]
             [(ImageUtil. "images/hands_20.jpg" 1) "hands"]
             [(ImageUtil. "images/monalisa_10.jpg" 1) "monalisa"]
             [(ImageUtil. "images/soju_8.jpg" 1) "soju"]
             [(ImageUtil. "images/starrynight_20.jpg" 1) "starrynight"]])

(doseq [experiment-num [1 2 3 4 5 6 7 8 9 10]]
(doseq [num-rect [30 90 150 300]]
(doseq [method [:lmh :rmh :plmh :almh :palmh]]
(doseq [[image image-str] images]
  (let [num-iter 5000
          intermediate-result (fn [x] (or (= (mod x 1000) 0)))
          w (.getWidth image)
          h (.getHeight image)
          
          
          draw (fn [im v] (.computeMSE im v))]
          
          (let [ofname-base (format "results/comp-inference/%s-nr%03d-%s-expnum%d"
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
                                        (observe (normal 0 1) sim-val)
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
                      (spit ofname-log (format "%f " sim-val) :append true)
                      (if (> i 4000)
                        (doseq [[x1 x2 y1 y2 vv] shapes]
                          (spit ofname-log (format "%d %d %d %d %d " x1 x2 y1 y2 vv) :append true)))
                      (spit ofname-log (format "\n" sim-val) :append true)
                      
                      (if (intermediate-result (inc i))
                        (.render image shapes
                                 (format "%s-it%d-f%.2f.png"
                                         ofname-base
                                         (inc i)
                                         (float sim-val))))
                      
                      (recur (drop 1 internal-results)
                             (inc i)
                             (+ elapsed-time (- after-time prev-time))))
                    (spit ofname-log (format "Elapsed time: %d ms\n" elapsed-time) :append true)))))))))
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"},{"type":"html","content":"<span class='clj-var'>#&#x27;template/images</span>","value":"#'template/images"}],"value":"[[nil,nil],#'template/images]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[nil,nil],#'template/images],nil]"}
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
(.availableProcessors (Runtime/getRuntime))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>8</span>","value":"8"}
;; <=

;; @@
@f
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>100</span>","value":"100"}
;; <=

;; @@
(def f (future (Thread/sleep 10000) (println "done") 100))
@f
;; @@
;; ->
;;; done
;;; 
;; <-
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;template/f</span>","value":"#'template/f"},{"type":"html","content":"<span class='clj-long'>100</span>","value":"100"}],"value":"[#'template/f,100]"}
;; <=

;; @@
(defn one-second [a b c d e f] (print 'Z') (+ a b c d e f))

(with-primitive-procedures [one-second]
  (defquery q []
    (let [s1 (sample (uniform-discrete 0 10))
          s2 (sample (uniform-discrete 0 10))
          s3 (sample (uniform-discrete 0 10))
          s4 (sample (uniform-discrete 0 10))
          s5 (sample (uniform-discrete 0 10))
          s6 (sample (uniform-discrete 0 10))]
      (observe (normal 0 10) (one-second s1 s2 s3 s4 s5 s6))
      [s1 s2 s3 s4 s5 s6])))

(loop [i 0
       d (doquery :plmh q [] )]
  (if (< i 10)
    (let [t1 (System/currentTimeMillis)
          res (:result (first (take 2 d)))
          t2 (System/currentTimeMillis)]
      (println (- t2 t1) res)
      
      (spit "results/tmp.txt" res :append true)

      (recur (+ i 1)
             (drop 2 d)))))
;; @@

;; @@
(println "[Main] calculate the answer to life the universe and everything")

;; Used Thread/sleep to simulate long running process
(def what-is-the-answer-to-life
    (future 
        (println "[Future] started computation")
        (Thread/sleep 3000) ;; running for 3 seconds
        (println "[Future] completed computation")
        42))
        
(println "[Main] created future")

(Thread/sleep 1000)
(println "[Main] do other things while waiting for the answer")
(println "[Main] get the answer")
(println "[Main] the result" @what-is-the-answer-to-life)
(shutdown-agents)
 
;; @@
;; ->
;;; [Main] calculate the answer to life the universe and everything
;;; [Future] started computation
;;; [Main] created future
;;; [Main] do other things while waiting for the answer
;;; [Main] get the answer
;;; [Future] completed computation
;;; [Main] the result 42
;;; 
;; <-
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-var'>#&#x27;template/what-is-the-answer-to-life</span>","value":"#'template/what-is-the-answer-to-life"}],"value":"[nil,#'template/what-is-the-answer-to-life]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[nil,#'template/what-is-the-answer-to-life],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[nil,#'template/what-is-the-answer-to-life],nil],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[[nil,#'template/what-is-the-answer-to-life],nil],nil],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[[[nil,#'template/what-is-the-answer-to-life],nil],nil],nil],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[[[[nil,#'template/what-is-the-answer-to-life],nil],nil],nil],nil],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[[[[[nil,#'template/what-is-the-answer-to-life],nil],nil],nil],nil],nil],nil]"}
;; <=

;; @@

;; @@
