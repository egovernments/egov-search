/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.search.domain.entities;

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
    @Searchable(group = Searchable.Group.COMMON)
    private Department currentDepartment;
    @Searchable(name = "previous_department")
    private Department previousDepartment;
    @Searchable(group = Searchable.Group.CLAUSES)
    private String status;
    @Searchable(name = "car")
    private Vehicle vehicle;

    @Searchable
    private List<Vehicle> vehicles;

    @Searchable
    private Map<String, Address> communicationAddresses;


    private String role;

    public static Person newInstance() {
        Person person = new Person();
        person.name = "Elzan";
        person.age = 37;
        person.citizen = true;
        person.role = "Manager";
        person.status = "ACTIVE";

        person.jobs = Arrays.asList("Job One", "Job Two");

        person.serviceHistory = new HashMap<>();
        person.serviceHistory.put("company1", "Job One");
        person.serviceHistory.put("company2", "Job Two");

        person.currentDepartment = new Department(1, "deptName", "deptCode");
        person.currentDepartment.setAddress(new Address("mg road", "560001"));

        person.previousDepartment = new Department(2, "oldDeptName", "oldDeptCode");

        Address addr1 = new Address("str1", "560001");
        Address addr2 = new Address("str2", "560002");
        person.addresses = Arrays.asList(addr1, addr2);

        person.communicationAddresses = new HashMap<>();
        person.communicationAddresses.put("primary", addr1);
        person.communicationAddresses.put("secondary", new Address("str2", null));

        person.vehicle = new Car("ferrari");
        person.vehicles = Arrays.asList(new Car("ferrari"), new Bike("bullet"));

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

    public Map<String, Address> getCommunicationAddresses() {
        return communicationAddresses;
    }

    public String getStatus() {
        return status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}
