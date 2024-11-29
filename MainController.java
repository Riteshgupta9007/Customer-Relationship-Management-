package com.softpro.myapp.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.softpro.myapp.dao.AdminLoginDao;
import com.softpro.myapp.dao.CustomerDao;
import com.softpro.myapp.dao.EnquiryDao;
import com.softpro.myapp.model.AdminLogin;
import com.softpro.myapp.model.Customer;
import com.softpro.myapp.model.Enquiry;
import com.softpro.myapp.service.AdminLoginRepository;
import com.softpro.myapp.service.CustomerRepository;
import com.softpro.myapp.service.EnquiryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {

	@Autowired
	CustomerRepository crepo;
	@Autowired
	EnquiryRepository erepo;
	
	@Autowired
	AdminLoginRepository alrepo;
	
	

	@GetMapping("/")
	public String showIndex() {
		return "index";
	}

	@GetMapping("/aboutus")
	public String showAboutUs() {
		return "aboutus";
	}

	@GetMapping("/registration")
	public String showRegistration(Model model) {
		CustomerDao customerDao = new CustomerDao();
		model.addAttribute("customerDao", customerDao);
		return "registration";
	}

	@PostMapping("/registration")
	public String createCustomer(@Valid @ModelAttribute CustomerDao customerDao, BindingResult result,
			RedirectAttributes attrib) {
		if (result.hasErrors()) {
			return "registration";
		}
		Customer customer = new Customer();
		customer.setName(customerDao.getName());
		customer.setGender(customerDao.getGender());
		customer.setAddress(customerDao.getAddress());
		customer.setContactno(customerDao.getContactno());
		customer.setEmailaddress(customerDao.getEmailaddress());
		customer.setPassword(customerDao.getPassword());
		customer.setRegdate(new Date() + "");
		crepo.save(customer);
		attrib.addFlashAttribute("message", "Registration is done");
		return "redirect:/registration";
	}

	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	@PostMapping("/login")
	public String validateUser(@ModelAttribute CustomerDao customerDao,HttpSession session, RedirectAttributes redirectAttributes)
	{
		try {
			Customer c=crepo.getById(customerDao.getEmailaddress());
			if(c.getPassword().equals(customerDao.getPassword()))
			{
				//redirectAttributes.addFlashAttribute("message","Valid User");
				session.setAttribute("userid", customerDao.getEmailaddress());
				return "redirect:/customer/";
			}
			else
			{
				redirectAttributes.addFlashAttribute("message","Invalid User");
			}
		}
		catch(EntityNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", "Customer does not exist");
		}
		return "redirect:/login";
	}

	@GetMapping("/contactus")
	public String showContactUs(Model model) {
		EnquiryDao enquiryDao = new EnquiryDao();
		model.addAttribute("enquiryDao", enquiryDao);
		return "contactus";
	}

	@PostMapping("/contactus")
	public String createEnquiry(@Valid @ModelAttribute EnquiryDao enquiryDao, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "contactus";
		}
		Enquiry enquiry=new Enquiry();
		enquiry.setName(enquiryDao.getName());
		enquiry.setContactno(enquiryDao.getContactno());
		enquiry.setSubject(enquiryDao.getSubject());
		enquiry.setMessage(enquiryDao.getMessage());
		enquiry.setPosteddate(new Date()+"");
		erepo.save(enquiry);
		redirectAttributes.addFlashAttribute("message", "Enquiry is saved");
		return "redirect:/contactus";
	}
	@GetMapping("/adminlogin")
	public String showAdminLogin(Model model ) {
		
		AdminLoginDao adminLoginDao=new AdminLoginDao();
		model.addAttribute("adminLoginDao",adminLoginDao);
		return "adminlogin";
	}
	@PostMapping("/adminlogin")
	public String validateAdmin(@ModelAttribute AdminLoginDao adminLoginDao, HttpSession session, RedirectAttributes attrib ) {
		
		try {
			AdminLogin al=alrepo.getById(adminLoginDao.getAdminid());
			if(al.getAdminpassword().equals(adminLoginDao.getAdminpassword()))
			{
				//attrib.addFlashAttribute("message","Valid User");
				session.setAttribute("adminid", al.getAdminid());
				return "redirect:/admin/";
			}
			else
			{
				attrib.addFlashAttribute("message","Invalid User");
			}
			
		}
		catch(EntityNotFoundException ex){
			
			attrib.addFlashAttribute("message","User does not exist");
		}
		return "redirect:/adminlogin";
	}
	
}
