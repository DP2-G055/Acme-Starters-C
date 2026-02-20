package acme.realms;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;

@Entity
@Getter
@Setter
public class Spokesperson extends AbstractRole {

	@Mandatory
	// @ValidText
	@Column
	private String	cv;

	@Mandatory
	// @ValidText
	@Column
	private String	achievements;

	@Mandatory
	@Valid
	@Column
	private Boolean	licensed;
}
