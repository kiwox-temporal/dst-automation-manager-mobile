package net.kiwox.manager.dst.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import net.kiwox.manager.dst.enums.TestPlanActive;

@Entity
@Table(name = "`TestPlan`")
public class TestPlan {
	
	private long id;
	private String name;
	private TestPlanActive active;
	private List<Test> tests;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TestPlan)) {
			return false;
		}
		TestPlan castOther = (TestPlan) obj;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestPlan [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", active=");
		builder.append(active);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@Column(name = "`id`", nullable = false)
	@GeneratedValue(generator = "TEST_PLAN_GENERATOR")
	@SequenceGenerator(name = "TEST_PLAN_GENERATOR", allocationSize = 1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "`name`", nullable = false)
	@Length(min = 1, max = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "`active`")
	public TestPlanActive isActive() {
		return active;
	}
	public void setActive(TestPlanActive active) {
		this.active = active;
	}

	@JoinColumn(name = "`test_plan`", referencedColumnName = "`id`")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Test> getTests() {
		return tests;
	}
	public void setTests(List<Test> tests) {
		this.tests = tests;
	}
}
