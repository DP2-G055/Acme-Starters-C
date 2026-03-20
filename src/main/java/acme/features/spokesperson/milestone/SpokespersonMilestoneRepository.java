
package acme.features.spokesperson.milestone;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Repository
public interface SpokespersonMilestoneRepository extends AbstractRepository {

	@Query("select s from Spokesperson s where s.userAccount.id = :userAccountId")
	Spokesperson findSpokespersonByUserAccountId(int userAccountId);

	@Query("select c from Campaign c where c.id = :campaignId")
	Campaign findCampaignById(int campaignId);

	@Query("select m from Milestone m where m.id = :id")
	Milestone findMilestoneById(int id);

	@Query("select m from Milestone m where m.campaign.id = :campaignId")
	List<Milestone> findMilestonesByCampaignId(int campaignId);
}
