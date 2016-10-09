package org.aleejo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.Date;


import org.aleejo.dao.EtudiantRepository;
import org.aleejo.entities.Etudiant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.*;

@SpringBootApplication
public class TpSpringMvcApplication {

	public static void main(String[] args) throws ParseException {
		ApplicationContext ctx= SpringApplication.run(TpSpringMvcApplication.class, args);
		EtudiantRepository etuRep= ctx.getBean(EtudiantRepository.class);
		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
		etuRep.save(new Etudiant("alejito", df.parse("1985-12-17"),"aleejo500@....", "mydocs"));
		etuRep.save(new Etudiant("erica", df.parse("1990-12-17"),"er500@....", "mydocs2"));
		etuRep.save(new Etudiant("natiss", df.parse("1983-12-17"),"natejo500@....", "mydocs3"));
		etuRep.save(new Etudiant("gordo", df.parse("1982-12-17"),"jacejo500@....", "mydocs4"));
		
		Page<Etudiant> etdts= etuRep.findAll(new PageRequest(0, 3)); //OK
		
		//Page<Etudiant> etdts=etuRep.chercherEtu("%J%",new PageRequest(0, 5)); //OK
		
		//List<Etudiant> etdts=etuRep.chercherEtu(df.parse("1980-01-01"),df.parse("1985-01-01")); //OK
		
		etdts.forEach(e->System.out.println(e.getNom())); //Expressions Java8
		
	}
}
