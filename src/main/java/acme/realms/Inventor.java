
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Inventor {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	//Falta agregar validtext
	@Column
	private String				bio;

	@Mandatory
	//Falta agregar validtext
	@Column
	private String				keyWords;

	@Mandatory
	@Valid
	@Column
	private Boolean				licensed;

}
