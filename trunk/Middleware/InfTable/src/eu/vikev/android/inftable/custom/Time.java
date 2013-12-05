package eu.vikev.android.inftable.custom;

public class Time {
	private int hour;
	private int minutes;

	public Time(int hour, int minutes) {
		this.hour = hour;
		this.minutes = minutes;
	}

	public Time(String t) {
		t = t.replace(":", "");
		int time = Integer.parseInt(t);
		hour = time / 100;
		minutes = time % 100;
	}

	public int toInt() {
		return hour * 100 + minutes;
	}

	@Override
	public String toString() {
		return String.format("%d:%02d", hour, minutes);
	}

}
