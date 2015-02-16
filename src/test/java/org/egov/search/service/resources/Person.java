package org.egov.search.service.resources;

import org.egov.search.domain.Searchable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {

    @Searchable(name = "citizen_name")
    private String name;
    @Searchable
    private int age;
    @Searchable
    private boolean citizen;
    @Searchable
    private List<String> jobs;
    @Searchable
    private Map<String, String> serviceHistory;
    @Searchable
    private List<Address> addresses;
    @Searchable
    private Department currentDepartment;
    @Searchable(name = "previous_department")
    private Department previousDepartment;

    private String role;

    public static Person newInstance() {
        Person person = new Person();
        person.name = "Elzan";
        person.age = 37;
        person.citizen = true;
        person.role = "Manager";

        person.jobs = Arrays.asList("Job One", "Job Two");

        person.serviceHistory = new HashMap<>();
        person.serviceHistory.put("company1", "Job One");
        person.serviceHistory.put("company2", "Job Two");

        person.currentDepartment = new Department(1, "deptName", "deptCode");
        person.currentDepartment.setAddress(new Address("mg road", "560001"));

        person.previousDepartment = new Department(2, "oldDeptName", "oldDeptCode");

        person.addresses = Arrays.asList(new Address("str1", "560001"), new Address("str2", "560002"));

        return person;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isCitizen() {
        return citizen;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public String getRole() {
        return role;
    }

    public Map<String, String> getServiceHistory() {
        return serviceHistory;
    }

    public Department getCurrentDepartment() {
        return currentDepartment;
    }

    public Department getPreviousDepartment() {
        return previousDepartment;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
