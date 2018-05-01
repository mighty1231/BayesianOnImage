;; gorilla-repl.fileformat = 1

;; **
;;; # Monalisa
;;; 
;;; 1. To compile this project, add [net.mikera/imagez "0.12.0"] on anglican-user/project.clj in dependencies
;;; 2. At the same project.clj file, add following line :resource-paths ["programs"]
;;; 3. I put monalisa.jpg inside programs/ directory inside anglican-user directory
;; **

;; @@
(use 'mikera.image.core)
(use 'mikera.image.colours)

;; load an image from a resource file
(def monalisa (load-image-resource "monalisa.jpg"))

(show monalisa)
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[nil,nil]"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/monalisa</span>","value":"#'user/monalisa"}],"value":"[[nil,nil],#'user/monalisa]"},{"type":"html","content":"<span class='clj-unkown'>#object[javax.swing.JFrame 0x47f2197b &quot;javax.swing.JFrame[frame0,75,174,470x771,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,0,0,470x771,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]&quot;]</span>","value":"#object[javax.swing.JFrame 0x47f2197b \"javax.swing.JFrame[frame0,75,174,470x771,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,0,0,470x771,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]"}],"value":"[[[nil,nil],#'user/monalisa],#object[javax.swing.JFrame 0x47f2197b \"javax.swing.JFrame[frame0,75,174,470x771,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,0,0,470x771,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]]"}
;; <=

;; @@
;; Basic example
;; create a new image
(def bi (new-image 32 32))

;; gets the pixels of the image, as an int array
(def pixels (get-pixels bi))

;; fill some random pixels with colours
(dotimes [i 1024]
  (aset pixels i (rand-colour)))

;; update the image with the newly changed pixel values
(set-pixels bi pixels)

;; view our new work of art
;; the zoom function will automatically interpolate the pixel values
(show bi :zoom 10.0 :title "Isn't it beautiful?")
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;user/bi</span>","value":"#'user/bi"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/pixels</span>","value":"#'user/pixels"}],"value":"[#'user/bi,#'user/pixels]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[#'user/bi,#'user/pixels],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[#'user/bi,#'user/pixels],nil],nil]"},{"type":"html","content":"<span class='clj-unkown'>#object[javax.swing.JFrame 0x63630e5a &quot;javax.swing.JFrame[frame1,75,164,340x420,layout=java.awt.BorderLayout,title=Isn&#x27;t it beautiful?,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,5,25,330x390,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777675,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]&quot;]</span>","value":"#object[javax.swing.JFrame 0x63630e5a \"javax.swing.JFrame[frame1,75,164,340x420,layout=java.awt.BorderLayout,title=Isn't it beautiful?,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,5,25,330x390,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777675,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]"}],"value":"[[[[#'user/bi,#'user/pixels],nil],nil],#object[javax.swing.JFrame 0x63630e5a \"javax.swing.JFrame[frame1,75,164,340x420,layout=java.awt.BorderLayout,title=Isn't it beautiful?,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,5,25,330x390,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777675,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]]"}
;; <=

;; @@
;; Practice
(def monalisa (load-image-resource "monalisa.jpg"))
(def pixels (get-pixels monalisa))
(dotimes [y 100]
  (dotimes [x 100]
    (aset pixels (+ 100 x (* (+ 100 y) (width monalisa))) (rand-colour))))
(set-pixels monalisa pixels)

(show monalisa)
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;user/monalisa</span>","value":"#'user/monalisa"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/pixels</span>","value":"#'user/pixels"}],"value":"[#'user/monalisa,#'user/pixels]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[#'user/monalisa,#'user/pixels],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[#'user/monalisa,#'user/pixels],nil],nil]"},{"type":"html","content":"<span class='clj-unkown'>#object[javax.swing.JFrame 0x51c11eb4 &quot;javax.swing.JFrame[frame0,75,669,460x741,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,5,25,460x741,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]&quot;]</span>","value":"#object[javax.swing.JFrame 0x51c11eb4 \"javax.swing.JFrame[frame0,75,669,460x741,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,5,25,460x741,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]"}],"value":"[[[[#'user/monalisa,#'user/pixels],nil],nil],#object[javax.swing.JFrame 0x51c11eb4 \"javax.swing.JFrame[frame0,75,669,460x741,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,5,25,460x741,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]]"}
;; <=

;; @@
(def monalisa (load-image-resource "monalisa.jpg"))

;; drawRect: draw a rectangle {(x, y) | x1 <= x < x2 and y1 <= y < y2} on IMAGE as given COLOR
;; Color is defined on
;; https://github.com/mikera/imagez/blob/master/src/main/clojure/mikera/image/colours.clj
(defn drawRect [image x1 y1 x2 y2 color]
  (let [pixels (get-pixels image)
        w (width image)]
    (if (or (> x1 x2) (> y1 y2))
      (throw (Exception. "x1 <= x2 and y1 <= y2"))
      (loop [y y1]
        (when (< y y2)
          (loop [x x1]
            (when (< x x2)
              (aset pixels (+ (* w y) x) color)
              (recur (+ x 1))))
          (recur (+ y 1)))))
    (set-pixels image pixels)))

(drawRect monalisa 10 10 100 100 (long-colour 0xFF1060E0))
(drawRect monalisa 100 200 200 400 (long-colour 0xFF806000))

(show monalisa)
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;user/monalisa</span>","value":"#'user/monalisa"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/drawRect</span>","value":"#'user/drawRect"}],"value":"[#'user/monalisa,#'user/drawRect]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[#'user/monalisa,#'user/drawRect],nil]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[#'user/monalisa,#'user/drawRect],nil],nil]"},{"type":"html","content":"<span class='clj-unkown'>#object[javax.swing.JFrame 0x47f2197b &quot;javax.swing.JFrame[frame0,75,202,470x771,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,0,0,470x771,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]&quot;]</span>","value":"#object[javax.swing.JFrame 0x47f2197b \"javax.swing.JFrame[frame0,75,202,470x771,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,0,0,470x771,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]"}],"value":"[[[[#'user/monalisa,#'user/drawRect],nil],nil],#object[javax.swing.JFrame 0x47f2197b \"javax.swing.JFrame[frame0,75,202,470x771,layout=java.awt.BorderLayout,title=Imagez Frame,resizable,normal,defaultCloseOperation=DISPOSE_ON_CLOSE,rootPane=javax.swing.JRootPane[,0,0,470x771,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]\"]]"}
;; <=

;; @@
;; Test get average value of rgb values on rectangle
(def monalisa (load-image-resource "monalisa.jpg"))

;; draw (10, 10), ..., (99, 99) with color (r, g, b) = (0x10, 0x60, 0xE0)
(drawRect monalisa 10 10 100 100 (long-colour 0xFF1060E0))

;; Pixels are updated
(def pixels (get-pixels monalisa))
(def w (width monalisa))
(def h (height monalisa))

;; Pixel value could be calculated with two different ways
;; Details on
;; https://github.com/mikera/imagez/blob/master/src/main/clojure/mikera/image/core.clj
(dotimes [y 10]
  (dotimes [x 10]
    (let [left (bit-and 0xFFFFFFFF (aget pixels (+ (* w y) x)))
          right (get-pixel monalisa x y)]
      (if (= left right)
        nil
        (throw (Exception. "error!!!"))))))

;; getSectAvg: evaluate average RGB color on a rectangle {(x, y) | x1 <= x < x2 and y1 <= y < y2} on given PIXELS
;; returns [avg-red avg-green avg-blue]
;;
;; Assume 0 <= x1 < x2 <= (width image)
;;    and 0 <= y1 < y2 <= (height image)
(defn getSectAvg [pixels w x1 y1 x2 y2]
  
  ;; rowsum: get sum of pixels with index IDX to IDX+X2-X1
  ;; Used on evaluate rgb-values of each rows
  (letfn [(rowsum [idx] (reduce (fn [cumul nxt]
                                  (map + cumul nxt))
                                [0 0 0]
                                (map (fn [t]
                                       (components-rgb (aget pixels t))) ;; It is array of long types - [r g b]
                                     (range idx (+ idx x2 (- x1))))))]
    
    ;; Now, evaluate summation of rgb-values for each row from y=y1 to y=y2-1
    ;; And in order to get average value, divide it by (x2-x1)*(y2-y1)
    (map / (reduce (fn [cumul nxt]
                     (map + cumul (rowsum (+ (* nxt w) x1))))
                   [0 0 0]
                   (range y1 y2))
         (let [t (* (- y2 y1) (- x2 x1))]
           [t t t]))))

(map double (getSectAvg pixels w 10 10 100 100)) ;; Pixels (10, 10) ... (99, 99) -> avg color should be (r, g, b) = (0x10, 0x60, 0xE0)
(map double (getSectAvg pixels w 10 10 101 101)) ;; Maybe different with specified value (0x10, 0x60, 0xE0)
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;user/monalisa</span>","value":"#'user/monalisa"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[#'user/monalisa,nil]"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/pixels</span>","value":"#'user/pixels"}],"value":"[[#'user/monalisa,nil],#'user/pixels]"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/w</span>","value":"#'user/w"}],"value":"[[[#'user/monalisa,nil],#'user/pixels],#'user/w]"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/h</span>","value":"#'user/h"}],"value":"[[[[#'user/monalisa,nil],#'user/pixels],#'user/w],#'user/h]"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[[[[[#'user/monalisa,nil],#'user/pixels],#'user/w],#'user/h],nil]"},{"type":"html","content":"<span class='clj-var'>#&#x27;user/getSectAvg</span>","value":"#'user/getSectAvg"}],"value":"[[[[[[#'user/monalisa,nil],#'user/pixels],#'user/w],#'user/h],nil],#'user/getSectAvg]"},{"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-double'>16.0</span>","value":"16.0"},{"type":"html","content":"<span class='clj-double'>96.0</span>","value":"96.0"},{"type":"html","content":"<span class='clj-double'>224.0</span>","value":"224.0"}],"value":"(16.0 96.0 224.0)"}],"value":"[[[[[[[#'user/monalisa,nil],#'user/pixels],#'user/w],#'user/h],nil],#'user/getSectAvg],(16.0 96.0 224.0)]"},{"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-double'>18.68156019804371</span>","value":"18.68156019804371"},{"type":"html","content":"<span class='clj-double'>97.09056877188745</span>","value":"97.09056877188745"},{"type":"html","content":"<span class='clj-double'>221.1626615143099</span>","value":"221.1626615143099"}],"value":"(18.68156019804371 97.09056877188745 221.1626615143099)"}],"value":"[[[[[[[[#'user/monalisa,nil],#'user/pixels],#'user/w],#'user/h],nil],#'user/getSectAvg],(16.0 96.0 224.0)],(18.68156019804371 97.09056877188745 221.1626615143099)]"}
;; <=

;; @@

;; @@
