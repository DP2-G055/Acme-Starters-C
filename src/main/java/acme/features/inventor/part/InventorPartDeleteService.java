
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.part.Part;
import acme.entities.part.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartDeleteService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository	repository;

	private Part					part;


	@Override
	public void load() {
		if (super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			this.part = this.repository.findPartById(id);
		}
	}

	@Override
	public void authorise() {
		boolean status;
		int inventorId;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			inventorId = super.getRequest().getPrincipal().getAccountId();

			status = this.part != null && this.part.getInvention().getInventor().getUserAccount().getId() == inventorId && this.part.getInvention().getDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.part);
	}

	@Override
	public void execute() {
		this.repository.delete(this.part);
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

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
