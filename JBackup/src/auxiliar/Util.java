package auxiliar;

public class Util {
	public static String bytesToHex(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<bytes.length;i++)sb.append(Integer.toHexString(bytes[i]));
		return sb.toString();
	}
}
