import java.util.Calendar;
import java.util.Date;


public class Util {
	private static String[] meses = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};

	public static String mesAno(long data){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(data));
		return meses[cal.get(Calendar.MONTH)]+cal.get(Calendar.YEAR);
	}
}
