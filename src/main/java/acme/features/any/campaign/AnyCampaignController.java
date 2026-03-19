
package acme.features.any.campaign;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.AbstractController;

import acme.entities.campaign.Campaign;

@Controller
public class AnyCampaignController extends AbstractController<Any, Campaign> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AnyCampaignListService.class);
		super.addBasicCommand("show", AnyCampaignShowService.class);
	}
}
