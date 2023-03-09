(ns advent-of-code-2022.day-2
  (:require [clojure.string :as str]))

(def puzzle-input (slurp "./src/puzzle-inputs/day-2.txt"))

(defn decrypt [row]
  (into [] (map
    (fn [v]
      (case v
        ("A" "X") :rock
        ("B", "Y") :paper
        ("C" "Z") :scissors))
    row)))

(defn get-rows [contents]
  (map (fn [line] (decrypt (str/split line #" "))) (str/split contents #"\n")))

; we can describe rps gestures strictly in terms of what gestures can beat an opponent
(def beats {:rock :scissors
            :scissors :paper
            :paper :rock})
(def gesture-points {:rock 1
                     :paper 2
                     :scissors 3})

; Almost positive there's a better way to do this, the
; nested conditional in here isn't doing it for me.
(defn battle-points [opponent player]
  (if (= opponent player)
    3 ; draw
    (if (= opponent (get beats player)) 6 0))) ; win / loss

(defn row->points [row]
  (let [opp (get row 0)
        player (get row 1)]
    (+ (get gesture-points player) (battle-points opp player))))

;eval
(reduce + (map row->points (get-rows puzzle-input)))

(def loses {:rock :paper
            :scissors :rock
            :paper :scissors})
(defn get-response-gesture [gesture intent]
  (case intent
    :win (get loses gesture)
    :lose (get beats gesture)
    :draw (identity gesture)))


(defn decrypt-with-intent [row]
  (into [] (map
             (fn [v]
               (case v
                 "A" :rock
                 "B" :paper
                 "C" :scissors
                 "X" :lose
                 "Y" :draw
                 "Z" :win))
             row)))

(defn get-intent-rows
  "Similar to get-rows; we decrypt differently"
  [contents]
  (map
    #(assoc % 1 (apply get-response-gesture %))
    (map
      #(decrypt-with-intent (str/split % #" "))
      (str/split contents #"\n"))))

;eval
(reduce + (map row->points (get-intent-rows puzzle-input)))