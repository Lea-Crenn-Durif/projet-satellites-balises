package eventHandler;

import java.util.EventObject;

public abstract class AbstractEvent extends EventObject {
	private static final long serialVersionUID = 1209936372774821519L;

	public AbstractEvent(Object source) {
		super(source);
	}

	public abstract void sendTo(Object target);
}