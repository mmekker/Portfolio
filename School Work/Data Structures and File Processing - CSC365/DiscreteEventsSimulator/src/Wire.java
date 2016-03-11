/**
 * a Wire object specifies a wiring between a source Pin and destination
 * Pin.  Source and destination indicate the direction of the signal.
 */
public class Wire {

    Pin source;
    Pin destination;

    Wire(Pin s, Pin d)
    {
		source = s;
		destination = d;
    }

    public Pin getSource() { return source; }
    public Pin getDestination() {return destination; }

    public void setSource(Pin s) {source = s; }
    public void setDestination(Pin d) {destination = d; }

    // you write this
    public String toString()
    {
    	return ("Source#: " + source.pinNumber + " :: Destination: " + destination.pinNumber);
    }
}
