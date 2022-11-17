package net.kiwox.manager.dst.domain;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "`ControllerProbe`")
public class ControllerProbe {

    private long id;
    private String msisdn;
    private String imei;
    private String usbPort;
    private String model;
    private String key;
    private String udid;

    private String phone_number;

    private String verification_code;

    private Integer popup_close_point_x;

    private Integer popup_close_point_y;

    private int active;

    private int compatible_4G;
    private int compatible_5G;

    private Date lastUpdate;

    // ingoing relationship
    private List<TestResult> testResults;

    private List<TestControllerProbe> testControllerProbes;
    
    private Controller controller;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ControllerProbe)) {
            return false;
        }
        ControllerProbe castOther = (ControllerProbe) obj;
        return Objects.equals(id, castOther.id);
    }

    /*@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ControllerProbe [id=");
        builder.append(id);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", imei=");
        builder.append(imei);
        builder.append(", usbPort=");
        builder.append(usbPort);
        builder.append(", model=");
        builder.append(model);
        builder.append(", key=");
        builder.append(key);
        builder.append(", lastUpdate=");
        builder.append(lastUpdate);
        builder.append("]");
        return builder.toString();
    }*/

    @Id
    @Column(name = "`id`", nullable = false)
    @GeneratedValue(generator = "CONTROLLER_PROBE_GENERATOR")
    @SequenceGenerator(name = "CONTROLLER_PROBE_GENERATOR", allocationSize = 1)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "`msisdn`")
    @Length(max = 11)
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Column(name = "`imei`")
    @Length(max = 15)
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Column(name = "`usb_port`")
    @Length(max = 5)
    public String getUsbPort() {
        return usbPort;
    }

    public void setUsbPort(String usbPort) {
        this.usbPort = usbPort;
    }

    @Column(name = "`model`")
    @Length(max = 255)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "`key`")
    @Length(max = 255)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NotNull
    @Column(name = "`udid`")
    @Length(max = 50)
    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }


    @Column(name = "`phone_number`")
    @Length(max = 100)
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Column(name = "`verification_code`")
    @Length(max = 100)
    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    @Column(name = "`popup_close_point_x`")
    public Integer getPopup_close_point_x() {
        return popup_close_point_x;
    }
    public void setPopup_close_point_x(Integer popup_close_point_x) {
        this.popup_close_point_x = popup_close_point_x;
    }

    @Column(name = "`popup_close_point_y`")
    public Integer getPopup_close_point_y() {
        return popup_close_point_y;
    }

    public void setPopup_close_point_y(Integer popup_close_point_y) {
        this.popup_close_point_y = popup_close_point_y;
    }

    @Column(name = "`last_update`")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    @NotNull
    @Column(name = "`active`")
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @NotNull
    @Column(name = "`compatible_4G`")
    public int getCompatible_4G() {
        return compatible_4G;
    }

    public void setCompatible_4G(int compatible_4G) {
        this.compatible_4G = compatible_4G;
    }

    @NotNull
    @Column(name = "`compatible_5G`")
    public int getCompatible_5G() {
        return compatible_5G;
    }

    public void setCompatible_5G(int compatible_5G) {
        this.compatible_5G = compatible_5G;
    }


    //@JoinColumn(name = "`controller_probe`", referencedColumnName = "`id`")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "controllerProbe")
    @JsonManagedReference
    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "controllerProbe")
    @JsonManagedReference
    public List<TestControllerProbe> getTestControllerProbes() {
        return testControllerProbes;
    }

    public void setTestControllerProbes(List<TestControllerProbe> testControllerProbes) {
        this.testControllerProbes = testControllerProbes;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "`controller`", nullable = false)
	public Controller getController() {
		return controller;
	}
	public void setController(Controller controller) {
		this.controller = controller;
	}
}
