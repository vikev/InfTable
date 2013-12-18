package eu.vikev.android.inftable.custom;

import java.util.Calendar;
import java.util.Locale;

public class MyTime {
	private int hour;
	private int minutes;

	public MyTime(int hour, int minutes) {
		this.hour = hour;
		this.minutes = minutes;
	}

	public MyTime(String t) {
		String tmp = t.replace(":", "");
		int time = Integer.parseInt(tmp);
		hour = time / 100;
		minutes = time % 100;
	}

	public int toInt() {
		return hour * 100 + minutes;
	}

	@Override
	public String toString() {
		return String.format(Locale.UK, "%d:%02d", hour, minutes);
	}

	/**
	 * @return Current time as integer. Format: hhmm
	 */
	public static int CURRENT_TIME() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minutes = Calendar.getInstance().get(Calendar.MINUTE);
		int time = hour * 100 + minutes;
		return time;
	}

	/**
	 * Get what day is today. <strong>Saturday and Sunday are returned as
	 * Monday!</strong>
	 * 
	 * @return Current day: Calendar.MONDAY to Calendar.FRIDAY
	 */
	public static int CURRENT_DAY() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day > Calendar.FRIDAY) {
			day = Calendar.MONDAY;
		}
		return day;
	}

	/**
	 * Gets the current semester.
	 * 
	 * @return Current semester.<br/>
	 *         1 -Semester 1 (From July to December) <br/>
	 *         2 - Semester 2 (From January to June)
	 */
	public static int CURRENT_SEMESTER() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		if (month >= Calendar.JULY) {
			return 1;
		} else {
			return 2;
		}
	}

}
