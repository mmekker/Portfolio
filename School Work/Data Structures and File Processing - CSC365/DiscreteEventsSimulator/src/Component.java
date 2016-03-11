import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class Component
{
    public static final Integer UNDEF = new Integer(-1);

    /**
     * name of the componnt
     */
    protected String name;
    protected String type;
    /**
     * holds input values
     */
    protected ArrayList<Integer> inPins;
    /**
     * holds output values
     */
    protected ArrayList<Integer> outPins;
    /**
     * propagation delay for this component
     */
    protected double delay;
    /**
     * used to specify trace of simulation events
     */
    protected boolean trace;

    public Component()
    {
		name = "c";
		type = "Nor";
		inPins = new ArrayList<Integer>();
		outPins = new ArrayList<Integer>();
		delay = 0;
		trace = false;
    }
    /**
     * constructor to create component
     * @param name a component has a name
     * @param name a component has a type
     * @param numInputs the number of inputs into this component
     * @param numOutputs the number of outputs from this component
     */
    Component(String name, String type, int numInputs, int numOutputs,
	      double propagation_delay, boolean trace)
    {
		this.trace = trace;
		this.name = name;
		this.type = type;
		this.delay = propagation_delay;
		inPins = new ArrayList<Integer>(numInputs);
		outPins = new ArrayList<Integer>(numOutputs);
		for (int i=0; i<numInputs; ++i)
		{
		    inPins.add(i,Component.UNDEF);
		}	
		for (int i=0; i<numOutputs; ++i)
		{
		    outPins.add(i,Component.UNDEF);
		}
    }

    public void setDelay(double delay)
    {
    	this.delay = delay;
    }
    public String getName()
    { 
    	return name;
    }
    
    public String getType()
    {
    	return type;
    }

    public int getInput(int pin)
    {
    	return inPins.get(pin);
    }

    public void setInput(int pin, int v)
    {
    	inPins.set(pin,v);
    }

    public int getOutput(int pin)
    {
    	return outPins.get(pin);
    }

    public void  setOutput(int pin, int v)
    {
    	outPins.set(pin,v);
    }

    /**
     * constructs a string representation of this component suitable for trace
     * @return string representation of the component
     */
    public  String toString()
    {
    	return ("Name: " + name + ", Type: " + type + ", Output(0): " + getOutput(0));
    }
    
    /**
     * Propagate the signal from inputs (inPins) to outputs (outPins).
     * Create an event for every component connected to outputs (outPins)
     * at currentTime + component delay time.
     * @pre. We assume this is called as the result of an event occurring.
     * @param currentTime the current simulation time
     */
    public void propagate(double currentTime)
    {
    	ArrayList<Component> el = DS.el;
    	if(this.compute())
    	{
    		System.out.println("Successful Computation of " + this.name);
    		for(Wire a: DS.pinList.get(this))
			{
				a.destination.c.setInput(a.destination.pinNumber, this.getOutput(a.source.pinNumber));
    			System.out.println("Adding Event: " + a.destination.c);
				DS.eventList.add(new Event(a.destination.c, currentTime + delay));
			}
    	}
    	else
    	{
    		System.out.println("Failed Computation of " + this.name);
    	}
    	return;
    }
    
    /**
     * Computes the output value of the component
     * Compute must check that all inputs are defined before computing the
     * output and updating the output pins.
     * If it can compute an output, then for every component its outputs are
     * attached to, it adds a new Event on the eventList with the eventTime
     * as currentTime + the component propagation time.  
     */  
    public abstract boolean compute();
    
}

