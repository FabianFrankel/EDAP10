import clock.ClockOutput;

public class Time implements Runnable {
	private Tick tock;

	public Time(Tick tock) {
		this.tock = tock;
	}

	public void run() {
//		long t1 = 0;
		while (true) {
			tock.timeDisplay();
			tock.alarmCheck();
		}
	}
}
