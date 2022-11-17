package net.kiwox.manager.dst.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.kiwox.dst.domain.AbstractUser;

@Entity
@Table(name = "`_User`")
public class Users extends AbstractUser {
	public Users() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [username=");
		builder.append(getUsername());
		builder.append(", name=");
		builder.append(getName());
		builder.append(", created=");
		builder.append(getCreated());
		builder.append(", lastAccess=");
		builder.append(getLastAccess());
		builder.append(", blocked=");
		builder.append(getBlocked());
		builder.append(", webadminUser=");
		builder.append(getWebadminUser());
		builder.append(", externalUser=");
		builder.append(getExternalUser());
		builder.append(", allowedIPs=");
		builder.append(getAllowedIPs());
		builder.append("]");
		return builder.toString();
	}
}
