package br.com.fiap.customer;


public interface CustomerService {

    CustomerResponse findById(Integer id);

    CustomerCreateResponse create(CustomerRequest customerRequest);


}
