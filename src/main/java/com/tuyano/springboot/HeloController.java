package com.tuyano.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.PostConstruct;

import com.tuyano.springboot.repositories.MyDataRepository;
 

@Controller
public class HeloController {
	
	@Autowired
	MyDataRepository repository;
	
	@PostConstruct
	 public void init(){
		MyData d1 = new MyData();
		d1.setTitle("tuyano");
		d1.setAge(123);
		d1.setMail("syoda@tuyano.com");
		d1.setMemo("this is my data!");
		repository.saveAndFlush(d1);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(
			@ModelAttribute("formModel") MyData mydata, 
			ModelAndView mav) {
		mav.setViewName("home");
		mav.addObject("msg","this is sample content.");
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist",list);
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@ModelAttribute("formModel") MyData mydata, 
			ModelAndView mav) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}
	

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(
			@ModelAttribute("formModel") MyData mydata, 
			ModelAndView mav) {
		mav.setViewName("view");
		mav.addObject("msg","this is sample content.");
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist",list);		
		return mav;
	}
//	@RequestMapping(value = "/view/{id}" method = RequestMethod.GET)
//	public ModelAndView view_id(
//			@ModelAttribute("formModel") MyData mydata, 
//			ModelAndView mav) {
//		mav.setViewName("view1");
//		mav.addObject("msg","this is sample content.");
//		Iterable<MyData> list = repository.findAll();
//		mav.addObject("datalist",list);
//		return mav;
//	}
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public ModelAndView view_id(@ModelAttribute MyData mydata, 
			@PathVariable int id,ModelAndView mav) {
		mav.setViewName("view1");
	mav.addObject("title","edit mydata.");
		Optional<MyData> data = repository.findById((long)id);
		Iterable<MyData> list = repository.findAll();
		mav.addObject("formModel",data.get());
		mav.addObject("datalist",list);	
		return mav;
	}

//	@RequestMapping(value = "/view}", method = RequestMethod.POST)
//	@Transactional(readOnly=false)
//	public ModelAndView view1(@ModelAttribute MyData mydata, 
//			ModelAndView mav) {
//		repository.saveAndFlush(mydata);
//		return new ModelAndView("redirect:/");
//	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@ModelAttribute MyData mydata, 
			@PathVariable int id,ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title","edit mydata.");
		Optional<MyData> data = repository.findById((long)id);
		mav.addObject("formModel",data.get());
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView update(@ModelAttribute MyData mydata, 
			ModelAndView mav) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}

@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
public ModelAndView delete(@PathVariable int id,
		ModelAndView mav) {
	mav.setViewName("delete");
	mav.addObject("title","delete mydata.");
	Optional<MyData> data = repository.findById((long)id);
	mav.addObject("formModel",data.get());
	return mav;
}

@RequestMapping(value = "/delete", method = RequestMethod.POST)
@Transactional(readOnly=false)
public ModelAndView remove(@RequestParam long id, 
		ModelAndView mav) {
	repository.deleteById(id);
	return new ModelAndView("redirect:/");
}
}