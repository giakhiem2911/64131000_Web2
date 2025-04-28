package khiem.nhg.demoJPA.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import khiem.nhg.demoJPA.model.Customer;
import khiem.nhg.demoJPA.repository.CustomerRepository;

//@Controller

@RestController
public class CustomerController {
	@Autowired
	CustomerRepository myCustomerRepository;
	// Các action ở đây
	@GetMapping("/customer/all")
	public String getAll(Model m)
	{
		List<Customer> dskh = new ArrayList<Customer>();
		dskh = myCustomerRepository.findAll();
		m.addAttribute("dskh", dskh);
		return null;
	}
}
