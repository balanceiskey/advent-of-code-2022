(ns advent-of-code-2022.day-7
  (:require [clojure.string :as str]
            [clojure.pprint :as pprint]))

; Function Buddies.
(defn parse-cmd [row]
  (let [parts (str/split row #" ")]
    {:cmd (if (= "cd" (get parts 1)) :cd :ls)
     :args [(get parts 2)]}))

(defn parse-dir [row]
  {:cmd :dir :args [(get (str/split row #" ") 1)]})

(defn parse-file [row]
  (let [parts (str/split row #" ")]
   {:cmd :file :args [(Integer/parseInt (get parts 0))
                      (get parts 1)]}))

(defn row->data [row]
  (case (first row)
    \$ (parse-cmd row)
    \d (parse-dir row)
    (parse-file row)))

(defn cd-handler [dirname world]
  (if (= dirname "..")
    (assoc world :cwd (pop (:cwd world)))
    (assoc world :cwd (conj (:cwd world) dirname))))

(defn file-handler [size filename world]
  (let [new-files (conj (:files world) {:size size :name filename :parents (:cwd world)})]
    (assoc world :files new-files)))

(defn noop-handler [_ world] world)

(def handlers {:cd cd-handler
               :dir noop-handler
               :file file-handler
               :ls noop-handler})

(defn handle
  "Takes some action against state"
  [world action]
  (let [cmd (:cmd action)
        args (:args action)
        handler (apply partial (flatten [(cmd handlers) args]))]
    (handler world)))

(defn get-leaves [v]
  (let [n (count v)]
    (for [i (range 1 (inc n))]
      (str/join ":" (subvec v 0 i)))))

(defn get-directories [files]
  (set (flatten (map #(get-leaves (:parents %)) files))))

(defn update-vals [map vals f]
  (reduce #(update-in % [%2] f) map vals))

(defn reduce-size [sizes file]
  (let [update-keys (get-leaves (:parents file))]
    (update-vals sizes update-keys (partial + (:size file)))))

(defn init-size-map [dirs]
  (zipmap dirs (vec (repeat (count dirs) 0))))

(defn process-files [files]
  (let [sizes (init-size-map (get-directories files))]
    (reduce reduce-size sizes files)))

(defn under-100k? [[_ v]] (<= v 100000))

(defn sum-deleteable
  "Takes in a list of command rows and provides the sum of deletable dirs"
  [rows]
  (let [init-state {:cwd [] :files []}
        row-data (reduce handle init-state (map row->data rows))]
    (apply + (vals (filter under-100k? (process-files (:files row-data)))))))


; eval party
(def puzzle-ex (str/split (slurp "./src/puzzle-inputs/day-7-ex.txt") #"\n"))
(def puzzle-alt (str/split (slurp "./src/puzzle-inputs/day-7-alt.txt") #"\n"))
(def puzzle-pt1 (str/split (slurp "./src/puzzle-inputs/day-7.txt") #"\n"))

(sum-deleteable puzzle-ex)
(sum-deleteable puzzle-alt)
(sum-deleteable puzzle-pt1)


