
package acme.features.any.milestone;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;

@Repository
public interface AnyMilestoneRepository extends AbstractRepository {

	@Query("select m from Milestone m where m.campaign.id = :campaignId and m.campaign.draftMode = false")
	List<Milestone> findMilestonesByCampaignId(int campaignId);

	@Query("select m from Milestone m where m.id = :milestoneId")
	Milestone findMilestoneById(int milestoneId);

	@Query("select c from Campaign c where c.id = :campaignId")
	public Campaign findCampaignById(int campaignId);
}
