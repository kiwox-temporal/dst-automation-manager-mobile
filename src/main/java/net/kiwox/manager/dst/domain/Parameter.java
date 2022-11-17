package net.kiwox.manager.dst.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import net.kiwox.manager.dst.enums.ParameterDataType;

@Entity
@Table(name = "`_Parameter`")
public class Parameter implements Serializable {

	private static final long serialVersionUID = 3036109089298332872L;

	private String name;
	private ParameterDataType dataType;
	private String description;
	private String value;

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Parameter [name=");
		builder.append(name);
		builder.append(", dataType=");
		builder.append(dataType);
		builder.append(", description=");
		builder.append(description);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

	@Id
	@NotNull
	@Length(max = 128)
	@Column(name = "`name`", nullable = false, length = 128)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "`data_type`", nullable = false)
	public ParameterDataType getDataType() {
		return dataType;
	}
	public void setDataType(ParameterDataType dataType) {
		this.dataType = dataType;
	}

	@Lob
	@Column(name = "`description`")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Lob
	@NotNull
	@Column(name = "`value`", nullable = false)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
