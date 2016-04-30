import java.sql.Time;
import java.util.Calendar;

public class Test {
	public static void main(String[] args) {
		System.out.println("****************************************");
		Time timeFrom = new Time(14400000);
		Time timeTo = new Time(50400000);
		Calendar instance = Calendar.getInstance();
		instance.getTimeInMillis();
		Time currentTime = new Time(instance.getTimeInMillis());
		System.out.println(currentTime);
		int currentHour = instance.HOUR_OF_DAY;
		int currentMinut = instance.MINUTE;
		System.out.println(currentHour);
		System.out.println(currentMinut);
		
//		currentTime.set
//		Time curentCompare = new Time();
		
		System.out.println(instance);
		System.out.println(currentTime);
		if (currentTime.after(timeFrom) && currentTime.before(timeTo)) {
			System.out.println(true);
		}
	}
}


// 1399113541116