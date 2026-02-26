
package acme.features.inventor.part;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.part.Part;
import acme.realms.Inventor;

@Service
public class InventorPartListService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository	repository;

	private List<Part>				parts;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
