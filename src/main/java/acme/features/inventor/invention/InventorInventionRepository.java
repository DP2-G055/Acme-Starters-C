
package acme.features.inventor.invention;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invention.Invention;

@Repository
public interface InventorInventionRepository extends AbstractRepository {

	@Query("select i from Invention i where i.inventor.userAccount.id = :userAccountId")
	public List<Invention> findInventionByUserAccountId(int userAccountId);

	@Query("select i from Invention i where i.id = :inventionId")
	public Invention findInventionById(int inventionId);

	@Query("select count(p) from Part p where p.invention.id = :inventionId")
	Integer countPartsByInventionId(int inventionId);
}
