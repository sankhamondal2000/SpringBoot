package com.nagarro.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.DocumentException;
import com.nagarro.entities.Employee;
import com.nagarro.entities.Employees;
import com.nagarro.entities.UserPDFExporter;
import com.nagarro.helper.Message;
import com.nagarro.repository.UserRepository;
import com.nagarro.service.EmployeeService;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmployeeService employeeService;
	

	@GetMapping("/")
	public String signUp(Model model) {

		model.addAttribute("title", "SignUp page");
		model.addAttribute("employees", new Employees());
		return "signup";
	}

	// hander for register user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("employees") Employees employees, Model model, HttpSession session) {

		try {

			employees.setPassword(passwordEncoder.encode(employees.getPassword()));
			employees.setRole("ROLE_USER");
			Employees result = this.userRepository.save(employees);
			model.addAttribute("employees", new Employees());
			return "details_page";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("employees", employees);
			return "signup";
		}

	}

	// handler for custom login
	@GetMapping("/signin")
	private String customLogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}

	@GetMapping("/addemp")
	public String addEmp() {
		return "add";
	}

	@PostMapping("/register")
	public String empRegister(@ModelAttribute Employee e, Model m) {
		employeeService.uploadEmployee(e);
		List<Employee> emp = employeeService.getEmployeeList();
		m.addAttribute("emp", emp);
		return "details_page";
	}

	@GetMapping("/edit/{employeeCode}")
	public String edit(@ModelAttribute Employee e, @PathVariable Long employeeCode, Model m) {
		// System.out.println(emp);
		// Employee e1=employeeService.updateEmployee(emp);
		Employee e1 = employeeService.getEmployeeById(employeeCode);
		employeeService.updateEmployee(e1);
		m.addAttribute("emp", e1);
		return "edit";

	}

	@PostMapping("/update")
	public String updateEmp(@ModelAttribute Employee e) {
		employeeService.uploadEmployee(e);
		return "redirect:/user/index";
	}

	
	 @GetMapping("/delete/{employeeCode}") public String deleteEmp(@PathVariable Long employeeCode) 
	 { 
		 boolean deleteEmployee = employeeService.deleteEmployee(employeeCode); 
		 if(deleteEmployee)
		 {
			 return "redirect:/user/index";
		 }
		 return "details_page";
		 
	 }
	 
	 @GetMapping("/export")
	 public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException
	 {
		 response.setContentType("application/pdf");
		 
		 DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		 String currentDateTime=dateFormatter.format(new Date());
		 String headerKey="content-Disposition";
		 String headerValue="attachment; filename=users_" + currentDateTime + ".pdf";
		 
		 response.setHeader(headerKey, headerValue);
		 
		 List<Employee> listUsers=employeeService.getEmployeeList();
		 
		 UserPDFExporter exporter = new UserPDFExporter(listUsers);
		 exporter.export(response);
	 }
	 

}
