(ns grammars.bool-exp)

(println "simple if-while-statement expression parser")

(def rules 
  [
   ;; zero index placeholder
   :P :P
   
   :start [:stmt]
   :stmt  ["if" "e" "then" :stmt]  
   :stmt  ["if" "e" "then" :stmt "else" :stmt]
   :stmt  ["while" "e" "do" :stmt]
   :stmt  ["begin" :list "end"]
   :stmt  ["s"]
   :list  [:list ";" :stmt]
   :list  [:stmt]
   ])
                

(def parse-table
  [
   ;; state #0
   :P
   
   ;; state #1
   {
    "if"    [:shift 6]
    "while" [:shift 2]
    "then"  [:error "error symbol" :skip]
    "else"  [:error "if e then" 19]
    "begin" [:shift 8]
    "end"   [:error "error symbol" :skip]
    "do"    [:error "error symbol" :skip]
    ";"     [:error "error symbol" :skip]
    "e"     [:error "if" 6]
    "s"     [:shift 7]
    :stmt   [:goto  5]
    nil     [:error "missing statement" :eof]
    }
   
   ;; state #2
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:error "error symbol" :skip]
    "else"  [:error "error symbol" :skip]
    "begin" [:error "error symbol" :skip]
    "end"   [:error "error symbol" :skip]
    "do"    [:error "e" 3]
    ";"     [:error "error symbol" :skip]
    "e"     [:shift 3]
    "s"     [:error "error symbol" :skip]
    nil     [:error "missing e do statement" :eof]
    }
   
   ;; state #3
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:error "error symbol" :skip]
    "else"  [:error "error symbol" :skip]
    "begin" [:error "do" 4]
    "end"   [:error "error symbol" :skip]
    "do"    [:shift 4]
    ";"     [:error "error symbol" :skip]
    "e"     [:error "error symbol" :skip]
    "s"     [:error "error symbol" :skip]
    nil     [:error "missing do statement" :eof]
    }
   
   ;; state #4
   {
    "if"    [:shift 6]
    "while" [:shift 2]
    "then"  [:error "if e" 9]
    "else"  [:error "if e then" 19]
    "begin" [:shift 8]
    "end"   [:error "error symbol" :skip]
    "do"    [:error "error symbol" :skip]
    ";"     [:error "error symbol" :skip]
    "e"     [:error "if" 6]
    "s"     [:shift 5]
    :stmt   [:goto 13]
    nil     [:error "missing statement" :eof]
    }
   
   ;; state #5
   {
    "if"    [:error "and rest of code is redundant" :skip-all]
    "while" [:error "and rest of code is redundant" :skip-all]
    "then"  [:error "and rest of code is redundant" :skip-all]
    "else"  [:error "and rest of code is redundant" :skip-all]
    "begin" [:error "and rest of code is redundant" :skip-all]
    "end"   [:error "and rest of code is redundant" :skip-all]
    "do"    [:error "and rest of code is redundant" :skip-all]
    ";"     [:error "and rest of code is redundant" :skip-all]
    "e"     [:error "and rest of code is redundant" :skip-all]
    "s"     [:error "and rest of code is redundant" :skip-all]
    nil     [:accept]
    }

   ;; state #6
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:error "e" 9]
    "else"  [:error "if e then" 19]
    "begin" [:error "error symbol" :skip]
    "end"   [:error "error symbol" :skip]
    "do"    [:error "error symbol" :skip]
    ";"     [:error "error symbol" :skip]
    "e"     [:shift 9]
    "s"     [:error "error symbol" :skip]
    nil     [:error "missing e then statement" :eof]
    }
   
   ;; state #7
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:reduce 6]
    "else"  [:reduce 6]
    "begin" [:error "error symbol" :skip]
    "end"   [:reduce 6]
    "do"    [:error "error symbol" :skip]
    ";"     [:reduce 6]
    "e"     [:error "error symbol" :skip]
    "s"     [:error "error symbol" :skip]
    nil     [:reduce 6]
    }
   
   ;; state #8
   {
    "if"    [:shift 6]
    "while" [:shift 2]
    "then"  [:error "if e" 9]
    "else"  [:error "if e then" 19]
    "begin" [:shift 8]
    "end"   [:error "statement" 11]
    "do"    [:error "error symbol" :skip]
    ";"     [:error "error symbol" :skip]
    "e"     [:error "if" 6]
    "s"     [:shift 7]
    :stmt   [:goto 20]
    :list   [:goto 11]
    nil     [:error "missing statement end" :eof]
    }
   
   ;; state #9
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "then" 10]
    "then"  [:shift 10]
    "else"  [:error "if e then" 19]
    "begin" [:error "then" 4]
    "end"   [:error "error symbol" :skip]
    "do"    [:error "error symbol" :skip]
    ";"     [:error "error symbol" :skip]
    "e"     [:error "error symbol" :skip]
    "s"     [:error "error symbol" :skip]
    nil     [:error "missing then statement" :eof]
    }
   
   ;; state #10
   {
    "if"    [:shift 6]
    "while" [:shift 2]
    "then"  [:error "if e" 9]
    "else"  [:error "if e then" 19]
    "begin" [:shift 8]
    "end"   [:error "error symbol" :skip]
    "do"    [:error "error symbol" :skip]
    ";"     [:error "error symbol" :skip]
    "e"     [:error "if" 6]
    "s"     [:shift 7]
    :stmt   [:goto 19]
    nil     [:error "missing statement" :eof]
    }

   ;; state #11
   {
    "if"    [:error ";" 18]
    "while" [:error ";" 18]
    "then"  [:error "error symbol" :skip]
    "else"  [:error "error symbol" :skip]
    "begin" [:error "error symbol" :skip]
    "end"   [:shift 12]
    "do"    [:error "error symbol" :skip]
    ";"     [:shift 18]
    "e"     [:error "error symbol" :skip]
    "s"     [:error "error symbol" :skip]
    :stmt   [:error "statement" 18]
    nil     [:error "missing end" :eof]
    }
   
   ;; state #12
   {
    "if"    [:reduce 5]
    "while" [:reduce 5]
    "then"  [:reduce 5]
    "else"  [:reduce 5]
    "begin" [:error ";" :reduce 5]
    "end"   [:reduce 5]
    "do"    [:error "error symbol" :skip]
    ";"     [:reduce 5]
    "e"     [:error "error symbol" :skip]
    "s"     [:error "error symbol" :skip]
    nil     [:reduce 5]
    }

   ;; state #13
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:reduce 4]
    "else"  [:reduce 4]
    "begin" [:error ";" :reduce 4]
    "end"   [:reduce 4]
    "do"    [:reduce 4]
    ";"     [:reduce 4]
    "e"     [:reduce 5]
    "s"     [:error "error symbol" :skip]
    nil     [:reduce 4]
    }

   ;; state #14
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:reduce 7]
    "else"  [:reduce 7]
    "begin" [:error ";" :reduce 7]
    "end"   [:reduce 7]
    "do"    [:reduce 7]
    ";"     [:reduce 7]
    "e"     [:reduce 7]
    "s"     [:error "error symbol" :skip]
    nil     [:reduce 7]
    }
   
   ;; state #15
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:reduce 3]
    "else"  [:reduce 3]
    "begin" [:error ";" :reduce 3]
    "end"   [:reduce 3]
    "do"    [:reduce 3]
    ";"     [:reduce 3]
    "e"     [:reduce 3]
    "s"     [:error "error symbol" :skip]
    nil     [:reduce 3]
    }
   
   ;; state #16
   {
    }
   
   ;; state #17
   {
    "if"    [:shift 6]
    "while" [:shift 2]
    "then"  [:error "if e" 9]
    "else"  [:error "error symbol" :skip]
    "begin" [:shift 8]
    "end"   [:error "statement" 11]
    "do"    [:error "error symbol" :skip]
    ";"     [:error ";" 20]
    "e"     [:error "if" 6]
    "s"     [:shift 7]
    :stmt   [:goto 15]
    nil     [:error "missing statement" :eof]
    }
   
   ;; state #18
   {
    "if"    [:shift 6]
    "while" [:shift 2]
    "then"  [:error "if e" 9]
    "else"  [:error "if e then" 19]
    "begin" [:shift 8]
    "end"   [:error "statement" 14]
    "do"    [:error "error symbol" :skip]
    ";"     [:error "error symbol" :skip]
    "e"     [:error "if" 6]
    "s"     [:shift 7]
    :stmt   [:shift 14]
    nil     [:error "missing statement" :eof]
    }
   
   ;; state #19
   {
    "if"    [:error "else" 17]
    "while" [:error "else" 17]
    "then"  [:error "error symbol" :skip]
    "else"  [:shift 17]
    "begin" [:error ";" :reduce 2]
    "end"   [:reduce 2]
    "do"    [:reduce 2]
    ";"     [:reduce 2]
    "e"     [:reduce 2]
    "s"     [:error "error symbol" :skip]
    nil     [:reduce 2]
    }
   
   ;; state #20
   {
    "if"    [:error "error symbol" :skip]
    "while" [:error "error symbol" :skip]
    "then"  [:reduce 8]
    "else"  [:reduce 8]
    "begin" [:error ";" :reduce 8]
    "end"   [:reduce 8]
    "do"    [:reduce 8]
    ";"     [:reduce 8]
    "e"     [:reduce 8]
    "s"     [:error "error symbol" :skip]
    nil     [:reduce 8]
    }
   
   ])


(defn head
  ([v] (head v 1))
  ([v cnt] (subvec v 0 (- (count v) cnt))))

(defn get-rule[index]
  (let [ruleIndex (* 2 index)
        ruleHead  (rules ruleIndex)
        ruleTail  (rules (+ 1 ruleIndex))]
    
    [ruleHead ruleTail]))

(defn reduce-stack [stack by]
  (head stack by))

(defn parse-internal [words stack error?]  
  (let [word (first words)
        currentState (last stack)
        action (or (get (parse-table currentState) word)
                   [:error "error symbol" :skip])]
    
    (case (first action) 
      
      :accept error? 
      
      :shift (let [state (second action)]
               (recur (rest words) (conj stack state) error?)) 
      
      :reduce (let [
                    ruleIndex (second action)                    
                    [ruleHead ruleTail] (get-rule ruleIndex)                    
                    
                    newStack (reduce-stack stack (count ruleTail))                                        
                    state (last newStack)                    
                    
                    nextAction (get (parse-table state) ruleHead)                                                            
                    newState (second nextAction)
                    newWords (into [ruleHead] words)
                    ]                
                (recur newWords newStack error?))
      
      :goto (let [newState (second action)]
              (recur (rest words) (conj stack newState) error?))

      :error (let [error-code (second action)
                   action-type (nth action 2)
                   eof-error (= :eof action-type)
                   skip-error (= :skip action-type)
                   skip-all (= :skip-all action-type)
                   reduce? (= :reduce action-type)]
               
               (case action-type
                 :eof (let [msg (second action)]
                             (println "unexpected eof" msg)
                             true)
                             
                 :skip (let [msg (second action)]
                                  (println msg word)
                                  (recur (rest words) stack true))
                 
                 :skip-all (let [msg (second action)]
                            (println "the" word msg)
                            true)
                 
                 :reduce (let [ruleIndex (nth action 3)
                               [ruleHead ruleTail] (get-rule ruleIndex)
                               newStack (reduce-stack stack (count ruleTail))
                               msg (second action)]
                           (println msg)
                           (recur (rest words) newStack true))
                 
                 (let [msg (second action)
                       newState (nth action 2)]
                   (println "missing" msg)
                   (recur words (conj stack newState) true)))))))

(defn parse [words]
  (not (parse-internal words [1] false)))

(defn start[]
  (let [words ["begin" "if" "e" "then" "s" "else" "if" "e" "then" "begin" "s" "end" "end"]
        parseResult (parse words)]
      (if parseResult 
        (println "parsing succeded")
        (println "parsing failed"))))

(def -main start)