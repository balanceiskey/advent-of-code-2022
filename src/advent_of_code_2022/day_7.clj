(ns advent-of-code-2022.day-7
  (:require [clojure.string :as str]
            ; [clojure.walk :as walk]
            [clojure.pprint :as pprint]))

(def puzzle-ex (str/split (slurp "./src/puzzle-inputs/day-7-ex.txt") #"\n"))

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

(row->data "$ cd /")
(row->data "$ ls")
(row->data "dir a")
(row->data "14848514 b.txt")

(parse-file "145123 b.txt")

(first "$ cd /")


(defn clean-filename [name]
  (str/replace name "." "-"))

(defn cd-handler [dirname world]
  (if (= dirname "..")
    (assoc world :cwd (pop (:cwd world)))
    (assoc world :cwd (conj (:cwd world) (keyword dirname)))))

; (defn file-handler [size filename world]
;   (assoc-in world (conj (:cwd world) (keyword (clean-filename filename))) {:size size :type :file :name filename }))

(defn file-handler [size filename world]
  (let [new-files (conj (:files world) {:size size :name filename :parents (:cwd world)})]
    (assoc world :files new-files)))

; dirs tells us about things that are going to extend the tree, files tell us about where sizes belong
(defn dir-handler [path world] world)

(defn ls-handler [path world] world)

(def handlers {:cd cd-handler
               :dir dir-handler
               :file file-handler
               :ls ls-handler})

(defn handle [world action]
  (let [cmd (:cmd action)
        args (:args action)
        handler (apply partial (flatten [(cmd handlers) args]))]
    (handler world)))


(def world {:cwd [] :files []})
(def result-ex (reduce handle world (map row->data puzzle-ex)))
(pprint/pprint result-ex)

(defn get-directories [files]
  (set (flatten (map #(:parents %) files))))

(get-directories (:files result-ex))

(defn update-vals [map vals f]
  (reduce #(update-in % [%2] f) map vals))

(defn reduce-size [sizes file]
  (update-vals sizes (:parents file) (partial + (:size file))))

(defn process-files [files]
  (let [sizes (init-size-map (get-directories files))]
    (reduce reduce-size sizes files)))

(defn under-100k? [[_ v]] (< v 100000))

; example woof tough
(apply + (vals (filter under-100k? (process-files (:files result-ex)))))


