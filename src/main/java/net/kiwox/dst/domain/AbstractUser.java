package net.kiwox.dst.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@MappedSuperclass
public abstract class AbstractUser {

	private String username;
	private String name;
	private Date created;
	private Date lastAccess;
	private Boolean blocked;
	private Boolean webadminUser;
	private Boolean externalUser;
	private Set<String> allowedIPs;

	public AbstractUser() {
		created = new Date();
		blocked = false;
		webadminUser = true;
		externalUser = true;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		AbstractUser other = (AbstractUser) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@Id
	@NotNull
	@Length(max = 255)
	@Column(name = "`username`", nullable = false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Length(max = 255)
	@Column(name = "`name`")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(name = "`created`", nullable = false)
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "`last_access`")
	public Date getLastAccess() {
		return lastAccess;
	}
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	@NotNull
	//@ColumnDefault("false")
	@Column(name = "`blocked`", nullable = false)
	public Boolean getBlocked() {
		return blocked;
	}
	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	@NotNull
	//@ColumnDefault("true")
	@Column(name = "`webadmin_user`", nullable = false)
	public Boolean getWebadminUser() {
		return webadminUser;
	}
	public void setWebadminUser(Boolean webadminUser) {
		this.webadminUser = webadminUser;
	}

	@NotNull
	//@ColumnDefault("true")
	@Column(name = "`external_user`", nullable = false)
	public Boolean getExternalUser() {
		return externalUser;
	}
	public void setExternalUser(Boolean externalUser) {
		this.externalUser = externalUser;
	}

	@Column(name = "`ip`")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "`_UserIP`", joinColumns = @JoinColumn(name = "`username`"))
	public Set<String> getAllowedIPs() {
		return allowedIPs;
	}
	public void setAllowedIPs(Set<String> allowedIPs) {
		this.allowedIPs = allowedIPs;
	}
}
