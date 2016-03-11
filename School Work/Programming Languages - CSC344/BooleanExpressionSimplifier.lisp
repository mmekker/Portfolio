;;;;This set of functions is used to symbollically simplify and evaluate boolean expressions
;;;;CSC 344 Homework Assignment 2
;;;;Created by Mike Mekker

(print "Start")

;;create expressions
(defun andexp (e1 e2) (list 'and e1 e2))
(defun orexp  (e1 e2) (list 'or e1 e2))
(defun notexp (e1) (list 'not e1))


#|fadsgfsdgfs
sdfgsdffgsdfg
sdfgsdfg|#


;;checks a list of ordered pairs and if e is the first argument in the ordered pair it returns the second value in the pair
;;if e isn't in any of the pairs then it returns "n"
;;Parameters:
	;list of ordered pairs ==> bindings
	;an element ==> e
(defun checkForBoundValue (bindings e) 
	;;debug
	; (print "////////////////////////")
	; (print "carcar bindings = e")
	; (print (equal (car(car bindings)) e))
	; (print "carcar bindings")
	; (print (car(car bindings)))
	; (print "e")
	; (print e)
	; (print "(cdr(car bindings))")
	; (print (car(cdr(car bindings))))
	; (print "////////////////////////")
	;;end debug
	(cond
		((null (car bindings)) "n") ;if there is nothing left in bindings then return "n"
		( (equal (car(car bindings)) e) (car(cdr(car bindings))) ) ;if the first element of the first element = e then return the second element of the first element
		(t (checkForBoundValue (cdr bindings) e))	
	)
)


;;bind-values function
;;recursively goes through function and replaces elements with their bound values
;;Parameters
	;bindings in list of form '((x 1) (y nil))
	;expression (list)
;;returns an expression
(defun bind-values (bindings exp) 
	;;debug
	; (print "////////////////////////")
	; (print "(and (atom (car exp)) (checkForBoundValue bindings (car exp)) n)")
	; (print (and (atom (car exp)) (checkForBoundValue bindings (car exp)) "n"))
	; (print "(and (atom (car exp)) (not(equal(checkForBoundValue bindings (car exp)) n)))")
	; (print (and (atom (car exp)) (not(equal(checkForBoundValue bindings (car exp)) "n"))))
	; (print "(cdr(car bindings))")
	; (print (cdr(car bindings)))
	; (print "////////////////////////")
	;;end debug

	(cond
		((null bindings) exp) 	;if bindings is empty
		((null exp) nil)		;if exp is empty
		;if car exp is an atom not a list
		;return a list with the current car exp and cdr exp passed through bind-values
		((and (atom (car exp)) (equal(checkForBoundValue bindings (car exp)) "n") ) (cons (car exp) (bind-values bindings (cdr exp))) )
		;if car exp is an atom not a list
		;return a list with the new car exp and cdr exp passed through bind-values
		((and (atom (car exp)) (not(equal (checkForBoundValue bindings (car exp)) "n"))) (cons (checkForBoundValue bindings (car exp)) (bind-values bindings (cdr exp))))
		;else car exp is a list so it should be passed back into bind-values along with cdr exp
		(t (cons (bind-values bindings (car exp)) (bind-values bindings (cdr exp))))
	)
)



(defun simplifyand (el1 el2)
		

	(let( (e1 (progn
					(cond
						((not(atom el1))
						(progn
							(cond
								((equal (car el1) 'and) (simplifyand (car(cdr el1)) (car(cdr(cdr el1)))))
								((equal (car el1) 'or) (simplifyor (car(cdr el1)) (car(cdr(cdr el1)))) )
								((equal (car el1) 'not) (simplifynot (car(cdr el1))) )
							)
						)
						)
						(t el1)
					)
			  )
		 )
		 (e2 (progn
					(cond
						((not(atom el2))
						(progn
							(cond
								((equal (car el2) 'and)(simplifyand (car(cdr el2)) (car(cdr(cdr el2)))))
								((equal (car el2) 'or) (simplifyor (car(cdr el2)) (car(cdr(cdr el2)))) )
								((equal (car el2) 'not) (simplifynot (car(cdr el2))) )
							)
						)
						)
						(t el2)
					)
			)
		 ))
	

	(cond 
		((and (equal e1 1) (equal e2 1)) 1) ;base cases
		((equal e1 nil) nil)
		((equal e2 nil) nil)
		((and (atom e1) (equal e2 nil)) nil)
		((and (equal e1 nil) (atom e2)) nil)
		((and (atom e1) (equal e2 1)) e1)
		((and (equal e1 1) (atom e2)) e2)
		((equal e1 e2) e1)
		((and (atom e1) (atom e2)) (andexp e1 e2))
		(t (andexp e1 e2))
	))
)

						
(defun simplifyor (el1 el2)

	(let( (e1 (progn
					(cond
						((not(atom el1))
						(progn
							(cond
								((equal (car el1) 'and) (simplifyand (car(cdr el1)) (car(cdr(cdr el1)))))
								((equal (car el1) 'or) (simplifyor (car(cdr el1)) (car(cdr(cdr el1)))) )
								((equal (car el1) 'not) (simplifynot (car(cdr el1))) )
							)
						)
						)
						(t el1)
					)
				)
		 )
	(e2 (progn
					(cond
						((not(atom el2))
						(progn
							(cond
								((equal (car el2) 'and)(simplifyand (car(cdr el2)) (car(cdr(cdr el2)))))
								((equal (car el2) 'or) (simplifyor (car(cdr el2)) (car(cdr(cdr el2)))) )
								((equal (car el2) 'not) (simplifynot (car(cdr el2))) )
							)
						)
						)
						(t el2)
					)
		)
	))
	
	(cond 
		((equal e1 1) 1)
		((equal e2 1) 1)
		((and (atom e1) (equal e2 nil)) e1)
		((and (equal e1 nil) (atom e2)) e2)
		(t (orexp e1 e2))
	))
)
(defun simplifynot (el)
	
	(let ((e (progn
					(cond
						((atom el) el)
						(t
							(progn
								(cond
									((equal (car el) 'and) (simplifyand (car(cdr el)) (car(cdr(cdr el)))))
									((equal (car el) 'or) (simplifyor (car(cdr el)) (car(cdr(cdr el)))) )
									((equal (car el) 'not) (simplifynot (car(cdr el)) ) )
								)
							)
						)
					)
			)
		))
	
	(cond
		((equal e nil) 1)
		((equal e 1) nil)
		((atom e) (notexp e))
		(t (notexp e))
	))
)


;;Checks if the first prefix is 'and', 'or', or 'not' and sends the expression to the correct function
;;else returns the expression
;;Parameters
	;Expression (list)
;;returns expression (list)
(defun simplify (exp)
	(cond
		( (equal(car exp) 'and) (simplifyand (car(cdr exp)) (car(cdr(cdr exp)))) )
		( (equal(car exp) 'or) (simplifyor (car(cdr exp)) (car(cdr(cdr exp)))) )
		( (equal(car exp) 'not) (simplifynot (car(cdr exp))) )
		(t exp)
	)
)
;simplify function
;recursively loops though each section of the expression simplifying it using the base cases
;(or x nil) => x; 
;(or nil x) => x; 
;(or 1 x) => 1;
;(or x 1) => 1; 
;(and x nil) => nil; X
;(and nil x) => nil; X
;(and x 1) => x; X
;(and 1 x) => x; X
;(not nil) => 1; X
;(not 1) => nil; X
;(not (and x y)) => (or (not x) (not y)); 
;(not (or x y)) => (and (not x) (not y)); 


;;evaluates expressions
;;takes bind values as parameters
;;simplifies the expression after it is run thought the bind-values function
;;Parameters:
	;bindings in list of form '((x 1) (y nil))
	;expression (list)
;;returns simplified expression
(defun evalexp (bindings exp) (simplify (bind-values bindings exp)))


;test expression
(setq p1 (andexp 'x (orexp 'x (andexp 'y (notexp 'z)))))

;'( (x nil) (y nil) (z 1) )
(setq bindValues '((z 1)))

(print "")
(print "simplify p1")
(print p1)
(print (bind-values bindValues p1))
(print "end value")
(print (evalexp bindValues p1))

;(print "")
;(print "simplify p2")
;(print p2)
;(print (bind-values bindValues p2))
;(print "end value")
;(print (evalexp bindValues p2))

;(print "")
;(print "simplify p3")
;(print p3)
;(print (bind-values bindValues p3))
;(print "end value")
;(print (evalexp bindValues p3))

;(print "")
;(print "simplify p4")
;(print p4)
;(print (bind-values bindValues p4))
;(print "end value")
;(print (evalexp bindValues p4))

;(print "")
;(print "simplify p5")
;(print p5)
;(print (bind-values bindValues p5))
;(print "end value")
;(print (evalexp bindValues p5))

;(print "")
;(print "simplify p6")
;(print p6)
;(print (bind-values bindValues p6))
;(print "end value")
;(print (evalexp bindValues p6))

(print "End")