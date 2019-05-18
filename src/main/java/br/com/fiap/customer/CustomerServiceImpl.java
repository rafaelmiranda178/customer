package br.com.fiap.customer;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service()
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerResponse findById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(NOT_FOUND, "Customer Not Found."));
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setName(customer.getName());
        customerResponse.setLastName(customer.getLastName());
        customerResponse.setGender(customer.getGender());
        customerResponse.setAge(customer.getAge());
        return customerResponse;
    }

    @Override
    public CustomerCreateResponse create(CustomerRequest customerRequest) {
        validateGender(customerRequest.getGender());

        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setLastName(customerRequest.getLastName());
        customer.setGender(customerRequest.getGender());
        customer.setAge(customerRequest.getAge());

        Customer createdCustomer = customerRepository.save(customer);

        CustomerCreateResponse customerCreateResponse = new CustomerCreateResponse();
        customerCreateResponse.setCustomerId(createdCustomer.getId());

        return customerCreateResponse;
    }

    private void validateGender(String gender) {
        Stream.of("MALE", "FEMALE")
                .filter(s -> s.equals(gender.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Gender Is Invalid."));
    }
}
