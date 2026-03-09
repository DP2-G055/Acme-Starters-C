
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

		id = super.getRequest().getData("id", Integer.class);

		partWithInventionPublished = this.repository.findPartById(id);

		status = partWithInventionPublished != null && !partWithInventionPublished.getInvention().getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.part, "name", "description", "cost", "kind");
	}
}
