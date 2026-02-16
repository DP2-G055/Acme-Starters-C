
package acme.entities.part;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.entities.invention.Invention;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Part {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Invention			invention;

	@Mandatory
	// validheader
	@Column
	private String				name;

	@Mandatory
	// valid text
	@Column
	private String				description;

	@Mandatory
	@ValidMoney(min = 0)
	@Column
	private Money				cost;

	@Mandatory
	@Valid
	@Column
	private PartKind			kind;

}
