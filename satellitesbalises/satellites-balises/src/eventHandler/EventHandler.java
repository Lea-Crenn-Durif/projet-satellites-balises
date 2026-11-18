package eventHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventHandler {

	private final Map<Class<? extends AbstractEvent>, List<Object>> listeners = new ConcurrentHashMap<>();


	public void registerListener(Class<? extends AbstractEvent> eventType, Object listener) {
		Objects.requireNonNull(eventType, "eventType");
		Objects.requireNonNull(listener, "listener");
		listeners.computeIfAbsent(eventType, type -> new CopyOnWriteArrayList<>()).add(listener);
	}
	
	public void unregisterListener(Class<? extends AbstractEvent> eventType, Object listener) {
		List<Object> registered = listeners.get(eventType);
		if (registered != null) {
			registered.remove(listener);
		}
	}
	
	public void send(AbstractEvent event) {
		Objects.requireNonNull(event, "event");
		for (Object listener : listeners.getOrDefault(event.getClass(), List.of())) {
			event.sendTo(listener);
		}
	}
}