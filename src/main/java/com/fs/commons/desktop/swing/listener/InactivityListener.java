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
package com.fs.commons.desktop.swing.listener;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Timer;

/*
 *  A class that monitors inactivity in an application.
 *
 *  It does this by using a Swing Timer and by listening for specified
 *  AWT events. When an event is received the Timer is restarted.
 *  If no event is received during the specified time interval then the
 *  timer will fire and invoke the specified Action.
 *
 *  When creating the listener the inactivity interval is specified in
 *  minutes. However, once the listener has been created you can reset
 *  this value in milliseconds if you need to.
 *
 *  Some common event masks have be defined with the class:
 *
 *  KEY_EVENTS
 *  MOUSE_EVENTS - which includes mouse motion events
 *  USER_EVENTS - includes KEY_EVENTS and MOUSE_EVENT (this is the default)
 *
 *  The inactivity interval and event mask can be changed at any time,
 *  however, they will not become effective until you stop and start
 *  the listener.
 */
public class InactivityListener implements ActionListener, AWTEventListener {
	public final static long KEY_EVENTS = AWTEvent.KEY_EVENT_MASK;

	public final static long MOUSE_EVENTS = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;

	public final static long USER_EVENTS = KEY_EVENTS + MOUSE_EVENTS;

	private Action action;
	private int interval;
	private long eventMask;
	private final Timer timer = new Timer(0, this);

	/*
	 * Use a default inactivity interval of 1 minute and listen for USER_EVENTS
	 */
	public InactivityListener(final Action action) {
		this(action, 1);
	}

	/*
	 * Specify the inactivity interval and listen for USER_EVENTS
	 */
	public InactivityListener(final Action action, final int interval) {
		this(action, interval, USER_EVENTS);
	}

	/*
	 * Specify the inactivity interval and the events to listen for
	 */
	public InactivityListener(final Action action, final int minutes, final long eventMask) {
		setAction(action);
		setInterval(minutes);
		setEventMask(eventMask);
	}

	// Implement ActionListener for the Timer
	@Override
	public void actionPerformed(final ActionEvent e) {
		this.action.actionPerformed(e);
	}

	// Implement AWTEventListener
	@Override
	public void eventDispatched(final AWTEvent e) {
		if (this.timer.isRunning()) {
			this.timer.restart();
		}
	}

	public void resetTimer() {
		this.timer.restart();
	}

	/*
	 * The Action to be invoked after the specified inactivity period
	 */
	public void setAction(final Action action) {
		this.action = action;
	}

	/*
	 * A mask specifying the events to be passed to the AWTEventListener
	 */
	public void setEventMask(final long eventMask) {
		this.eventMask = eventMask;
	}

	/*
	 * The interval before the Action is invoked specified in minutes
	 */
	public void setInterval(final int minutes) {
		setIntervalInMillis(minutes * 60000);
	}

	/*
	 * The interval before the Action is invoked specified in milliseconds
	 */
	public void setIntervalInMillis(final int interval) {
		this.interval = interval;
		this.timer.setInitialDelay(interval);
	}

	/*
	 * Start listening for events.
	 */
	public void start() {
		this.timer.setInitialDelay(this.interval);
		this.timer.setRepeats(false);
		this.timer.start();
		Toolkit.getDefaultToolkit().addAWTEventListener(this, this.eventMask);
	}

	/*
	 * Stop listening for events
	 */
	public void stop() {
		Toolkit.getDefaultToolkit().removeAWTEventListener(this);
		this.timer.stop();
	}
}
