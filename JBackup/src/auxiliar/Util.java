package auxiliar;

public class Util {
	public static String bytesToHex(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<bytes.length;i++){
			if(bytes[i]<(byte)16) sb.append(0);
			sb.append((Integer.toHexString(bytes[i])));
		}
		return sb.toString();
	}
}
