import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

public class Circuit {

    public Circuit()
    {   
	    //simulate a RS f/f as a nor latch
		DS.pinList = new HashMap<Component, ArrayList<Wire>  >();
    	DS.el = new ArrayList<Component>();
		DS.eventList = new CompleteHeap<Event>();
		
		// inputs
		Input r = new Input("R",1,1,true);
		Input s = new Input("S",1,1,true);
		// gates
		Nor g1 = new Nor("G1",2,1,true);
		Nor g2 = new Nor("G2",2,1,true);
		//outputs
		Output q = new Output("Q",1,1,true);
		Output qnot = new Output("Qnot",1,1,true);
		
		// pin list
		
		DS.pinList.put(r, new ArrayList<Wire>());
		DS.pinList.put(s, new ArrayList<Wire>());
		DS.pinList.put(g1,new ArrayList<Wire>());
		DS.pinList.put(g2,new ArrayList<Wire>());
		DS.pinList.put(q, new ArrayList<Wire>());
		DS.pinList.put(qnot, new ArrayList<Wire>());
		DS.el.add(r);
		DS.el.add(s);
		DS.el.add(g1);
		DS.el.add(g2);
		DS.el.add(q);
		DS.el.add(qnot);
		ArrayList<Wire> al;
		
		// make all the connections
		
		al = DS.pinList.get(r);
		al.add(new Wire(new Pin(r,0),new Pin(g2,1)));
		
		al = DS.pinList.get(s);
		al.add(new Wire(new Pin(s,0),new Pin(g1,0)));
		
		al = DS.pinList.get(g1);
		al.add(new Wire(new Pin(g1,0),new Pin(g2,0)));
		al.add(new Wire(new Pin(g1,0),new Pin(q,0)));
		
		al = DS.pinList.get(g2);
		al.add(new Wire(new Pin(g2,0),new Pin(g1,1)));
		al.add(new Wire(new Pin(g2,0), new Pin(qnot,0)));
		
		// initialize state
		// requirement:  SR = 0
		
		r.setInput(0,1);
		s.setInput(0,0);
		/*g1.setOutput(0,1);
		q.setInput(0,1);
		q.setOutput(0,1);	
		g2.setInput(0,q.getOutput(0));*/
		
		
		double startTime = 0.0;
		double endTime;
		boolean trace = true;
	
		DS.eventList.add(new Event(r,startTime));
		DS.eventList.add(new Event(s,startTime));
		
		
		// constraint:  rs=0  (r and s can't both be 1)
		
		//q(t)  s(t)  r(t) | q(t+1)
		//-----------------|-------
		// 0     0     0       0
		// 0     0     1       0
		// 0     1     1      ???
		// 0     1     0       1
		// 1     1     0       1
		// 1     1     1      ??? 
		// 1     0     1       0
		// 1     0     0       1
		
		
		
		// start simulation
		DS ds = new DS();
		endTime = ds.go(trace);
		ds.report();
		System.out.println("End Time: " + endTime);
		
		
		/*DS.eventList.clear(); 
		r.setInput(0,1);
		DS.eventList.add(new Event(r,endTime));
		DS.eventList.add(new Event(s,endTime));
		endTime = ds.go(trace);
		ds.report();
		System.out.println("End Time: " + endTime);*/
    	
    	
    	/*DS.pinList = new HashMap<Component, ArrayList<Wire>  >();
    	DS.el = new ArrayList<Component>();
		DS.eventList = new CompleteHeap<Event>();
		Input r = new Input("R",1,1,true);
		Input s = new Input("S",1,1,true);
		Not g1 = new Not("not",1,1,true);
		And g2 = new And("and",2,1,true);
		//Nand g2 = new Nand("nand",2,1,true);
		//Or g2 = new Or("or",2,1,true);
		Output q = new Output("Q",1,1,true);
		DS.pinList.put(r, new ArrayList<Wire>());
		DS.pinList.put(s, new ArrayList<Wire>());
		DS.pinList.put(g1,new ArrayList<Wire>());
		DS.pinList.put(g2,new ArrayList<Wire>());
		DS.pinList.put(q, new ArrayList<Wire>());
		DS.el.add(r);
		DS.el.add(s);
		DS.el.add(g1);
		DS.el.add(g2);
		DS.el.add(q);
		ArrayList<Wire> al;
		al = DS.pinList.get(r);
		al.add(new Wire(new Pin(r,0),new Pin(g2,0)));
		al = DS.pinList.get(s);
		al.add(new Wire(new Pin(s,0),new Pin(g1,0)));
		al = DS.pinList.get(g1);
		al.add(new Wire(new Pin(g1,0),new Pin(g2,1)));
		al = DS.pinList.get(g2);
		al.add(new Wire(new Pin(g2,0),new Pin(q,0)));
		r.setInput(0,0);
		s.setInput(0,0);
		double startTime = 0.0;
		double endTime;
		boolean trace = true;
		DS.eventList.add(new Event(r,startTime));
		DS.eventList.add(new Event(s,startTime));
		DS ds = new DS();
		endTime = DS.go(trace);
		ds.report();
		System.out.println("End Time: " + endTime);*/
		
		
    }//end constructor
    

    // yes, I could have defined a Main class.  Do whatever you like
    public static void main(String [] args)
    {
    	Circuit c = new Circuit();
    }
}
