package eu.vikev.android.inftable.custom;

public class Time {
	private int hour;
	private int minutes;

	public Time(int hour, int minutes) {
		this.hour = hour;
		this.minutes = minutes;
	}

	public int toInt() {
		return hour * 100 + minutes;
	}

	@Override
	public String toString() {
		return hour + ":" + minutes;
	}

}
