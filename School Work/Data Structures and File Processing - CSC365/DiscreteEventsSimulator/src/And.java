public class And extends Component {


    public And(String name, int nInputs, int nOutputs, boolean trace)
    {
    	super(name,"And",nInputs,nOutputs,2.5,trace);
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
	    	if(inPins.get(0) == 1 && inPins.get(1) == 1)
	    	{
	    		outPins.set(0,1);
	    		return true;
	    	}
	    	else
	    	{
	    		outPins.set(0,0);
	    		return true;
	    	}
    	}
    	return false;
    }

}
