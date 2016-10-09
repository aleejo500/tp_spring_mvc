package org.aleejo.web;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.aleejo.dao.EtudiantRepository;
import org.aleejo.entities.Etudiant;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import scala.annotation.meta.field;

@Controller
@RequestMapping(value="/Etudiant")
public class EtudiantController {
	@Autowired //injecter dependences 
	private EtudiantRepository etuRep;
	@Value("${dir.images}")
	private String DirIma;
	
	
	@RequestMapping(value="/Index")								//defaultvalue oblig
	//public String Index(Model model, @RequestParam(name="page", defaultValue="0")int pg){// ok
	public String Index(Model model, 
			@RequestParam(name="page", defaultValue="0")int pg,
			@RequestParam(name="motCle", defaultValue="")String mc){
	
		/*List<Etudiant> etds=etuRep.findAll(); //ok
		
	  //Page<Etudiant> etds=etuRep.findAll(new PageRequest(pg, 2)); //OK
		*/
		Page<Etudiant> etds= etuRep.chercherEtu("%"+mc+"%",(new PageRequest(pg, 10)));
		//model.addAttribute("etudiants", etds); //pour list
		int pCount=etds.getTotalPages();
		int[] pags = new int[pCount];
		for(int i =0;i<pCount;i++)pags[i]=i;
		model.addAttribute("pages", pags);
		model.addAttribute("PgEtudiants", etds); 
		model.addAttribute("pageCourante", pg);
		model.addAttribute("motCle", mc);
		return "etudiants";
		
	}
	
	@RequestMapping(value="/form",method=RequestMethod.GET)
	public String formEtudiant(Model model){
		
		model.addAttribute("etudiant", new Etudiant());
		return "FormEtudiant";
		
	}
	
	@RequestMapping(value="/saveEtudiant", method=RequestMethod.POST)
	public String save(@Valid Etudiant et, 
						BindingResult BiRes,
						@RequestParam(name="pict")MultipartFile file) throws Exception {
		if(BiRes.hasErrors()){
			return "FormEtudiant";
		}
		etuRep.save(et);
		if(!file.isEmpty()){
			file.transferTo(new File(DirIma+et.getId()));
			et.setPhoto(DirIma+et.getId());
		}
		
		
		return "redirect:Index";
	}
	
	@RequestMapping(value="/getPhoto", produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getPhoto(Long id) throws Exception{
		File f=new File(DirIma+id);
		return IOUtils.toByteArray(new FileInputStream(f));
	}
	
	@RequestMapping(value="/supprimer")
	public String supprimer(Long id){
		
		etuRep.delete(id);
		return "redirect:Index";
		
	}
	
	@RequestMapping(value="/edit")
	public String edit(Long id, Model model){
		Etudiant et=etuRep.getOne(id);
		model.addAttribute("etudiant", et);
		return "EditEtudiant";
		
	}
	
	@RequestMapping(value="/UpdateEtudiant", method=RequestMethod.POST)
	public String Update(@Valid Etudiant et, 
						BindingResult BiRes,
						@RequestParam(name="pict")MultipartFile file) throws Exception {
		if(BiRes.hasErrors()){
			return "EditEtudiant";
		}
		etuRep.save(et);
		if(!file.isEmpty()){
			file.transferTo(new File(DirIma+et.getId()));
			et.setPhoto(DirIma+et.getId());
		}
		
		
		return "redirect:Index";
	}
	
}
