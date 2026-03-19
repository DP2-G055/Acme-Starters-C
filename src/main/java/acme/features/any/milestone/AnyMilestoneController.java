
package acme.features.any.milestone;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.AbstractController;

import acme.entities.milestone.Milestone;

@Controller
public class AnyMilestoneController extends AbstractController<Any, Milestone> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AnyMilestoneListService.class);
		super.addBasicCommand("show", AnyMilestoneShowService.class);
	}
}
