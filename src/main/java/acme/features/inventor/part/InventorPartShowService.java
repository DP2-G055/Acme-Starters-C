
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.part.Part;
import acme.entities.part.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartShowService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository	repository;

	private Part					part;


	@Override
	public void authorise() {
		boolean status;
		int inventorId;

		if (!super.getRequest().hasData("id") || !super.getRequest().getPrincipal().hasRealmOfType(Inventor.class) || this.part == null)
			status = false;
		else if (this.part.getInvention().getDraftMode()) {
			inventorId = super.getRequest().getPrincipal().getAccountId();

			status = this.part != null && this.part.getInvention().getInventor().getUserAccount().getId() == inventorId;
		} else
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		if (super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			this.part = this.repository.findPartById(id);
		}
	}

	@Override
	public void unbind() {
		if (this.part != null) {
			SelectChoices kindChoices = SelectChoices.from(PartKind.class, this.part.getKind());
			super.unbindObject(this.part, "name", "description", "cost", "kind");
			super.unbindGlobal("draftMode", this.part.getInvention().getDraftMode());
			super.unbindGlobal("inventionId", this.part.getInvention().getId());
			super.unbindGlobal("kindOptions", kindChoices);
		}
	}
}
