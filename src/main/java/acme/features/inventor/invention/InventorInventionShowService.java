
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionShowService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;


	@Override
	public void authorise() {
		boolean status;

		status = this.invention != null && (this.invention.getInventor().isPrincipal() || !this.invention.getDraftMode());

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);

	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		tuple.put("draftMode", this.invention.getDraftMode());
		tuple.put("cost", this.invention.getCost());
		tuple.put("monthsActive", this.invention.getMonthsActive());
	}

}
