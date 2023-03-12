(ns advent-of-code-2022.day-5
  (:require [clojure.string :as str]))

(def stack-ex [[\N \Z]
               [\D \C \M]
               [\P]])

(def stack [[\J \F \C \N \D \B \W]
            [\T \S \L \Q \V \Z \P]
            [\T \J \G \B \Z \P]
            [\C \H \B \Z \J \L \T \D]
            [\S \J \B \V \G]
            [\Q \S \P]
            [\N \P \M \L \F \D \V \B]
            [\R \L \D \B \F \M \S \P]
            [\R \T \D \V]])

(defn row->action [row]
  (let [values (map #(Integer/parseInt %) (take-nth 2 (reverse (str/split row #" "))))]
    (zipmap [:to :from :num] values)))

(defn apply-action [stacks action]
  (let [from-index (- (:from action) 1)
        to-index (- (:to action) 1)
        taken (reverse (take (:num action) (get stacks from-index)))
        new-to (vec (concat taken (get stacks to-index)))
        new-from (subvec (get stacks from-index) (:num action))]
    (-> stacks
        (assoc from-index new-from)
        (assoc to-index new-to))))

(defn apply-action-2 [stacks action]
  (let [from-index (- (:from action) 1)
        to-index (- (:to action) 1)
        taken (take (:num action) (get stacks from-index))
        new-to (vec (concat taken (get stacks to-index)))
        new-from (subvec (get stacks from-index) (:num action))]
    (-> stacks
        (assoc from-index new-from)
        (assoc to-index new-to))))

(defn get-rows [contents]
  (map
    row->action
    (filter
      (fn [row] (and
                  (> (count row) 0)
                  (= (subs row 0 4) "move")))
      (str/split contents #"\n"))))

; day 5 part 1
(def puzzle-input (slurp "./src/puzzle-inputs/day-5.txt"))
(def puzzle-ex (slurp "./src/puzzle-inputs/day-5-ex.txt"))

(reduce apply-action stack-ex (get-rows puzzle-ex))
(reduce apply-action stack (get-rows puzzle-input))

; day 5 part 2
(reduce apply-action-2 stack-ex (get-rows puzzle-ex))
(reduce apply-action-2 stack (get-rows puzzle-input))


