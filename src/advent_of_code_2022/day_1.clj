(ns advent-of-code-2022.day-1
  (:require [clojure.string :as str])
  (:gen-class))

(defn line-to-cals
  "Takes a 'line' ('3120\n4127\n') and turns it into a reduced sum"
  [line]
  (let [parts (map #(Integer/parseInt %) (str/split line #"\n"))]
    (reduce + parts)))

(defn to-sums []
  (map line-to-cals (str/split (slurp "./src/puzzle-inputs/day-1.txt") #"\n\n")))

(def maximum (apply max (to-sums)))
(println maximum)

(def top-three (reduce + (take 3 (sort > (to-sums)))))
(println top-three)
