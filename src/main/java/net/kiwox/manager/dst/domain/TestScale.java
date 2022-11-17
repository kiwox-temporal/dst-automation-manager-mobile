package net.kiwox.manager.dst.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import net.kiwox.manager.dst.enums.TestResultGrade;

@Entity
@Table(name = "`TestScale`")
public class TestScale {
	
	private long id;
	private TestResultGrade type;
	private Integer from;
	private Integer toEqual;
	private Test test;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TestScale)) {
			return false;
		}
		TestScale castOther = (TestScale) obj;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestScale [id=");
		builder.append(id);
		builder.append(", type=");
		builder.append(type);
		builder.append(", from=");
		builder.append(from);
		builder.append(", toEqual=");
		builder.append(toEqual);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@Column(name = "`id`", nullable = false)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "`type`")
	public TestResultGrade getType() {
		return type;
	}
	public void setType(TestResultGrade type) {
		this.type = type;
	}

	@Column(name = "`from`")
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}

	@Column(name = "`to_equal`")
	public Integer getToEqual() {
		return toEqual;
	}
	public void setToEqual(Integer toEqual) {
		this.toEqual = toEqual;
	}

	@NotNull
	@JoinColumn(name = "`test`")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Test getTest() {
		return test;
	}
	public void setTest(Test test) {
		this.test = test;
	}

}
