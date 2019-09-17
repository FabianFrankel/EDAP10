import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.*;
import clock.ClockInput;
import clock.ClockInput.UserInput;
import clock.ClockOutput;
import emulator.AlarmClockEmulator;

public class ClockMain {

	final static int CHOICE_SET_TIME = 1; // user set new clock time
	final static int CHOICE_SET_ALARM = 2; // user set new alarm time
	final static int CHOICE_TOGGLE_ALARM = 3; // user pressed both buttons simultaneously
	final static int CHOICE_OTHER = 4; // user pressed a button, but didn't change time

	public static void main(String[] args) throws InterruptedException {

		AlarmClockEmulator emulator = new AlarmClockEmulator();
		ClockInput in = emulator.getInput();
		ClockOutput out = emulator.getOutput();
		Tick tock = new Tick(out);

		Time r = new Time(tock);
		Thread timethread = new Thread(r);
		timethread.start();
		Semaphore sem = in.getSemaphore();
		while (true) {
			sem.acquire();// wait for user input
			
			UserInput userInput = in.getUserInput();
			int choice = userInput.getChoice();
			int value = userInput.getValue();
			System.out.println("choice = " + choice + "  value=" + value);
			switch (choice) {
			case CHOICE_SET_TIME:
				tock.setTime(value);
				break;
			case CHOICE_SET_ALARM:
				tock.alarmSet(value);
				break;
			case CHOICE_TOGGLE_ALARM:
				tock.alarmToggle();
				break;
			case CHOICE_OTHER:
				tock.stopAlarm();
				break;
			default:
				break;
			}

		}
	}

}
