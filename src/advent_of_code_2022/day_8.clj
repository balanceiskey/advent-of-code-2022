(ns advent-of-code-2022.day-8
  (:require [advent-of-code-2022.utils :as utils]
            [clojure.string :as str]
            [clojure.pprint :as pprint]))

(def puzzle-alt (utils/get-rows "day-8-alt.txt"))
(def puzzle-ex (utils/get-rows "day-8-ex.txt"))
(def puzzle-real (utils/get-rows "day-8.txt"))

(def puzzle puzzle-real)

(defn string->int [s]
  (into [] (map #(Integer/parseInt %) (str/split s #""))))

(defn build-map [rows]
  (into [] (map string->int rows)))

(def tree-map (build-map puzzle))
(def num-rows (count tree-map))
(def num-columns (count (first tree-map)))

(defn num-edge-trees
  "Accepts a tree map and returns the number trees that make up its edge."
  [tmap]
  (let [width (count (get tmap 0 ))
        height (count tmap)]
    (+ (- (* 2 width) 2) (- (* 2 height) 2))))

(defn get-in-tmap [tmap coords]
  (get-in tmap coords))

; [
  ; [ 3 0 3 7 3 ]
  ; [ 2 5 5 1 2 ]
  ; [ 6 5 3 3 2 ]
  ; [ 3 3 5 4 9 ]
  ; [ 3 5 3 9 0 ]
; ]

(defn take-top [[row column] tmap]
  (reverse (map #(get % column) (subvec tmap 0 row))))

(defn take-bottom [[row column] tmap]
  (map #(get % column) (subvec tmap (inc row))))

(defn take-right [[row column] tmap]
  (subvec (get tmap row) (inc column)))

(defn take-left [[row column] tmap]
  (reverse (subvec (get tmap row) 0 column)))

(defn visible? [[row column] tmap take-fn]
  (let [value (get-in-tmap tmap [row column])
        taken (take-fn [row column] tmap)]
    (every? #(< % value) taken)))

(defn some-visible? [coords tmap]
  (some true? [(visible? coords tmap take-top)
               (visible? coords tmap take-right)
               (visible? coords tmap take-bottom)
               (visible? coords tmap take-left)]))

(defn gen-coords [num-rows num-columns]
  (for [row (range num-rows)
        col (range num-columns)]
    [row col]))

(count (filter true? (map #(some-visible? % tree-map) (gen-coords num-rows num-columns))))



