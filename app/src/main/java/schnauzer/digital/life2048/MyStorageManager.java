package schnauzer.digital.life2048;

/**
 * Created by Rogelio on 11/12/2015.
 */
public class MyStorageManager {
    public byte booleanToByte (boolean vIn) {
        return (byte)(vIn?1:0);
    }

    public boolean byteToBoolean (byte vIn) {
        return vIn!=0;
    }

    public String booleanToString (boolean vIn) {
        if (vIn==false)
            return "Off";
        return "On";
    }

    public boolean stringToBoolean (String vIn) {
        if (vIn.equalsIgnoreCase("Off"))
            return false;
        if (vIn.equalsIgnoreCase("On"))
            return true;
        return false;
    }
}
