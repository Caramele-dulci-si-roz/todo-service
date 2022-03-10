package ro.unibuc.hello.data;

public enum TaskStatus {
	OPEN, IN_PROGRESS, REOPENED, RESOLVED, CLOSED;

	public static boolean contains(String test) {
		for (TaskStatus taskStatus : TaskStatus.values()) {
			if (taskStatus.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}
