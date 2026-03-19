
package acme.features.any.part;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.part.Part;

@Service
public class AnyPartListService extends AbstractService<Any, Part> {

	@Autowired
	private AnyPartRepository	repository;

	private List<Part>			parts;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Invention inventionPublished;

		if (!super.getRequest().hasData("inventionId", int.class))
			status = false;
		else {
			id = super.getRequest().getData("inventionId", Integer.class);

			inventionPublished = this.repository.findInventionById(id);

			status = inventionPublished != null && !inventionPublished.getDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void unbind() {
		if (this.parts != null)
			super.unbindObjects(this.parts, "name", "description", "cost");
	}

	@Override
	public void load() {
		try {
			if (super.getRequest().hasData("inventionId", int.class)) {
				int id = super.getRequest().getData("inventionId", int.class);
				this.parts = this.repository.findAllPartsByInventionId(id);
			}
		} catch (Exception e) {
			this.parts = null;
		}

	}

}
