/**
 *<p>
 * Component connections are referred to as Pins.  A Pin object refers to the
 * component object and it's pin number.  In real life, pins are numbered from
 * 0..n, and you must look the specs to determine their meanings (including
 * which are inputs and which are outputs).
 *</p>
 *<p>
 *We've made a simplifying (hopefully) assumption that pin numbers range from
 * 0..n for inputs, and 0..n'  for outputs.  Hope that works for y'all.
 *</p>
 */
public class Pin {
    int pinNumber;
    Component c;

    Pin()
    {
       pinNumber = -1;
       c = null;
    }

    Pin(Component c, int pin)
    {
		this.c = c;
		pinNumber = pin;
    }

    int getPin()
    {
    	return pinNumber;
    }

    Component getComponent()
    {
    	return c;
    }

    public String toString()
    {
    	return ("Pin#: " + pinNumber + " :: Component name: " + this.c.name);
    }

}
