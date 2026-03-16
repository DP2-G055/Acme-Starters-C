
package acme.features.inventor.part;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.part.Part;
import acme.realms.Inventor;

@Service
public class InventorPartListService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository	repository;

	private List<Part>				parts;


	@Override
	public void authorise() {
		boolean status;
		int inventionId;
		int inventorId;
		Invention inventionPublished;

		if (!super.getRequest().hasData("inventionId") || !super.getRequest().getPrincipal().hasRealmOfType(Inventor.class))
			status = false;
		else if (this.parts.size() == 0 || this.parts.get(0).getInvention().getDraftMode()) {
			inventionId = super.getRequest().getData("inventionId", Integer.class);

			inventionPublished = this.repository.findInventionById(inventionId);

			inventorId = super.getRequest().getPrincipal().getAccountId();

			status = inventionPublished != null && inventionPublished.getInventor().getUserAccount().getId() == inventorId;
		} else
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void unbind() {
		if (this.parts != null && super.getRequest().hasData("inventionId")) {
			super.unbindObjects(this.parts, "name", "description", "cost");
			super.unbindGlobal("id", super.getRequest().getData("inventionId", int.class));
		}
	}

	@Override
	public void load() {
		if (super.getRequest().hasData("inventionId")) {
			int id = super.getRequest().getData("inventionId", int.class);
			this.parts = this.repository.findAllPartsByInventionId(id);
		}
	}

}
