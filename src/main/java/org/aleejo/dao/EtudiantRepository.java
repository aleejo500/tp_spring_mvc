package org.aleejo.dao;


import java.util.Date;
import java.util.List;

import org.aleejo.entities.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EtudiantRepository 
	extends JpaRepository<Etudiant, Long> {
	
	public List<Etudiant> findByNom(String nomm);
	public Page<Etudiant> findByNom(String nomm, Pageable pageable);//dialect s'adapte selon mapping
	
	
	@Query("Select e from Etudiant e where e.nom like :x")
	public Page<Etudiant> chercherEtu(@Param("x")String mc, Pageable pageable);
	
	@Query("Select e from Etudiant e where e.dateNaissance >:x and e.dateNaissance <:y")
	public List<Etudiant> chercherEtu(@Param("x")Date d1, @Param("y")Date d2);
	
	
	
	
}
