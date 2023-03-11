(ns advent-of-code-2022.day-3
  (:require [clojure.string :as str]))

(defn split-row [row]
  (let [num-take (/ (count row) 2)]
    [(take num-take row)
     (take-last num-take row)]))

(defn lazy-contains? [col key]
  (some #{key} col))

(defn find-match
  "Find character from first list that exists in second"
  [needles haystack]
    (first (filter #(lazy-contains? haystack %) needles)))

(defn char->point [char]
  (let [code (int char)]
    (if (< code 97)
      (- code 38)
      (- code 96))))

(defn get-rows [contents] (str/split contents #"\n"))

; part 1 eval
(def puzzle-input (slurp "./src/puzzle-inputs/day-3.txt"))
(apply + (map #(char->point (apply find-match (split-row %))) (get-rows puzzle-input)))


; * elves (e) are divided into groups (g) of three
; * each elf (e) carries a badge (b) that identifies their group (g)
; * within each group (g) the badge (b) is the only item type (t) carried by all three elves
  ; * if a group's badge (b) is item type \B then all three elves will have item type \B somewhere in their rucksack
  ; * at most two of the elves will be carrying any other item type (t)
;











