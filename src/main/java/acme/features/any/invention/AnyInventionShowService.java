
package acme.features.any.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;

@Service
public class AnyInventionShowService extends AbstractService<Any, Invention> {

	@Autowired
	private AnyInventionRepository	repository;

	private Invention				invention;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Invention inventionPublished;

		id = super.getRequest().getData("id", Integer.class);

		inventionPublished = this.repository.findInventionById(id);

		status = inventionPublished != null && !inventionPublished.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		super.unbindGlobal("inventorId", this.invention.getInventor().getId());
	}
}
