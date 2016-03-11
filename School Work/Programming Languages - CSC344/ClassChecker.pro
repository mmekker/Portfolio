varType(x,int).
varType(y,subClass).
varType(z,int).
varType(w,func).
class(main).
class(subClass).
isSubclass(subClass,main).
methodType(func,int,int).

hasMethod(main,func).
hasMethod(X,F) :- isSubclass(X,Y),
		  hasMethod(Y,F).

hasVar(main,x).
hasVar(main,y).
hasVar(main,z).
hasVar(X,V) :- isSubclass(X,Y),
	       hasVar(Y,V).

typeCheck(F,X) :- varType(X,Y), methodType(F,Y,_).



infer(A,B) :-  (varType(A,B)), not(methodType(B,_,_));
	       (varType(A,B)), methodType(B,_,C) -> write(C);
               (class(A)) ->  B='class';
               (methodType(A,_,B)).
