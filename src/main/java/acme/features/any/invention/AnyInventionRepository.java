
package acme.features.any.invention;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invention.Invention;

@Repository
public interface AnyInventionRepository extends AbstractRepository {

	@Query("select i from Invention i where i.draftMode = false")
	public List<Invention> findAllPublishedInventions();

	@Query("select i from Invention i where i.id = :inventionId")
	public Invention findInventionById(int inventionId);
}
