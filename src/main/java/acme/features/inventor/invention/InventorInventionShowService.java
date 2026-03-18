
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionShowService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;


	@Override
	public void authorise() {
		boolean status;
		int inventorId;

		if (!super.getRequest().hasData("id") || !super.getRequest().getPrincipal().hasRealmOfType(Inventor.class) || this.invention == null)
			status = false;
		else if (this.invention.getDraftMode()) {
			inventorId = super.getRequest().getPrincipal().getAccountId();

			status = this.invention != null && this.invention.getInventor().getUserAccount().getId() == inventorId;
		} else
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		if (super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			this.invention = this.repository.findInventionById(id);
		}

	}

	@Override
	public void unbind() {
		if (this.invention != null)
			super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "id", "draftMode");
	}

}
