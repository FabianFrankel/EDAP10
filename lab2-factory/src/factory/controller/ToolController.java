package factory.controller;

import factory.model.DigitalSignal;
import factory.model.WidgetKind;
import factory.swingview.Factory;

public class ToolController {
	private final DigitalSignal conveyor, press, paint;
	private final long pressingMillis, paintingMillis;
	private boolean down;

	public ToolController(DigitalSignal conveyor, DigitalSignal press, DigitalSignal paint, long pressingMillis,
			long paintingMillis) {
		this.conveyor = conveyor;
		this.press = press;
		this.paint = paint;
		this.pressingMillis = pressingMillis;
		this.paintingMillis = paintingMillis;
	}

	public synchronized void onPressSensorHigh(WidgetKind widgetKind) throws InterruptedException {
		//
		// TODO: you will need to modify this method
		//
		if (widgetKind == WidgetKind.BLUE_RECTANGULAR_WIDGET) {
			conveyor.off();
			press.on();
			down = true;
//			Thread.sleep(pressingMillis);
			waitOutside(pressingMillis);
			press.off();
//			Thread.sleep(pressingMillis);
			waitOutside(pressingMillis);
			down = false;
			notifyAll();
			conveyor.on();
		}

	}

	public synchronized void onPaintSensorHigh(WidgetKind widgetKind) throws InterruptedException {
		//
		// TODO: you will need to modify this method
		//
		if (widgetKind == WidgetKind.ORANGE_ROUND_WIDGET) {
			conveyor.off();
			paint.on();
//			Thread.sleep(paintingMillis);
//			wait(paintingMillis);
			waitOutside(paintingMillis);
			paint.off();
			while(down) {
				wait();
			}
			conveyor.on();
//			startConveyor();
		}
	}

	/** Helper method: sleep outside of monitor for ’millis’ milliseconds. */
	private void waitOutside(long millis) throws InterruptedException {
		long timeToWakeUp = System.currentTimeMillis() + millis;

		while (System.currentTimeMillis() < timeToWakeUp) {
			long dt = timeToWakeUp - System.currentTimeMillis();
			wait(dt);

		}
	}

	private synchronized void startConveyor() {
		conveyor.on();
	}
	// -----------------------------------------------------------------------

	public static void main(String[] args) {
		Factory factory = new Factory();
		factory.startSimulation();
	}
}
