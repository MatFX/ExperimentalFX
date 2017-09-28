package tools.helper;

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

}
