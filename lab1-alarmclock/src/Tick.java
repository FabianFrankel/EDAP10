import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import clock.ClockOutput;

public class Tick {
	private ClockOutput out;
	private int time;
	private int alarmTime;
	private boolean toggleAlarm;
	private boolean activeAlarm;
	private long temp1;
	private Lock mutex;

	public Tick(ClockOutput out) {
		this.out = out;
		time = 0;
		mutex = new ReentrantLock();
		temp1 = 0;
		alarmTime = 0;
	}

	public void timeDisplay() {
		mutex.lock();
		time++;
		long t = System.currentTimeMillis();
		int hours = (time / 10000);
		time = time - hours * 10000;
		int min = time / 100;
		time = time - 100 * min;
		int sec = time;
		min = min + sec / 60;
		hours = hours + min / 60;
		sec = sec % 60;
		min = min % 60;
		hours = hours % 24;
		time = hours * 10000 + min * 100 + sec;
		out.displayTime(time);
		mutex.unlock();

		try {
			while (System.currentTimeMillis() < t + 1000) {
				Thread.sleep(0, 1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setTime(int newTime) {
		mutex.lock();
		time = newTime;
		mutex.unlock();
	}

	public void alarmCheck() {
		mutex.lock();
		if (toggleAlarm) {
			if (time == alarmTime) {
				temp1 = System.currentTimeMillis();
				activeAlarm = true;
			}
			if (activeAlarm && (System.currentTimeMillis() - temp1) < 20000) {
				out.alarm();
			}
		}
		mutex.unlock();
	}

	public void alarmSet(int alarm) {
		mutex.lock();
		alarmTime = alarm;
		mutex.unlock();
	}

	public void alarmToggle() {
		mutex.lock();
		toggleAlarm = !toggleAlarm;
		out.setAlarmIndicator(toggleAlarm);
		mutex.unlock();
	}

	public void stopAlarm() {
		mutex.lock();
		activeAlarm = false;
		mutex.unlock();
	}
}
