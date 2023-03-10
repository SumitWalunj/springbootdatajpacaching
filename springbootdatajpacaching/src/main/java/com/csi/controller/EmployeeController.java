package com.csi.controller;

import com.csi.exception.RecordNotFound;
import com.csi.model.Employee;
import com.csi.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeServiceImpl employeeServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Employee employee) {

        employeeServiceImpl.signUp(employee);

        return ResponseEntity.ok("Data Saved Successfully");
    }

    @GetMapping("/signin/{empEmailId}/{empPassword}")
    public ResponseEntity<Boolean> signIn(@PathVariable String empEmailId, @PathVariable String empPassword) {

        return ResponseEntity.ok(employeeServiceImpl.signIn(empEmailId, empPassword));
    }

    @GetMapping("/getdatabyid/{empId}")
    public ResponseEntity<Employee> getDataById(@PathVariable int empId) {

        return ResponseEntity.ok(employeeServiceImpl.getDataById(empId).orElseThrow(() -> new RecordNotFound("Id Not Exist")));
    }

    @GetMapping("/getalldata")
    public ResponseEntity<List<Employee>> getAllData() {

        return ResponseEntity.ok(employeeServiceImpl.getAllData());
    }

    @PutMapping("/updatedata/{empId}")
    public ResponseEntity<Employee> updateData(@PathVariable int empId, @RequestBody Employee employee) {

        Employee employee1 = employeeServiceImpl.getDataById(empId).orElseThrow(() -> new RecordNotFound("Id Not Exist"));

        employee1.setEmpName(employee.getEmpName());
        employee1.setEmpSalary(employee.getEmpSalary());
        employee1.setEmpDOB(employee.getEmpDOB());
        employee1.setEmpEmailId(employee.getEmpEmailId());
        employee1.setEmpPassword(employee.getEmpPassword());

        return ResponseEntity.ok(employeeServiceImpl.updateData(employee1));
    }

    @DeleteMapping("/deletebyid/{empId}")
    public ResponseEntity<String> deleteById(@PathVariable int empId) {

        employeeServiceImpl.deleteById(empId);

        return ResponseEntity.ok("Data Deleted Successfully");
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteAll() {

        employeeServiceImpl.deleteAll();

        return ResponseEntity.ok(" Data Deleted Successfully");
    }

    @GetMapping("/getdatabyname/{empName}")
    public ResponseEntity<List<Employee>> getDataById(@PathVariable String empName) {

        return ResponseEntity.ok(employeeServiceImpl.getAllData().stream().filter(emp -> emp.getEmpName().equals(empName)).collect(Collectors.toList()));
    }

    @GetMapping("/getdatabydob/{empDOB}")

    public ResponseEntity<List<Employee>> getDataByDOB(@PathVariable String empDOB) {

        List<Employee> employeeList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (Employee employee : employeeServiceImpl.getAllData()) {

            String dob;

            dob = dateFormat.format(employee.getEmpDOB());
            if (dob.equals(empDOB)) {

                employeeList.add(employee);
            }
        }
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/getdatabysalary/{empSalary}")
    public ResponseEntity<List<Employee>> getDataBySalary(@PathVariable double empSalary) {

        return ResponseEntity.ok(employeeServiceImpl.getAllData().stream().filter(emp -> emp.getEmpSalary() == empSalary).collect(Collectors.toList()));
    }

    @GetMapping("/sortbyname")
    public ResponseEntity<List<Employee>> sortByName() {

        return ResponseEntity.ok(employeeServiceImpl.getAllData().stream().sorted(Comparator.comparing(Employee::getEmpName)).collect(Collectors.toList()));
    }

    @GetMapping("/sortbysalary")
    public ResponseEntity<List<Employee>> sortBySalary() {

        return ResponseEntity.ok(employeeServiceImpl.getAllData().stream().sorted(Comparator.comparingDouble(Employee::getEmpSalary)).collect(Collectors.toList()));
    }

    @GetMapping("/sortbydob")
    public ResponseEntity<List<Employee>> sortByDOB() {

        return ResponseEntity.ok(employeeServiceImpl.getAllData().stream().sorted(Comparator.comparing(Employee::getEmpDOB)).collect(Collectors.toList()));
    }

    @PostMapping("/savebulkofdata")
    public ResponseEntity<String> saveBulkOfData(@RequestBody List<Employee> employees) {

        employeeServiceImpl.saveBulkOfData(employees);

        return ResponseEntity.ok("Data Saved Successfully");
    }

    @GetMapping("/getdatabyanyinput/{input}")
    public ResponseEntity<List<Employee>> getDataByAnyInput(@PathVariable String input) {

        List<Employee> employeeList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Employee employee : employeeServiceImpl.getAllData()) {


            String dob;
            dob = dateFormat.format(employee.getEmpDOB());

            if (employee.getEmpName().equals(input)
                    || employee.getEmpEmailId().equals(input)
                    || String.valueOf(employee.getEmpSalary()).equals(input)
                    || String.valueOf(employee.getEmpContactNumber()).equals(input)
                    || dob.equals(input)) {
                employeeList.add(employee);
            }
        }

        return ResponseEntity.ok(employeeList);
    }


}
