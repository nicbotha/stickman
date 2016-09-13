package au.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name = "SomeEntity")
@Multitenant
@TenantDiscriminatorColumn(name = "tenant_id")
public class SomeEntity extends BaseEntity {

	@Column(name = "name", length = 10, nullable = false)
	@NotNull(message = "{model.someentity.name.notnull}")
	@Size(message = "{model.someentity.name.size}", min = 1, max = 10)
	private String name;

	public SomeEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String param) {
		this.name = param;
	}
}