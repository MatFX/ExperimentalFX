package tools.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Alles was man sonst noch brauch
 * @author Matthias
 *
 */
public class Tools 
{
	public static String toHexString(int value, boolean fill)
	{
		boolean leading = true;
		StringBuffer buffer = new StringBuffer();

		for (int i = 3; i >= 0; i--)
		{
			byte digit = (byte)(value >>> i * 8);

			if (digit != 0)
				leading = false;
			
			if ((leading && fill) |!leading)
				buffer.append(toHexString(digit));
		}

		return buffer.toString();
	}	

	public static String toHexString(byte value)
	{
		char[] chars = new char[2];
		chars[0] = digits[(value >>> 4) & 0xF];
		chars[1] = digits[value & 0x0F];
		return new String(chars);
	}
	

    private final static char[] digits = 
    {
    	'0' , '1' , '2' , '3' , '4' , '5' ,
    	'6' , '7' , '8' , '9' , 'A' , 'B' ,
    	'C' , 'D' , 'E' , 'F'
    };
    
    public static Object cloneObject(Object objectToClone) throws IOException
    {
    	
    	
    	//ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
        		ObjectOutputStream oos = new ObjectOutputStream(bos);
        		)
        {
           oos.writeObject(objectToClone);  
           oos.flush();               
           ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); 
           ois = new ObjectInputStream(bin);                  
           return ois.readObject(); 
        }
        catch(Exception e)
        {
           System.out.println("Exception in ObjectCloner = " + e);
        }
        finally
        {
        	ois.close();
        }
        return null;
    }
    
    public static void main(String[] args)
    {
    	System.out.println(" " + Tools.getRange(0D, 2500D));
    	System.out.println(" " + Tools.getRange(-500D, 2500D));
    	System.out.println(" " + Tools.getRange(+500D, 2500D));
    	
    	System.out.println(" " + Tools.getRange(-1000D, -1500D));
    	
    }

	public static double getRange(double minWatt, double maxWatt) 
	{
		double resultValue = maxWatt - minWatt;
		if(resultValue < 0)
			return resultValue *-1;
		return resultValue;
	}

}
