(ns grammars.core
  (:import [grammars CYK]))


(def gr 
  {:A (list [:B :C]
            [:P :L])
   :B (list [:Z :V])
   :C (list [:E :E])
   :Z (list [:K :L])
   :K (list "z")
   :V (list "v")
   :L (list "pl")
   :P (list "p")
   :E (list "e")})

(def gr1 
  {
   :expr (list "0"
               "1"
               [:expr :term])
   :term (list [:op :expr])
   :op (list "and" "or")
   })


(defn parse [gr words]
  (binding [*out* *err*]
    (println "parsing " words)
    (.parse (CYK. gr words))
    (newline)))
     
(defn run[]
  (parse gr ["z" "pl" "v" "e" "e"])
  (parse gr ["p" "pl"])
  (parse gr1 ["0" "or" "1"]))


(def -main run)


(defn find-first [grammar start-symbol]

  (declare fn1 fn2 first-token)
  
  (defn fn1 [productions]
    (when-not (empty? productions)
      (let [first-production (first productions)]
        (if (empty? first-production)
          (fn1 (rest productions))
          (let [result1 (fn2 first-production)
                result2 (fn1 (rest productions))]
            (into result1 result2 ))))))
  
  (defn fn2 [production]
    (if (vector? production)
      (let [first-sym (first production)]
        (if (keyword? first-sym)
          (first-token first-sym)
          #{first-sym}))
      #{production}))
  
  (defn first-token [sym]
    (let [productions (grammar sym)]
      (fn1 productions)))
  
  (first-token start-symbol))
    

;; Grammars
(def zero-one 
  {:S (list ["0" :S "1"]
            ["0" "1"])
   :B (list "a")})

(find-first zero-one :S)

(def prefix
  {:S (list ["+" :S :S]
            ["*" :S :S])})

(find-first prefix :S)

(def parantheses
  {:S (list ["(" :S ")" :S]
            [])})

(find-first parantheses :S)

(def abba
  {:S (list ["a" :S "b" :S]
            ["b" :S "a" :S]
            [])})

(find-first abba :S)      

(def bool-expr
  {:bexpr (list [:bterm "or" :bexpr]
                [:bterm])
   
   :bterm (list [:bfactor "and" :bterm]
                [:bfactor])
   
   :bfactor (list ["not" :bfactor]
                  ["(" :bexpr ")"]
                  ["true"]
                  ["false"]) })

(find-first bool-expr :bexpr)

          
          

