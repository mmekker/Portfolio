public class Nor extends Component {


    public Nor(String name, int nInputs, int nOutputs, boolean trace)
    {
    	super(name,"Nor",nInputs,nOutputs,3.0,trace);
    }


    public boolean inputsPresent()
    {
		for (int i=0; i<inPins.size();i++)
		{
		    if (inPins.get(i) == Component.UNDEF) return false;
		}
		return true;
    }

    public boolean compute()
    {
    	if(inputsPresent())
    	{
	    	if(inPins.get(0) == 1 || inPins.get(1) == 1)
	    	{
	    		outPins.set(0,0);
	    		return true;
	    	}
	    	else if(inPins.get(0) == 0 && inPins.get(1) == 0)
	    	{
	    		outPins.set(0,1);
	    		return true;
	    	}
    	}
    	return false;
    }

}
