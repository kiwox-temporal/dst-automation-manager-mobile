package net.kiwox.manager.dst.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import net.kiwox.manager.dst.enums.TestResultState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`TestControllerProbe`")
public class TestControllerProbe {

    private long id;
    private Test test;
    private Date createdOn;
    private ControllerProbe controllerProbe;
    private int active;

    public TestControllerProbe() {
        createdOn = new Date();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + active;
        result = prime * result + ((controllerProbe == null) ? 0 : controllerProbe.hashCode());
        result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((test == null) ? 0 : test.hashCode());
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
        TestControllerProbe other = (TestControllerProbe) obj;
        if (active != other.active)
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
        if (id != other.id)
            return false;
        if (test == null) {
            if (other.test != null)
                return false;
        } else if (!test.equals(other.test))
            return false;
        return true;
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
    @Column(name = "`active`")
    public int getActive() {
        return active;
    }

    public void setActive(int attempt) {
        this.active = attempt;
    }


    public static TestControllerProbe buildTuple(Test test, ControllerProbe controllerProbe) {
        TestControllerProbe item = new TestControllerProbe();
        item.setTest(test);
        item.setCreatedOn(new Date());
        item.setActive(1);
        return item;
    }
}




