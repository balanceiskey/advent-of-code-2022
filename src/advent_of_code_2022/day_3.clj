(ns advent-of-code-2022.day-3
  (:require [clojure.string :as str]))

(defn split-row [row]
  (let [num-take (/ (count row) 2)]
    [(take num-take row)
     (take-last num-take row)]))

(defn lazy-contains? [col key]
  (some #{key} col))

(defn find-matches
  "Find characters that exist in the first list that also exist in the second"
  [needles haystack]
    (filter #(lazy-contains? haystack %) needles))

(defn find-first-match [needles haystack]
  (first (find-matches needles haystack)))

(defn char->point [char]
  (let [code (int char)]
    (if (< code 97)
      (- code 38)
      (- code 96))))

(defn get-rows [contents] (str/split contents #"\n"))

; part 1 eval
(def puzzle-input (slurp "./src/puzzle-inputs/day-3.txt"))
(apply + (map #(char->point (apply find-first-match (split-row %))) (get-rows puzzle-input)))

; part 2, function composition ftw
(defn find-match-2 [coll-1 coll-2 coll-3]
  (let [first-matches (find-matches coll-1 coll-2)]
    (find-first-match first-matches coll-3)))

; part 2 eval
(apply + (map #(char->point (apply find-match-2 %)) (partition 3 (get-rows puzzle-input))))










