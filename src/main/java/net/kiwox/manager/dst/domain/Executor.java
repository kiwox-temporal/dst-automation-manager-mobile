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
@Table(name = "`Executor`")
public class Executor {
	
	private long id;
	private String name;
	private String email;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Executor)) {
			return false;
		}
		Executor castOther = (Executor) obj;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Executor [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@Column(name = "`id`", nullable = false)
	@GeneratedValue(generator = "EXECUTOR_GENERATOR")
	@SequenceGenerator(name = "EXECUTOR_GENERATOR", allocationSize = 1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "`name`")
	@Length(min = 1, max = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "`email`")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
