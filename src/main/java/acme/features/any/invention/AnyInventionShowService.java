
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

		if (!super.getRequest().hasData("id", int.class))
			status = false;
		else {
			int id = super.getRequest().getData("id", Integer.class);
			Invention inventionPublished = this.repository.findInventionById(id);

			status = inventionPublished != null && !inventionPublished.getDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		try {
			if (super.getRequest().hasData("id", int.class)) {
				int id = super.getRequest().getData("id", int.class);
				this.invention = this.repository.findInventionById(id);
			}
		} catch (Exception e) {
			this.invention = null;
		}

	}

	@Override
	public void unbind() {
		if (this.invention != null) {
			super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
			super.unbindGlobal("cost", this.invention.cost());
			super.unbindGlobal("monthsActive", this.invention.monthsActive());
			super.unbindGlobal("inventorId", this.invention.getInventor().getId());
		}
	}
}
