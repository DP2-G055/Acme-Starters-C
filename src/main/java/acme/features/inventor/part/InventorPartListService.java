
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

		inventionId = super.getRequest().getData("inventionId", Integer.class);

		inventionPublished = this.repository.findInventionById(inventionId);

		inventorId = super.getRequest().getPrincipal().getAccountId();

		status = inventionPublished != null && inventionPublished.getInventor().getUserAccount().getId() == inventorId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.parts, "name", "description", "cost");
		super.unbindGlobal("id", super.getRequest().getData("inventionId", int.class));
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("inventionId", int.class);
		this.parts = this.repository.findAllPartsByInventionId(id);
	}

}
