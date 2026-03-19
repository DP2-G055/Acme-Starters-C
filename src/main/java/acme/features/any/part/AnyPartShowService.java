
package acme.features.any.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.part.Part;

@Service
public class AnyPartShowService extends AbstractService<Any, Part> {

	@Autowired
	private AnyPartRepository	repository;

	private Part				part;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Part partWithInventionPublished;

		if (!super.getRequest().hasData("id", int.class))
			status = false;
		else {
			id = super.getRequest().getData("id", Integer.class);

			partWithInventionPublished = this.repository.findPartById(id);

			status = partWithInventionPublished != null && !partWithInventionPublished.getInvention().getDraftMode();
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		try {
			if (super.getRequest().hasData("id", int.class)) {
				int id = super.getRequest().getData("id", int.class);
				this.part = this.repository.findPartById(id);
			}
		} catch (Exception e) {
			this.part = null;
		}

	}

	@Override
	public void unbind() {
		if (this.part != null)
			super.unbindObject(this.part, "name", "description", "cost", "kind");
	}
}
