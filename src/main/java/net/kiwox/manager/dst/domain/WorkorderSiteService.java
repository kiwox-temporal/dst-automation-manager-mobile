package net.kiwox.manager.dst.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "`WorkorderSiteService`")
public class WorkorderSiteService {
	
	private long id;
	private Workorder workorder;
	private Site site;
	private Service service;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WorkorderSiteService)) {
			return false;
		}
		WorkorderSiteService castOther = (WorkorderSiteService) obj;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WorkorderSiteService [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@Column(name = "`id`", nullable = false)
	@GeneratedValue(generator = "WORKORDER_SITE_SERVICE_GENERATOR")
	@SequenceGenerator(name = "WORKORDER_SITE_SERVICE_GENERATOR", allocationSize = 1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@JoinColumn(name = "`workorder`")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Workorder getWorkorder() {
		return workorder;
	}
	public void setWorkorder(Workorder workorder) {
		this.workorder = workorder;
	}

	@NotNull
	@JoinColumn(name = "`site`")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}

	@NotNull
	@JoinColumn(name = "`service`")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
}
