
package acme.features.any.inventor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Inventor;

@Service
public class AnyInventorShowService extends AbstractService<Any, Inventor> {

	@Autowired
	private AnyInventorRepository	repository;

	private Inventor				inventor;


	@Override
	public void authorise() {
		boolean status;

		if (!super.getRequest().hasData("id", int.class))
			status = false;
		else
			status = this.inventor != null;

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		try {
			if (super.getRequest().hasData("id", int.class)) {
				int id = super.getRequest().getData("id", int.class);
				this.inventor = this.repository.findInventorById(id);
			}
		} catch (Exception e) {
			this.inventor = null;
		}
	}

	@Override
	public void unbind() {
		super.unbindObject(this.inventor, "bio", "keyWords", "licensed");
	}
}
