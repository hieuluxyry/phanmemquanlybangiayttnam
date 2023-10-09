package com.Services;

import com.Models.Customer;

import java.util.List;

public interface CustomerService {

    public List<Customer> getList();

    public boolean insert(Customer customer);

    public boolean update(Customer customer, String code);

    public boolean delete(String code);

    public List<Customer> search(String sdt);

    public String getByFisrtName(String code);

    public String getByLastName(String code);

    public String getByID(String phoneNumber);

    public String getByEmail(String phoneNumber);

    public String getByName(String phoneNumber);

    public boolean checkCustomer(String phone);
}
