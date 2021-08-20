package com.nagarro.service;

import java.util.List;

import com.nagarro.entities.Employee;

public interface EmployeeService {

	List<Employee> getEmployeeList();
    Employee updateEmployee(Employee employee);
    void uploadEmployee(Employee employees);
	boolean deleteEmployee(Long employeeCode);
	Employee getEmployeeById(Long employeeCode);
}
