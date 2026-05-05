
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

	private Invention				invention;


	@Override
	public void load() {
		int inventionId = super.getRequest().getData("inventionId", int.class);
		this.parts = this.repository.findAllPartsByInventionId(inventionId);
		this.invention = this.repository.findInventionById(inventionId);

	}

	@Override
	public void authorise() {
		boolean status;

		status = this.parts != null && (!this.invention.getDraftMode() || this.invention.getInventor().isPrincipal());

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.parts, "name", "description", "cost");
		super.unbindGlobal("inventionId", this.invention.getId());
		super.unbindGlobal("draftMode", this.invention.getDraftMode());

	}

}
