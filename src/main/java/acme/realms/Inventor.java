
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Inventor extends AbstractRealm {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidText
	@Column
	private String				bio;

	@Mandatory
	@ValidText
	@Column
	private String				keyWords;

	@Mandatory
	@Valid
	@Column
	private Boolean				licensed;

}
