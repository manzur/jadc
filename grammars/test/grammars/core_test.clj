(ns grammars.core-test
  (:use clojure.test
        [clojure.string :only [split]]
        grammars.bool-exp))

(defmacro def-parsetest [name code expected]
  `(deftest ~name
     (println "Testing parsing for |" ~code "|")
     (let [~'words (split ~code #" +")
           ~'parseResult (parse ~'words)]
       (is (= ~'parseResult ~expected)))))
  

;; success tests

(def-parsetest test-1 "s" true)

(def-parsetest test-2 "if e then s" true)

(def-parsetest test-3 "if e then s else s" true)

(def-parsetest test-4 "while e do s" true)

(def-parsetest test-5 "begin s end" true)

(def-parsetest test-6 "begin s ; s end" true)

(def-parsetest test-7 "begin if e then s ; s end" true)

(def-parsetest test-8 "begin if e then s ; if e then s end" true)

(def-parsetest test-9 "begin if e then s ; if e then s else s end" true)

(def-parsetest test-10 "begin if e then s end" true)

(def-parsetest test-11 "begin if e then s else s end" true)

(def-parsetest test-12 "begin begin s end ; s end" true)

(def-parsetest test-13 "if e then begin s end else s" true)

(def-parsetest test-14 "if e then begin s end else while e do s" true)

(def-parsetest test-15 "if e then begin while e do if e then begin s end end else while e do s" true)

(def-parsetest test-16 "if e then begin while e do if e then begin s end end else while e do s" true)

(def-parsetest test-17 "while e do while e do while e do while e do while e do while e do s" true)

(def-parsetest test-18 "while e do while e do while e do while e do while e do while e do while e do while e do while e do while e do while e do while e do if e then while e do s" true)

(def-parsetest test-19 "while e do if e then while e do if e then while e do if e then while e do s" true)

;; fail tests
(def-parsetest test-20 "s ;" false)

(def-parsetest test-21 "if" false)

(def-parsetest test-22 "if e" false)

(def-parsetest test-23 "if e then" false)

(def-parsetest test-24 "if then s" false)

(def-parsetest test-25 "if e s" false)

(def-parsetest test-26 "e then s" false)

(def-parsetest test-27 "then s" false)

(def-parsetest test-28 "while do s" false)

(def-parsetest test-29 "while e s" false)

(def-parsetest test-30 "while e do" false)

(def-parsetest test-31 "begin s" false)

(def-parsetest test-32 "begin" false)

(def-parsetest test-33 "begin ; end" false)

(def-parsetest test-34 "begin s ; end" false)

(def-parsetest test-35 "begin s ; s" false)

(def-parsetest test-36 "begin begin s end ; end" false)

(def-parsetest test-37 "begin begin s end ; end" false)

(def-parsetest test-38 "end begin s end end" false)

(def-parsetest test-39 "end end s begin begin" false)

(def-parsetest test-40 "begin end s begin end" false)

(def-parsetest test-41 "begin while if then s" false)

(def-parsetest test-42 "begin while then s" false)

(def-parsetest test-43 "begin while" false)

(run-tests)