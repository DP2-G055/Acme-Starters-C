
package acme.features.any.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Sponsor;

@Service
public class AnySponsorShowService extends AbstractService<Any, Sponsor> {

	@Autowired
	private AnySponsorRepository	repository;

	private Sponsor					sponsor;


	@Override
	public void authorise() {
		boolean status;

		if (!super.getRequest().hasData("id"))
			status = false;
		else
			status = this.sponsor != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.sponsor = this.repository.findSponsorById(id);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.sponsor, "address", "im", "gold");
		tuple.put("fullName", this.sponsor.getIdentity().getFullName());
	}

}
