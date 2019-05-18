package br.com.fiap.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController
public class CustomerResource {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable("id")Integer id){
        ResponseEntity<CustomerResponse> t = ResponseEntity.ok(customerService.findById(id));
        return t;
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerCreateResponse> createCustomer(@RequestBody CustomerRequest customerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerRequest));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(HttpServerErrorException e){
        ResponseEntity<ErrorResponse> t = ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getStatusText()));
        return t;
    }


}
