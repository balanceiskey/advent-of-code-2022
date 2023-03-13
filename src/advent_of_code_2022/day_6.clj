(ns advent-of-code-2022.day-5
  (:require [clojure.string :as str]))

(def marker-size 14)

(defn input->chunks [in]
  "Accepts a string and partitions it into quad char chunks"
  (map
    (fn [index] (subs in index (+ index marker-size)))
    (range 0 (- (+ 1 (count in)) marker-size))))

(defn sopm? [chunk]
  (= (count chunk) (count (set chunk))))

(def puzzle-input (slurp "./src/puzzle-inputs/day-6.txt"))

(def sopm (first (filter sopm? (input->chunks puzzle-input))))
(+ marker-size (str/index-of puzzle-input sopm))












