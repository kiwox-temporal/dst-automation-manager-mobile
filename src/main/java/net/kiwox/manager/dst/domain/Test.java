package net.kiwox.manager.dst.domain;

import java.util.List;
import java.util.Locale;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import net.kiwox.manager.dst.enums.Category;
import org.hibernate.validator.constraints.Length;

import net.kiwox.manager.dst.enums.TestType;

@Entity
@Table(name = "`Test`")
public class Test {

    private long id;

    private String execution_code;
    private String description;
    private String channel;
    private String paymentType;
    private String script;
    private TestType type;
    private String testName;
    private Category category;
    private int order_in_report;
    private String indexText;
    private int iteration;
    private int maxExecutions;

    private boolean selected;
    private boolean active;

    private String mail_notification_subject;
    private String mail_notification_message;

    private int timeout_flow;
    private int timeout_sla;


    // ingoing relationships
    private List<TestScale> testScales;

    private List<TestResult> testResults;

    private List<TestControllerProbe> testControllerProbes;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((indexText == null) ? 0 : indexText.hashCode());
        result = prime * result + iteration;
        result = prime * result + maxExecutions;
        result = prime * result + ((script == null) ? 0 : script.hashCode());
        result = prime * result + ((testName == null) ? 0 : testName.hashCode());
        result = prime * result + ((testScales == null) ? 0 : testScales.hashCode());
        result = prime * result + ((testResults == null) ? 0 : testResults.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Test other = (Test) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (indexText == null) {
            if (other.indexText != null)
                return false;
        } else if (!indexText.equals(other.indexText))
            return false;
        if (iteration != other.iteration)
            return false;
        if (maxExecutions != other.maxExecutions)
            return false;
        if (script == null) {
            if (other.script != null)
                return false;
        } else if (!script.equals(other.script))
            return false;
        if (testName == null) {
            if (other.testName != null)
                return false;
        } else if (!testName.equals(other.testName))
            return false;
        if (testScales == null) {
            if (other.testScales != null)
                return false;
        } else if (!testScales.equals(other.testScales))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Test [id=" + id + ", description=" + description + ", script=" + script + ", type=" + type
                + ", testName=" + testName + ", testScales=" + testScales + ", testResul=" + testResults + ", maxExecutions=" + maxExecutions
                + ", indexText=" + indexText + ", iteration=" + iteration + ", active=" + active + "]";
    }

    @Id
    @Column(name = "`id`", nullable = false)
    @GeneratedValue(generator = "TEST_GENERATOR")
    @SequenceGenerator(name = "TEST_GENERATOR", allocationSize = 1)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Column(name = "execution_code")
    @Length(max = 10)
    public String getExecution_code() {
        return execution_code;
    }

    public void setExecution_code(String execution_code) {
        this.execution_code = execution_code;
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
    @Column(name = "`script`")
    @Length(min = 1, max = 255)
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "`type`")
    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    @Column(name = "`test_name`")
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @JoinColumn(name = "`test`", referencedColumnName = "`id`")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TestScale> getTestScales() {
        return testScales;
    }

    public void setTestScales(List<TestScale> testScales) {
        this.testScales = testScales;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "test")
    @JsonManagedReference
    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "test")
    @JsonManagedReference
    public List<TestControllerProbe> getTestControllerProbes() {
        return testControllerProbes;
    }

    public void setTestControllerProbes(List<TestControllerProbe> testControllerProbes) {
        this.testControllerProbes = testControllerProbes;
    }

    @NotNull
    @Column(name = "`max_executions`")
    public int getMaxExecutions() {
        return maxExecutions;
    }

    public void setMaxExecutions(int maxExecutions) {
        this.maxExecutions = maxExecutions;
    }

    @Column(name = "`index_text`")
    public String getIndexText() {
        return indexText;
    }

    public void setIndexText(String indexText) {
        this.indexText = indexText;
    }

    @NotNull
    @Column(name = "`iteration`")
    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    @NotNull
    @Column(name = "`active`")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "`category`")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @NotNull
    @Column(name = "`channel`")

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @NotNull
    @Column(name = "`payment_type`")
    public String getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @NotNull
    @Column(name = "`order_in_report`")
    public int getOrder_in_report() {
        return order_in_report;
    }

    public void setOrder_in_report(int order_in_report) {
        this.order_in_report = order_in_report;
    }

    @NotNull
    @Column(name = "`selected`")
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Column(name ="`mail_notification_message`")
    public String getMail_notification_message() {
        return mail_notification_message;
    }
    public void setMail_notification_message(String mail_notification_message) {
        this.mail_notification_message = mail_notification_message;
    }

    @Column(name = "`mail_notification_subject`")
    public String getMail_notification_subject() {
        return mail_notification_subject;
    }
    public void setMail_notification_subject(String mail_notification_subject) {
        this.mail_notification_subject = mail_notification_subject;
    }

    @Column(name = "`timeout_flow`")
    public int getTimeout_flow() {
        return timeout_flow;
    }

    public void setTimeout_flow(int timeout_flow) {
        this.timeout_flow = timeout_flow;
    }

    @Column(name = "`timeout_sla`")
    public int getTimeout_sla() {
        return timeout_sla;
    }

    public void setTimeout_sla(int timeout_sla) {
        this.timeout_sla = timeout_sla;
    }
}
