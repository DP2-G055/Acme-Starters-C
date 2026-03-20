
package acme.features.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignShowService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;


	@Override
	public void authorise() {
		boolean status;
		int userAccountId;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			status = this.campaign != null && this.campaign.getSpokesperson().getUserAccount().getId() == userAccountId;
		}

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		super.unbindGlobal("campaignId", this.campaign.getId());
	}
}
