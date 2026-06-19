
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
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

	}

	@Override
	public void authorise() {
		boolean status;

		status = this.part != null && this.part.getInvention().getInventor().isPrincipal() && this.part.getInvention().getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {

	}

	@Override
	public void validate() {

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
}
