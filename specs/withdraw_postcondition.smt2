; Look for a valid withdrawal that violates either postcondition.
(set-logic QF_LIA)

(define-fun withdraw ((balance Int) (amount Int)) Int
  (- balance amount))

(declare-const balance Int)
(declare-const amount Int)

(assert (>= balance 0))
(assert (>= amount 0))
(assert (<= amount balance))

; A satisfying assignment would be a counterexample.
(assert
  (or
    (< (withdraw balance amount) 0)
    (not (= (withdraw balance amount) (- balance amount)))))

(check-sat)
