import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

// DS is shorthand for DigitalSimulator

public class DS {

    //yes, I could have made this a class.  I was lazy.
    public static Map<Component, ArrayList<Wire> > pinList;
    public static CompleteHeap<Event> eventList;
    public static ArrayList<Component> el;
    public static boolean eventTrace;
    public DS(){}
    public static double go(boolean eventTrace)
    {
    	ArrayList<Component> el = DS.el;
		Event e;
		double currentTime = -1.0; // no events yet
		while (eventList.size() > 0)
		{
		    e = eventList.poll();
		    currentTime=e.getTime();
		    if(eventTrace)
		    {
		    	System.out.println("Event: " +e.toString());
		    }
		    e.getComponent().propagate(currentTime);
		}
		return currentTime;
    }


    public static void report()
    {
		System.out.println("\nSimulation Results");
		Set<Component> set = new HashSet<Component>();
		set = DS.pinList.keySet();
		for(Iterator<Component> itr = set.iterator(); itr.hasNext();)
		{
		    Component c = itr.next();
		    System.out.println(c.toString());
		}
		System.out.println("End Results\n");
    }
}
