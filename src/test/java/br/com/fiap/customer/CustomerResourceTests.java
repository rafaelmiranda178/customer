package br.com.fiap.customer;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceTests {

    @Autowired
    private CustomerRepository customerRepository;
    @LocalServerPort
    private Integer port;


    @Before
    public void setup(){
        stubCreateCustomer();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .build();
    }

    private void stubCreateCustomer() {

        Customer customer = new Customer();
        customer.setName("Rafael");
        customer.setLastName("Miranda");
        customer.setGender("Male");
        customer.setAge(30);

        customerRepository.save(customer);

    }

    @Test
    public void shouldFindCustomerById() {
        RestAssured.get("/customer/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", Matchers.is("Rafael"))
                .body("lastName", Matchers.is("Miranda"))
                .body("gender", Matchers.is("Male"))
                .body("age", Matchers.is(30))
        ;
    }

    @Test
    public void cannotFindCustomerById(){
        RestAssured.get("/customer/300")
                .then()
                .assertThat()
                .statusCode(404)
                .body("messageError", Matchers.is("Customer Not Found."));
    }

    @Test
    public void shouldCreateCustomer(){
        CustomerRequest customerRequest = new CustomerRequest();

        customerRequest.setName("Rafael");
        customerRequest.setLastName("Miranda");
        customerRequest.setGender("Male");
        customerRequest.setAge(30);

        RestAssured.given()
                .body(customerRequest)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(201)
                .body("customerId", Matchers.any(Integer.class));


    }

    @Test
    public void cannotCreateCustomerWhenGenderIsInvalid(){
        CustomerRequest customerRequest = new CustomerRequest();

        customerRequest.setName("Rafael");
        customerRequest.setLastName("Miranda");
        customerRequest.setGender("animal");
        customerRequest.setAge(30);

        RestAssured.given()
                .body(customerRequest)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(422)
                .body("messageError", Matchers.is("Gender Is Invalid."));


    }


}
