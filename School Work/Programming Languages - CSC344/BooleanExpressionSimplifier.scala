/**
 * This Assignment was made for CSC344 - Programming Languages
 * Author: Mike Mekker
 * This program simplifies boolean expressions
 */
object HW3 {
  
  def main(args: Array[String])
  {
    //input should be as in the lisp assignment.
    //For example, "(and (or x (not y)) z)" should be inputed as "andexp((orexp("x",(notexp("y")))),"z")"
    //var exp1 = andexp((orexp("x",(notexp("y")))),"z")
    //println("exp1 = " + exp1)
    //println(evalExp(exp1))
    //var exp2 = andexp("x", orexp("x", andexp("y", notexp("z"))))
    //println("exp2 = " + exp2)
    //println(evalExp(exp2))
    //var exp3 = andexp(andexp("z", "0"), orexp("x", "1"))
    //println("exp3 = " + exp3)
    //println(evalExp(exp3))
    var exp4 = notexp(orexp("a", andexp("b", "c")))
    println("exp4 = " + exp4)
    println(evalExp(exp4))
    
    
    
    /*sdsdfgsdfg
	dsfgsdfgsdf
	fgdfghfg*/
    
    
  }
  
  def evalExp(exp : List[Any]) : Any = 
  {
    return normalForm(simplify(bindValues(exp, findBindings(exp))))
  }
  
  def normalForm(exp : Any) : Any =
  {
    if(exp.isInstanceOf[List[Any]] &&  exp.asInstanceOf[List[Any]].head == "and")
    {
      var e1 : Any = exp.asInstanceOf[List[Any]].tail.head
      var e2 : Any = exp.asInstanceOf[List[Any]].tail.tail.head
      
      //Normal Form the 2 elements
      if(exp.asInstanceOf[List[Any]].tail.head.isInstanceOf[List[Any]])//if the first element is a list
      {
        e1 = normalForm(exp.asInstanceOf[List[Any]].tail.head.asInstanceOf[List[Any]])//Normal Form it
      }
      if(exp.asInstanceOf[List[Any]].tail.tail.head.isInstanceOf[List[Any]])//if the second element is a list
      {
        e2 = normalForm(exp.asInstanceOf[List[Any]].tail.tail.head.asInstanceOf[List[Any]])//Normal Form it
      }
      return ("(" + e1 + " and " + e2 + ")")
    }
    else if(exp.isInstanceOf[List[Any]] &&  exp.asInstanceOf[List[Any]].head == "or")
    {
      var e1 : Any = exp.asInstanceOf[List[Any]].tail.head
      var e2 : Any = exp.asInstanceOf[List[Any]].tail.tail.head
      
      //Normal Form the 2 elements
      if(exp.asInstanceOf[List[Any]].tail.head.isInstanceOf[List[Any]])//if the first element is a list
      {
        e1 = normalForm(exp.asInstanceOf[List[Any]].tail.head.asInstanceOf[List[Any]])//Normal Form it
      }
      if(exp.asInstanceOf[List[Any]].tail.tail.head.isInstanceOf[List[Any]])//if the second element is a list
      {
        e2 = normalForm(exp.asInstanceOf[List[Any]].tail.tail.head.asInstanceOf[List[Any]])//Normal Form it
      }
      
      return ("(" + e1 + " or " + e2 + ")")
    }
    else if(exp.isInstanceOf[List[Any]] &&  exp.asInstanceOf[List[Any]].head == "not")
    {
      var e : Any = exp.asInstanceOf[List[Any]].tail.head
      
      //Normal Form the element
      if(exp.asInstanceOf[List[Any]].tail.head.isInstanceOf[List[Any]])//if the element is a list
      {
        e = normalForm(exp.asInstanceOf[List[Any]].tail.head.asInstanceOf[List[Any]])//Normal Form it
      }
      
      return ("(not " + e + ")")
    }
    
    else return exp
  }
  
  def andexp(e1:Any, e2:Any) : List[Any] = 
  {
    return List("and", e1, e2)
  }
  def orexp(e1:Any, e2:Any) : List[Any] = 
  {
    return List("or", e1, e2)
  }
  def notexp(e:Any) : List[Any] = 
  {
    return List("not", e)
  }
  
  
  
  def findBindings(exp: List[Any]) : List[(String, String)] = 
  {
    var replacement : String = null
    var replaced : String = null
    if(exp == Nil)
    {
      return Nil
    }
    else if(exp.head.isInstanceOf[List[Any]])
    {
      return findBindings(exp.head.asInstanceOf[List[Any]]) ::: findBindings(exp.tail)
    }
    else if(exp.head != "and" && exp.head != "or" && exp.head != "not" && exp.head != Nil && exp.head != "0" && exp.head != "1")
    {
      replaced = exp.head.toString()
      replacement = readLine("Enter the binding for '" + replaced + "'.  Enter 1 for true and 0 for false.  ")
      return (replaced, replacement) :: findBindings(exp.tail)
    }
    return findBindings(exp.tail)
    
  }
  
  def bindValues(exp:List[Any], bindings : List[(String, String)]) : List[Any] = 
  {
    //println("Binding values for " + exp)
    if(exp == Nil) return Nil
    else if(bindings == null) return exp
    else
    {
      if(!exp.head.isInstanceOf[List[Any]]) //if head isn't a list
      {
        if(checkValue(exp.head.asInstanceOf[String], bindings) == "n") // no bound value found
        {
          return exp.head :: bindValues(exp.tail, bindings)
        }
        else //bound value found
        {
          return checkValue(exp.head.asInstanceOf[String], bindings) :: bindValues(exp.tail, bindings)
        }
      }
      else //head is a list
      {
        return bindValues(exp.head.asInstanceOf[List[Any]], bindings) :: bindValues(exp.tail, bindings)
      }
    }
  }
  def checkValue(e : String, bindings : List[(String, String)]) : String = 
  {
    //println("Checking for bound value for " + e)
    //println("Bindings = " + bindings)
    if(bindings.isEmpty) return "n" //no binding for said e
    else if(bindings.head._1 == e) //if the current head of bindings is e then return the  replacement
    {
      return bindings.head._2
    }
    return checkValue(e, bindings.tail) //else keep looking
  }
  
  
  
  
  
  //TODO Simplify function
        //including simplifyAnd, simplifyOr, and simplifyNot
  def simplify(exp : List[Any]) : Any = 
  {
    if(exp.head == "and") return simplifyAnd(exp)
    else if(exp.head == "or") return simplifyOr(exp)
    else if(exp.head == "not") return simplifyNot(exp)
    else return exp
  }
  
  def simplifyAnd(exp : List[Any]) : Any = 
  {
    var e1 : Any = exp.tail.head
    var e2 : Any = exp.tail.tail.head
    
    //Simplify the 2 elements
    if(exp.tail.head.isInstanceOf[List[Any]])//if the first element is a list
    {
      e1 = simplify(exp.tail.head.asInstanceOf[List[Any]])//simplify it
    }
    if(exp.tail.tail.head.isInstanceOf[List[Any]])//if the second element is a list
    {
      e2 = simplify(exp.tail.tail.head.asInstanceOf[List[Any]])//simplify it
    }
    
    //simplify expression
    if(e1 == "1" && e2 == "1") return "1"
    else if(e1 == "0" || e2 == "0") return "0"
    else if(e1 == e2) return e1
    else if(e1 == "1") return e2
    else if(e2 == "1") return e1
    else if(e1.isInstanceOf[List[Any]] && !e2.isInstanceOf[List[Any]])
    {
      if(e1.asInstanceOf[List[Any]].head == "or")
      {
        return orexp(andexp(e1.asInstanceOf[List[Any]].tail.head, e2), andexp(e1.asInstanceOf[List[Any]].tail.tail.head, e2))
      }
    }
    else if(e2.isInstanceOf[List[Any]] && !e1.isInstanceOf[List[Any]])
    {
      if(e2.asInstanceOf[List[Any]].head == "or")
      {
        return orexp(andexp(e1, e2.asInstanceOf[List[Any]].tail.head), andexp(e1, e2.asInstanceOf[List[Any]].tail.tail.head))
      }
    }
    else return andexp(e1, e2)
  }
  
  def simplifyOr(exp : List[Any]) : Any = 
  {
    var e1 : Any = exp.tail.head
    var e2 : Any = exp.tail.tail.head
    
    //Simplify the 2 elements
    if(exp.tail.head.isInstanceOf[List[Any]])//if the first element is a list
    {
      e1 = simplify(exp.tail.head.asInstanceOf[List[Any]])//simplify it
    }
    if(exp.tail.tail.head.isInstanceOf[List[Any]])//if the second element is a list
    {
      e2 = simplify(exp.tail.tail.head.asInstanceOf[List[Any]])//simplify it
    }
    
    //simplify expression
    if(e1 == "1" || e2 == "1") return "1"
    else if(e1 == "0" && e2 == "0") return "0"
    else if(e1 == e2) return e1
    else if(e1 == "0") return e2
    else if(e2 == "0") return e1
    else if(e1.isInstanceOf[List[Any]] && !e2.isInstanceOf[List[Any]])
    {
      if(e1.asInstanceOf[List[Any]].head == "and")
      {
        return andexp(orexp(e1.asInstanceOf[List[Any]].tail.head, e2), orexp(e1.asInstanceOf[List[Any]].tail.tail.head, e2))
      }
    }
    else if(e2.isInstanceOf[List[Any]] && !e1.isInstanceOf[List[Any]])
    {
      if(e2.asInstanceOf[List[Any]].head == "and")
      {
        return andexp(orexp(e1, e2.asInstanceOf[List[Any]].tail.head), orexp(e1, e2.asInstanceOf[List[Any]].tail.tail.head))
      }
    }
    else return orexp(e1, e2)
  }
  
  def simplifyNot(exp : List[Any]) : Any = 
  {
    var e : Any = exp.tail.head
    
    //Simplify the element
    if(exp.tail.head.isInstanceOf[List[Any]])//if the first element is a list
    {
      e = simplify(exp.tail.head.asInstanceOf[List[Any]])//simplify it
    }
    
    //simplify expression
    if(e == "1") return "0"
    else if(e == "0") return "1"
    else if(e.isInstanceOf[List[Any]])
    {
      if(e.asInstanceOf[List[Any]].head == "and")
      {
        return orexp(simplifyNot(notexp(e.asInstanceOf[List[Any]].tail.head)), simplifyNot(notexp(e.asInstanceOf[List[Any]].tail.tail.head)) )
      }
      else if(e.asInstanceOf[List[Any]].head == "or")
      {
        return andexp(simplifyNot(notexp(e.asInstanceOf[List[Any]].tail.head)), simplifyNot(notexp(e.asInstanceOf[List[Any]].tail.tail.head)))
      }
    }
    else return notexp(e)
  }
  
  
}