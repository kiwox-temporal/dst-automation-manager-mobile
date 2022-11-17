package net.kiwox.manager.dst.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "`Task`")
public class Task {
	
	private long id;
	private String description;
	private int order;
	private boolean active;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Task)) {
			return false;
		}
		Task castOther = (Task) obj;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Task [id=");
		builder.append(id);
		builder.append(", description=");
		builder.append(description);
		builder.append(", order=");
		builder.append(order);
		builder.append(", active=");
		builder.append(active);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@Column(name = "`id`", nullable = false)
	@GeneratedValue(generator = "TASK_GENERATOR")
	@SequenceGenerator(name = "TASK_GENERATOR", allocationSize = 1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "`description`")
	@Length(min = 1, max = 255)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@Column(name = "`order`")
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	@NotNull
	@Column(name = "`active`")
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
