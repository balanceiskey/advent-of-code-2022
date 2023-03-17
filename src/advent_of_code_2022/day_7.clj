(ns advent-of-code-2022.day-7
  (:require [clojure.string :as str]
            [clojure.walk :as walk]))

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

(defn file-handler [size filename world]
  (assoc-in world (conj (:cwd world) (keyword (clean-filename filename))) {:size size :type :file :name filename }))

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


(def state {:cwd [] })

(clojure.pprint/pprint map-ex)
(clojure.pprint/pprint (reduce handle state (map row->data puzzle-ex)))

; okay so we've got this neat data structure:

(def ex-structure (reduce handle state (map row->data puzzle-ex)))

; if we wanted to keep things basic, and just wanted to see if we could get the
; the size of a specific directory, like the root `:/`, we'd need to first discover all files (things with :type file)
; and return their sizes.
{:cwd [:/ :d],
 :/
 {:b.txt {:size 14848514, :type :file, :name "b.txt"},
  :c.dat {:size 8504156, :type :file, :name "c.dat"},
  :a
  {:f {:size 29116, :type :file, :name "f"},
   :g {:size 2557, :type :file, :name "g"},
   :h.lst {:size 62596, :type :file, :name "h.lst"},
   :e {:i {:size 584, :type :file, :name "i"}}},
  :d
  {:j {:size 4060174, :type :file, :name "j"},
   :d.log {:size 8033020, :type :file, :name "d.log"},
   :d.ext {:size 5626152, :type :file, :name "d.ext"},
   :k {:size 7214296, :type :file, :name "k"}}}}

; (get-in ex-structure [:/ :/b.txt])

(keys ex-structure)

; is it a file?

(contains? (:/ ex-structure) :type) ; nope
(contains? (get-in ex-structure [:/ :b-txt]) :type) ; yep

; okay, how do you walk a nested map?

(walk/prewalk-demo ex-structure)

(walk/prewalk (fn [x] (if integer? x 0)) ex-structure)

; (walk/postwalk integer? ex-structure)
; (walk/postwalk #(if (integer? %) (%) nil) ex-structure)

; (walk/postwalk identity ex-structure)

(def my-map {:a 1 :b {:c "hello" :d {:e 2.5}}})

; (filter number? (walk/postwalk identity my-map))











