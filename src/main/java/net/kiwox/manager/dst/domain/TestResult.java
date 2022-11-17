package net.kiwox.manager.dst.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import net.kiwox.manager.dst.enums.TestResultGrade;
import net.kiwox.manager.dst.enums.TestResultState;
import org.hibernate.annotations.GenericGenerator;

import static net.kiwox.manager.dst.utils.Utils.isNull;

@Entity
@Table(name = "`TestResult`")
public class TestResult {

    private long id;

    private String code;
    private WorkorderSiteService workorderSiteService;
    private Test test;
    private Date createdOn;
    private TestResultState state;
    private TestResultGrade result;
    private ControllerProbe controllerProbe;
    private Date testResultDatetime;
    private String testResultData;
    private Float testResultLapse;
    private int attempt;
    private int disconnected;

    private List<TestResultDetail> testResultDetails;


    public TestResult() {
        createdOn = new Date();
        testResultDetails = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + attempt;
        result = prime * result + ((controllerProbe == null) ? 0 : controllerProbe.hashCode());
        result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
        result = prime * result + disconnected;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((test == null) ? 0 : test.hashCode());
        result = prime * result + ((testResultData == null) ? 0 : testResultData.hashCode());
        result = prime * result + ((testResultDatetime == null) ? 0 : testResultDatetime.hashCode());
        result = prime * result + ((testResultLapse == null) ? 0 : testResultLapse.hashCode());
        result = prime * result + ((workorderSiteService == null) ? 0 : workorderSiteService.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
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
        TestResult other = (TestResult) obj;
        if (attempt != other.attempt)
            return false;
        if (code != other.code)
            return false;
        if (controllerProbe == null) {
            if (other.controllerProbe != null)
                return false;
        } else if (!controllerProbe.equals(other.controllerProbe))
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
        if (result != other.result)
            return false;
        if (state != other.state)
            return false;
        if (test == null) {
            if (other.test != null)
                return false;
        } else if (!test.equals(other.test))
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
        if (workorderSiteService == null) {
            if (other.workorderSiteService != null)
                return false;
        } else if (!workorderSiteService.equals(other.workorderSiteService))
            return false;
        return true;
    }

    /*@Override
    public String toString() {
        return "TestResult [id=" + id + ", createdOn=" + createdOn + ", state=" + state + ", result=" + result
                + ", testResultDatetime=" + testResultDatetime + ", testResultData=" + testResultData
                + ", testResultLapse=" + testResultLapse + ", workorderSiteService=" + workorderSiteService + ", test="
                + test + ", controllerProbe=" + controllerProbe + ", attempt=" + attempt + ", disconnected="
                + disconnected + "]";
    }*/

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
    /*@GeneratedValue(generator = "TEST_RESULT_GENERATOR")
    @SequenceGenerator(name = "TEST_RESULT_GENERATOR", allocationSize = 1)*/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Column(name = "`code`")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return result;
    }

    public void setResultGrade(TestResultGrade result) {
        this.result = result;
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

    //@NotNull
    @JoinColumn(name = "`workorder_site_service`")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public WorkorderSiteService getWorkorderSiteService() {
        return workorderSiteService;
    }

    public void setWorkorderSiteService(WorkorderSiteService workorderSiteService) {
        this.workorderSiteService = workorderSiteService;
    }

    @NotNull
    @JoinColumn(name = "`test`")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @JoinColumn(name = "`controller_probe`")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    public ControllerProbe getControllerProbe() {
        return controllerProbe;
    }

    public void setControllerProbe(ControllerProbe controllerProbe) {
        this.controllerProbe = controllerProbe;
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


    // ingoing relationships


    @NotNull
    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    public List<TestResultDetail> getTestResultDetails() {
        return testResultDetails;
    }

    public void setTestResultDetails(List<TestResultDetail> testResultDetails) {
        this.testResultDetails = testResultDetails;
    }

    public void addTestResultDetail(TestResultDetail resultDetail) {
        this.testResultDetails.add(resultDetail);
        resultDetail.setTestResult(this);
    }

    public static TestResult buildTuple(Test test, ControllerProbe controllerProbe) {
        TestResult item = new TestResult();
        item.setTest(test);
        item.setCreatedOn(new Date());
        item.setState(TestResultState.PENDING);
        item.setAttempt(0);
        item.setDisconnected(0);
        // relationshio
        item.setTest(test);
        item.setControllerProbe(controllerProbe);
        return item;
    }

    public static List<TestResult> buildTuples(List<Test> tests, ControllerProbe controllerProbe) {
        return tests.stream()
                .map(test -> buildTuple(test, controllerProbe))
                .collect(Collectors.toList());
    }


}




