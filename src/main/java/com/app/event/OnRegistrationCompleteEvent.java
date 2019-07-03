package com.app.event;

import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = -1817025520504944116L;

	public OnRegistrationCompleteEvent(Object source) {
		super(source);
	}

}
