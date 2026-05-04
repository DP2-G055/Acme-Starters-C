
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.part.Part;
import acme.entities.part.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartCreateService extends AbstractService<Inventor, Part> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventorPartRepository	repository;

	private Part					part;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int id = super.getRequest().getData("inventionId", int.class);
		Invention invention = this.repository.findInventionById(id);
		this.part = new Part();
		this.part.setInvention(invention);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.part != null && this.part.getInvention() != null) {
			Invention invention = this.part.getInvention();
			boolean isOwner = invention.getInventor().isPrincipal();
			boolean isDraft = invention.getDraftMode() != null && invention.getDraftMode();

			status = isOwner && isDraft;
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
		this.repository.save(this.part);
	}

	@Override
	public void unbind() {
		SelectChoices kindChoices = SelectChoices.from(PartKind.class, this.part.getKind());
		super.unbindObject(this.part, "name", "description", "cost", "kind");
		super.unbindGlobal("draftMode", this.part.getInvention().getDraftMode());
		super.unbindGlobal("inventionId", this.part.getInvention().getId());
		super.unbindGlobal("kindOptions", kindChoices);
	}

}
