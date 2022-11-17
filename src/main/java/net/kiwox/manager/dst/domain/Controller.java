package net.kiwox.manager.dst.domain;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "`Controller`")
public class Controller {
	
	private long id;
	private String name;
	private String ip;
	private Date lastUpdate;
	private List<ControllerProbe> probes;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Controller)) {
			return false;
		}
		Controller castOther = (Controller) obj;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Controller [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", lastUpdate=");
		builder.append(lastUpdate);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@Column(name = "`id`", nullable = false)
	@GeneratedValue(generator = "CONTROLLER_GENERATOR")
	@SequenceGenerator(name = "CONTROLLER_GENERATOR", allocationSize = 1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "`name`")
	@Length(max = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "`ip`")
	@Length(max = 15)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "`last_update`")
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@JoinColumn(name = "`controller`", referencedColumnName = "`id`")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ControllerProbe> getProbes() {
		return probes;
	}
	public void setProbes(List<ControllerProbe> probes) {
		this.probes = probes;
	}
}
