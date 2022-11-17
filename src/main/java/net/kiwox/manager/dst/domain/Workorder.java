package net.kiwox.manager.dst.domain;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import net.kiwox.manager.dst.enums.TestResultGrade;
import net.kiwox.manager.dst.enums.WorkorderState;

@Entity
@Table(name = "`Workorder`")
public class Workorder {
	
	private long id;
	private String assignmentCode;
	private Date plannedDatetime;
	private Date endDatetime;
	private String comment;
	private WorkorderState state;
	private TestResultGrade testResult;
	private Date createdOn;
	private Task task;
	// private Executor executor;
	// private Users executor;
	private String executor;
	private boolean onDemand;
	
	public Workorder() {
		createdOn = new Date();
		plannedDatetime = new Date();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Workorder)) {
			return false;
		}
		Workorder castOther = (Workorder) obj;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Workorder [id=");
		builder.append(id);
		builder.append(", assignmentCode=");
		builder.append(assignmentCode);
		builder.append(", plannedDatetime=");
		builder.append(plannedDatetime);
		builder.append(", endDatetime=");
		builder.append(endDatetime);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", state=");
		builder.append(state);
		builder.append(", testResult=");
		builder.append(testResult);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", executor=");
		builder.append(executor);
		builder.append(", onDemand=");
		builder.append(onDemand);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@Column(name = "`id`", nullable = false)
	@GeneratedValue(generator = "WORKORDER_GENERATOR")
	@SequenceGenerator(name = "WORKORDER_GENERATOR", allocationSize = 1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "`assignment_code`")
	@Length(min = 1, max = 20)
	public String getAssignmentCode() {
		return assignmentCode;
	}
	public void setAssignmentCode(String assignmentCode) {
		this.assignmentCode = assignmentCode;
	}

	@Column(name = "`planned_datetime`")
	public Date getPlannedDatetime() {
		return plannedDatetime;
	}
	public void setPlannedDatetime(Date plannedDatetime) {
		this.plannedDatetime = plannedDatetime;
	}

	@Column(name = "`end_datetime`")
	public Date getEndDatetime() {
		return endDatetime;
	}
	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	@Column(name = "`comment`")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "`state`")
	public WorkorderState getState() {
		return state;
	}
	public void setState(WorkorderState state) {
		this.state = state;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "`test_result`")
	public TestResultGrade getTestResult() {
		return testResult;
	}
	public void setTestResult(TestResultGrade testResult) {
		this.testResult = testResult;
	}
	
	@NotNull
	@Column(name = "`created_on`")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@NotNull
	@JoinColumn(name = "`task`")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}

	@NotNull
	@Column(name = "`executor`")
	public String getExecutor() {
		return executor;
	}
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	
	@NotNull
	@Column(name = "`on_demand`", nullable = false)
	public boolean isOnDemand() {
		return onDemand;
	}
	public void setOnDemand(boolean onDemand) {
		this.onDemand = onDemand;
	}
}
