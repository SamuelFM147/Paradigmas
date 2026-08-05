(defn tabuada [x]
  (doseq [i (range 11)] 
    (println (format "%d x %d = %d" i x (* i x)))))

(tabuada 5)

