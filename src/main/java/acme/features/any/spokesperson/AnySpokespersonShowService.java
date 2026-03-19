
package acme.features.any.spokesperson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AbstractService;

import acme.realms.Spokesperson;

@Service
public class AnySpokespersonShowService extends AbstractService<Any, Spokesperson> {

	@Autowired
	private AnySpokespersonRepository	repository;

	private Spokesperson				spokesperson;


	@Override
	public void authorise() {
		boolean status;

		if (!super.getRequest().hasData("id"))
			status = false;
		else
			status = this.spokesperson != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.spokesperson = this.repository.findSpokespersonById(id);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.spokesperson, "cv", "achievements", "licensed");
		tuple.put("fullName", this.spokesperson.getIdentity().getFullName());
	}
}
