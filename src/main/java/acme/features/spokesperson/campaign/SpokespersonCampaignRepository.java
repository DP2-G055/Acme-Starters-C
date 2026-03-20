
package acme.features.spokesperson.campaign;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Repository
public interface SpokespersonCampaignRepository extends AbstractRepository {

	@Query("select s from Spokesperson s where s.userAccount.id = :userAccountId")
	Spokesperson findSpokespersonByUserAccountId(int userAccountId);

	@Query("select c from Campaign c where c.spokesperson.userAccount.id = :userAccountId")
	List<Campaign> findCampaignsByUserAccountId(int userAccountId);

	@Query("select c from Campaign c where c.id = :id")
	Campaign findCampaignById(int id);

	@Query("select count(m) from Milestone m where m.campaign.id = :campaignId")
	int countMilestonesByCampaignId(int campaignId);

	@Query("select m from Milestone m where m.campaign.id = :campaignId")
	List<Milestone> findMilestonesByCampaignId(int campaignId);
}
