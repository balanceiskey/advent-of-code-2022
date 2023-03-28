(ns advent-of-code-2022.utils
  (:require [clojure.string :as str]))


(defn get-rows
  "Returns a newline split list of rows from a given file"
  [filename]
  (str/split (slurp (str "./src/puzzle-inputs/" filename)) #"\n"))



