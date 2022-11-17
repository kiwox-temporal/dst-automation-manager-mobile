package net.kiwox.manager.dst.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import net.kiwox.manager.dst.enums.TestResultGrade;
import net.kiwox.manager.dst.enums.TestResultState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "`TestResultDetail`")
public class TestResultDetail {

    private long id;
    private TestResult testResult;
    private String code;
    private String detail;
    private Date createdOn;
    private TestResultState state;
    private TestResultGrade resultGrade;
    private Date testResultDatetime;
    private String testResultData;
    private Float testResultLapse;
    private int attempt;
    private int disconnected;


    public TestResultDetail() {
        createdOn = new Date();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + attempt;
        result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
        result = prime * result + disconnected;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((this.resultGrade == null) ? 0 : this.resultGrade.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((detail == null) ? 0 : detail.hashCode());
        result = prime * result + ((testResultData == null) ? 0 : testResultData.hashCode());
        result = prime * result + ((testResultDatetime == null) ? 0 : testResultDatetime.hashCode());
        result = prime * result + ((testResultLapse == null) ? 0 : testResultLapse.hashCode());
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
        TestResultDetail other = (TestResultDetail) obj;
        if (attempt != other.attempt)
            return false;
        if (createdOn == null) {
            if (other.createdOn != null)
                return false;
        } else if (!createdOn.equals(other.createdOn))
            return false;
        if (disconnected != other.disconnected)
            return false;
        if (id != other.id)
            return false;
        if (resultGrade != other.resultGrade)
            return false;
        if (code != other.code)
            return false;
        if (detail != other.detail)
            return false;
        if (state != other.state)
            return false;
        if (testResultData == null) {
            if (other.testResultData != null)
                return false;
        } else if (!testResultData.equals(other.testResultData))
            return false;
        if (testResultDatetime == null) {
            if (other.testResultDatetime != null)
                return false;
        } else if (!testResultDatetime.equals(other.testResultDatetime))
            return false;
        if (testResultLapse == null) {
            if (other.testResultLapse != null)
                return false;
        } else if (!testResultLapse.equals(other.testResultLapse))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TestResult [id=" + id + ", createdOn=" + createdOn + "code=" + code + ", detail=" + detail +
                ", state=" + state + ", result=" + resultGrade
                + ", testResultDatetime=" + testResultDatetime + ", detail=" + testResultData
                + ", testResultLapse=" + testResultLapse + ", workorderSiteService=" + ", test="
                + ", attempt=" + attempt + ", disconnected="
                + disconnected + "]";
    }

    @Id
    @Column(name = "`id`", nullable = false)
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    //@GeneratedValue(generator = "TEST_RESULT_DETAIL_GENERATOR")
    //@SequenceGenerator(name = "TEST_RESULT_DETAIL_GENERATOR", allocationSize = 1)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    @Column(name = "`created_on`")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "`code`")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "`detail`")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "`state`")
    public TestResultState getState() {
        return state;
    }

    public void setState(TestResultState state) {
        this.state = state;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "`result`")
    public TestResultGrade getResultGrade() {
        return resultGrade;
    }

    public void setResultGrade(TestResultGrade result) {
        this.resultGrade = result;
    }

    @Column(name = "`test_result_datetime`")
    public Date getTestResultDatetime() {
        return testResultDatetime;
    }

    public void setTestResultDatetime(Date testResultDatetime) {
        this.testResultDatetime = testResultDatetime;
    }

    @Column(name = "`test_result_data`")
    public String getTestResultData() {
        return testResultData;
    }

    public void setTestResultData(String testResultData) {
        this.testResultData = testResultData;
    }

    @Column(name = "`test_result_lapse`")
    public Float getTestResultLapse() {
        return testResultLapse;
    }

    public void setTestResultLapse(Float testResultLapse) {
        this.testResultLapse = testResultLapse;
    }


    @NotNull
    @JoinColumn(name = "`idTestResult`")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    @NotNull
    @Column(name = "`attempt`")
    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    @NotNull
    @Column(name = "`disconnected`")
    public int getDisconnected() {
        return disconnected;
    }

    public void setDisconnected(int disconnected) {
        this.disconnected = disconnected;
    }
}
