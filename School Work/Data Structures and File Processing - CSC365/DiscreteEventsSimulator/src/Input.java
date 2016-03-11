public class Input extends Component {


    Input(String name, int nInputs, int nOutputs, boolean trace) {
	super(name,"Input",nInputs,nOutputs,0.0,trace);
    }


    public boolean compute()
    {
		if (inPins.get(0)!= Component.UNDEF)
		{
		    outPins.set(0,inPins.get(0));
		    return true;
		}
		else
		{
		    return false;
		}
    }
}
