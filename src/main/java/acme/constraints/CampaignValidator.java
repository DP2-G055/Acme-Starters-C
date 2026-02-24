
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.CampaignRepository;
import acme.entities.milestone.Milestone;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign> {

	@Autowired
	private CampaignRepository repository;


	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {

		assert context != null;
		boolean result;

		if (campaign == null)
			result = true;
		else {
			{
				boolean uniqueCampaign;
				Campaign existingCampaign;

				existingCampaign = this.repository.findCampaignByTicker(campaign.getTicker());
				uniqueCampaign = existingCampaign == null || existingCampaign.equals(campaign);

				super.state(context, uniqueCampaign, "ticker", "acme.validation.campaign.duplicated-ticker.message");
			}
			{
				boolean campaignHasMilestone;
				List<Milestone> milestones = this.repository.getMilestonesByCampaignId(campaign.getId());

				campaignHasMilestone = campaign.getDraftMode() || !milestones.isEmpty();

				super.state(context, campaignHasMilestone, "draftMode", "acme.validation.campaign.no-milestones.message");
			}
			{
				boolean validInterval;
				if (campaign.getStartMoment() != null && campaign.getEndMoment() != null)
					validInterval = campaign.getStartMoment().before(campaign.getEndMoment());
				else
					validInterval = false;

				super.state(context, validInterval, "endMoment", "acme.validation.campaign.invalid-interval.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
