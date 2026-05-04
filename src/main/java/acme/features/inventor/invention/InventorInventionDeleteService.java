
package acme.features.inventor.invention;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.part.Part;
import acme.realms.Inventor;

@Service
public class InventorInventionDeleteService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (super.getRequest().hasData("id", int.class) && this.invention != null) {
			boolean isOwner = this.invention.getInventor().isPrincipal();
			boolean isDraft = this.invention.getDraftMode() != null && this.invention.getDraftMode();

			status = isOwner && isDraft;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);
	}

	@Override
	public void execute() {
		List<Part> parts = this.repository.findAllPartsByInventionId(this.invention.getId());
		for (int i = 0; i < parts.size(); i++)
			this.repository.delete(parts.get(i));
		this.repository.delete(this.invention);
	}

	@Override
	public void unbind() {
		if (this.invention != null)
			super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

	}
}
