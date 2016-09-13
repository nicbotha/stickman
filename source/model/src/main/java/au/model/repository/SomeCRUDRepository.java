package au.model.repository;

import org.springframework.data.repository.CrudRepository;

import au.model.entity.SomeEntity;

public interface SomeCRUDRepository extends CrudRepository<SomeEntity, String> {

	
}
