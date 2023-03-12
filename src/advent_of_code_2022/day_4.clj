(ns advent-of-code-2022.day-4
  (:require
    [clojure.set :as set]
    [clojure.string :as str]
    [advent-of-code-2022.day-3 :as aoc3]))

; range play, yay.

(defn range-inclusive [a b]
  (range a (+ b 1)))

(defn str->range [a]
  "2-4 => [2 3 4]"
  (set (apply range-inclusive (map #(Integer/parseInt %) (str/split a #"-")))))

(defn row->ranges [row]
  "2-4,6-8 => ([2 3 4] [6 7 8])"
  (into [] (map str->range (str/split row #","))))

(defn get-rows [contents]
  (map row->ranges (str/split contents #"\n")))

(defn sort-lists [l1 l2]
  "sort lists by length"
  (sort-by count [l1 l2]))

(defn overlap? [l1 l2]
  (let [sorted (sort-lists l1 l2)]
    (set/subset? (nth sorted 0) (nth sorted 1))))

(defn overlap-2? [l1 l2]
  (not= nil (aoc3/find-first-match l1 l2)))

; day 4 part 1
(def puzzle-input (slurp "./src/puzzle-inputs/day-4.txt"))

(count (map #(apply overlap? %) (get-rows puzzle-input)))
(count (filter true? (map #(apply overlap? %) (get-rows puzzle-input))))

; day 4 part 2
(count (filter true? (map #(apply overlap-2? %) (get-rows puzzle-input))))
