
/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.sql.Time;
import java.util.Calendar;

public class Test {
	public static void main(final String[] args) {
		System.out.println("****************************************");
		final Time timeFrom = new Time(14400000);
		final Time timeTo = new Time(50400000);
		final Calendar instance = Calendar.getInstance();
		instance.getTimeInMillis();
		final Time currentTime = new Time(instance.getTimeInMillis());
		System.out.println(currentTime);
		final int currentHour = Calendar.HOUR_OF_DAY;
		final int currentMinut = Calendar.MINUTE;
		System.out.println(currentHour);
		System.out.println(currentMinut);

		// currentTime.set
		// Time curentCompare = new Time();

		System.out.println(instance);
		System.out.println(currentTime);
		if (currentTime.after(timeFrom) && currentTime.before(timeTo)) {
			System.out.println(true);
		}
	}
}

// 1399113541116