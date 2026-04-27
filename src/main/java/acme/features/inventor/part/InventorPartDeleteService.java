
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
		int id = super.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
	}

	@Override
	public void authorise() {
<<<<<<< Updated upstream
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		if (status)
			status = this.part.getInvention().getInventor().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.setAuthorised(status);
=======
		boolean status = false;

		if (super.getRequest().hasData("id", int.class) && this.part != null && this.part.getInvention() != null) {
			boolean isOwner = this.part.getInvention().getInventor().isPrincipal();
			boolean isDraft = this.part.getInvention().getDraftMode() != null && this.part.getInvention().getDraftMode();

			status = isOwner && isDraft;
		}

		super.getResponse().setAuthorised(status);
>>>>>>> Stashed changes
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
		SelectChoices kindChoices = SelectChoices.from(PartKind.class, this.part.getKind());
		super.unbindObject(this.part, "name", "description", "cost", "kind");
		super.unbindGlobal("draftMode", this.part.getInvention().getDraftMode());
		super.unbindGlobal("inventionId", this.part.getInvention().getId());
		super.unbindGlobal("kindOptions", kindChoices);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
