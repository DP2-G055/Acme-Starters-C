
package acme.features.inventor.part;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invention.Invention;
import acme.entities.part.Part;

@Repository
public interface InventorPartRepository extends AbstractRepository {

	@Query("select p from Part p where p.invention.id = :inventionId")
	public List<Part> findAllPartsByInventionId(int inventionId);

	@Query("select p from Part p where p.id = :partId")
	public Part findPartById(int partId);

	@Query("select i from Invention i where i.id = :inventionId")
	public Invention findInventionById(int inventionId);
}
