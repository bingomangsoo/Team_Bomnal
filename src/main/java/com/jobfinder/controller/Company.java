package com.jobfinder.controller;

import com.jobfinder.domain.*;
import com.jobfinder.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class Company {
	@Autowired
	CompanyService companyService;

	@GetMapping("/company")
	public String CompanyListMain(Criteria cri, Model model, HttpSession session){
		ArrayList<CompanyList> list = companyService.listmain(cri);
		int counting = companyService.countmain();

		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(counting);

		model.addAttribute("list",list);
		model.addAttribute("counting",counting);
		session.setAttribute("pageMaker",pageMaker);
		return "CompanyList";
	}

	@GetMapping("/company/search")
	public String CompanySearchList(@RequestParam String keyword,Criteria cri, Model model, HttpSession session){
		cri.setKeyword(keyword);
		ArrayList<CompanyList> list = companyService.listsearch(cri);
		int counting = companyService.countsearch(keyword);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(counting);
		model.addAttribute("list",list);
		model.addAttribute("counting",counting);
		model.addAttribute("keyword",keyword);
		session.setAttribute("pageMaker",pageMaker);
		return "CompanyList";
	}

	@GetMapping("/company/industry/{industry_class}")
	public String CompanyList(@PathVariable String industry_class, Criteria cri, Model model, HttpSession session){
		cri.setIndustry_class(industry_class);
		List<Company_info> list = companyService.list(cri);
		int counting = companyService.count(industry_class);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(counting);


		model.addAttribute("list",list);
		model.addAttribute("counting",counting);
		session.setAttribute("pageMaker",pageMaker);
		return "CompanyList";
	}
	
	@GetMapping("/company/{company_id}")
	public String CompanyDetail(@PathVariable String company_id, Model model) {
		Company_info detail = companyService.detail(company_id);
		int countgongo = companyService.countgongo(company_id);
		model.addAttribute("detail",detail);
		model.addAttribute("countgongo",countgongo);
		return "CompanyDetail";
	}

	@GetMapping("/company/review/{company_id}")
	public String CompanyReview(@PathVariable String company_id, Model model, HttpSession session, Reviews reviews) {
		Company_info detail = companyService.detail(company_id);


		LoginVO vo = (LoginVO)session.getAttribute("VO");
		reviews.setCompany_id(company_id);

		if (vo != null) {
			reviews.setMem_id(vo.getMem_id());
		}

		ArrayList<Reviews> review = companyService.review(reviews);
		Reviews avg = companyService.avg(company_id);
		int countgongo = companyService.countgongo(company_id);
		int countreview = companyService.countreview(company_id);
		model.addAttribute("review",review);
		model.addAttribute("detail",detail);
		model.addAttribute("avg",avg);
		model.addAttribute("countgongo",countgongo);
		model.addAttribute("countreview",countreview);


		return "CompanyReview";
	}

	@PostMapping("/like")
	@ResponseBody
	public Review_like likeinsert(Review_like like, HttpSession session, Model model){
		LoginVO vo = (LoginVO) session.getAttribute("VO");
		like.setMem_id(vo.getMem_id());
		companyService.likeinsert(like);

		return null;
	}

	@DeleteMapping("/like")
	@ResponseBody
	public Review_like likedelete(Review_like like, HttpSession session, Model model){
		LoginVO vo = (LoginVO) session.getAttribute("VO");
		like.setMem_id(vo.getMem_id());

		companyService.likedelete(like);

		return null;
	}




	@PostMapping("/company/review/{company_id}")
	public String save(@ModelAttribute Reviews reviews, @PathVariable String company_id, HttpSession session){
		System.out.println("reviews = " + reviews);

		companyService.save(reviews);


		return "redirect:/companyReview/{company_id}";
	}

	@GetMapping("/company/recruit/{company_id}")
	public String Recruit(@PathVariable String company_id, Model model){
		Company_info detail = companyService.detail(company_id);
		ArrayList<Recruit> gongo = companyService.gongo(company_id);
		int countgongo = companyService.countgongo(company_id);
		model.addAttribute("detail",detail);
		model.addAttribute("gongo",gongo);
		model.addAttribute("countgongo",countgongo);
		return "CompanyRecruit";
	}

}
