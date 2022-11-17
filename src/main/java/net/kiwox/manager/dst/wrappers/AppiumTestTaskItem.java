package net.kiwox.manager.dst.wrappers;

public class AppiumTestTaskItem implements Comparable<AppiumTestTaskItem> {

	private long id;
	private String apn;
	private boolean onDemand;
	private int priority;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppiumTestTaskItem other = (AppiumTestTaskItem) obj;
		return id == other.id;
	}

	@Override
	public int compareTo(AppiumTestTaskItem other) {
		int p = priority - other.priority;
		if (p == 0 && onDemand != other.onDemand) {
			return onDemand ? -1 : 1;
		}
		return p;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppiumTestTaskItem [id=");
		builder.append(id);
		builder.append(", apn=");
		builder.append(apn);
		builder.append(", priority=");
		builder.append(priority);
		builder.append(", onDemand=");
		builder.append(onDemand);
		builder.append("]");
		return builder.toString();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isOnDemand() {
		return onDemand;
	}
	public void setOnDemand(boolean onDemand) {
		this.onDemand = onDemand;
	}
	
}
