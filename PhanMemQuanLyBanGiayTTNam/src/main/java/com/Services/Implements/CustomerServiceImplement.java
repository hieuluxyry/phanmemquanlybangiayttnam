package com.Services.Implements;

import com.Models.Customer;
import com.Repositories.CustomerRepository;
import com.Services.CustomerService;

import java.util.List;

public class CustomerServiceImplement implements CustomerService {

    private CustomerRepository customerRepository = new CustomerRepository();

    @Override
    public List<Customer> getList() {
        return customerRepository.getList();
    }

    @Override
    public boolean insert(Customer customer) {
        return customerRepository.insert(customer);
    }

    @Override
    public boolean update(Customer customer, String code) {
        return customerRepository.update(customer, code);
    }

    @Override
    public boolean delete(String code) {
        return customerRepository.delete(code);
    }

    @Override
    public List<Customer> search(String sdt) {
        return customerRepository.search(sdt);
    }

    @Override
    public String getByFisrtName(String code) {
        return customerRepository.getByName(code, customerRepository.getByFirstName);
    }

    @Override
    public String getByLastName(String code) {
        return customerRepository.getByName(code, customerRepository.getByLastName);
    }

    @Override
    public String getByID(String phoneNumber) {
        return customerRepository.getBy(phoneNumber, customerRepository.getByID);
    }

    @Override
    public String getByEmail(String phoneNumber) {
        if (!checkCustomer(phoneNumber)) {
            return "hhh@gmail.com";
        }
        return customerRepository.getBy(phoneNumber, customerRepository.getByEmail);
    }

    @Override
    public String getByName(String phoneNumber) {
        return customerRepository.getByName(phoneNumber, customerRepository.getByName);
    }

    @Override
    public boolean checkCustomer(String phone) {
        return customerRepository.checkCustomer(phone);
    }

}
