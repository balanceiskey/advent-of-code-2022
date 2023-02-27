(ns advent-of-code-2022.day-1
  (:require [clojure.string :as str])
  (:gen-class))

; "calories" are the puzzle input
; the incoming puzzle input is just newline split sets of calorie counts, we want to take that and basically get back say a list of summations

; 1. Split on breaks
; 2. map over splits, return max cals
; 3. find index position of highest cals


(defn line-to-cals
  "Takes a 'line' ('3120\n4127\n') and turns it into a reduced sum"
  [line]
  (let [parts (map #(Integer/parseInt %) (str/split line #"\n"))]
    (reduce + parts)))

; we want to map over that, but what we really want
; is to know what to do with a single line,
; which now looks like 3120\n4127\n

; This gives us the sums

(defn to-sums []
  (map line-to-cals (str/split (slurp "./src/puzzle-inputs/day-1.txt") #"\n\n")))

; we need to find the index position of the highest value

(def maximum (apply max (to-sums)))

; 68292


