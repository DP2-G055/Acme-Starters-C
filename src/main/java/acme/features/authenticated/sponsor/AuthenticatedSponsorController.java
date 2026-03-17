
package acme.features.authenticated.sponsor;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractController;
import acme.realms.Sponsor;

public class AuthenticatedSponsorController extends AbstractController<Authenticated, Sponsor> {

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("create", AuthenticatedSponsorCreateService.class);
		super.addBasicCommand("update", AuthenticatedSponsorUpdateService.class);
	}

}
