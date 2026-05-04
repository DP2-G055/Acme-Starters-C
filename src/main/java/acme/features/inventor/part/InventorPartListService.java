
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
	public void authorise() {
		boolean status = false;

		if (super.getRequest().hasData("inventionId", int.class) && this.invention != null) {
			boolean isOwner = this.invention.getInventor().isPrincipal();
			boolean isDraft = this.invention.getDraftMode() != null && this.invention.getDraftMode();

			status = isOwner || !isDraft;
		}

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
		this.invention = this.repository.findInventionById(id);
	}

}
