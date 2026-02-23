
package acme.entities.invention;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface InventionRepository extends AbstractRepository {

	@Query("select sum(p.cost.amount) from Part p WHERE p.invention.id = :inventionId")
	Double findMoneyByInventionId(int inventionId);

	@Query("select count(p) from Part p where p.invention.id = :inventionId and p.draftMode = true")
	Integer countPartsByInventionId(int inventionId);
}
