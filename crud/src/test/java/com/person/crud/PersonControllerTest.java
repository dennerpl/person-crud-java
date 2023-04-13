package com.person.crud;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.person.crud.dto.AddressDto;
import com.person.crud.model.Address;
import com.person.crud.model.Person;
import com.person.crud.repository.AddressRepository;
import com.person.crud.repository.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private AddressRepository addressRepository;

	private Person person1;

	private Person person2;

	private Address address1;

	private Address address2;

	@BeforeEach
	public void setup() {
		person1 = new Person();
		person1.setName("João da Silva");
		person1.setBirthdate(LocalDate.of(1990, 1, 1));

		person2 = new Person();
		person2.setName("João da Silva");
		person2.setBirthdate(LocalDate.of(1990, 1, 1));

		address1 = new Address();
		address1.setStreet("Rua das Flores");
		address1.setCep("12345-678");
		address1.setNumber("123");
		address1.setCity("São Paulo");
		address1.setPrincipal(false);
		address1.setPerson(person1);

		address2 = new Address();
		address2.setStreet("Rua dos Girassóis");
		address2.setCep("98765-432");
		address2.setNumber("456");
		address2.setCity("Rio de Janeiro");
		address2.setPrincipal(true);
		address2.setPerson(person1);
	}

	@Test
	@DirtiesContext
	public void createPersonTest() {
		ResponseEntity<Person> response = restTemplate.postForEntity("/pessoas", person1, Person.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getId()).isNotNull();

		Optional<Person> optionalPerson = personRepository.findById(response.getBody().getId());
		assertThat(optionalPerson).isPresent();

		Person savedPerson = optionalPerson.get();
		assertThat(savedPerson.getName()).isEqualTo(person1.getName());
		assertThat(savedPerson.getBirthdate()).isEqualTo(person1.getBirthdate());
		assertThat(savedPerson.getAdresses()).isEmpty();
	}

	@Test
	@DirtiesContext
	public void updatePersonTest() {
		person1 = personRepository.save(person1);

		Person updatedPerson = new Person();
		updatedPerson.setName("João da Silva Santos");
		updatedPerson.setBirthdate(LocalDate.of(1990, 1, 1));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Person> request = new HttpEntity<>(updatedPerson, headers);
		ResponseEntity<Person> response = restTemplate.exchange("/pessoas/" + person1.getId(), HttpMethod.PUT, request,
				Person.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		Optional<Person> optionalPerson = personRepository.findById(person1.getId());
		assertThat(optionalPerson).isPresent();

		Person savedPerson = optionalPerson.get();
		assertThat(savedPerson.getName()).isEqualTo(updatedPerson.getName());
		assertThat(savedPerson.getBirthdate()).isEqualTo(updatedPerson.getBirthdate());
	}

	@Test
	@DirtiesContext
	public void getOnePersonTest() {
		person1 = personRepository.save(person1);

		ResponseEntity<Person> response = restTemplate.getForEntity("/pessoas/" + person1.getId(), Person.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo(person1.getId());
		assertThat(response.getBody().getName()).isEqualTo(person1.getName());
		assertThat(response.getBody().getBirthdate()).isEqualTo(person1.getBirthdate());
	}

	@Test
	@DirtiesContext
	public void getPersonAddressesTest() {
		person1 = personRepository.save(person1);

		addressRepository.save(address1);
		addressRepository.save(address2);

		ResponseEntity<List<AddressDto>> response = restTemplate.exchange("/pessoas/" + person1.getId() + "/enderecos",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<AddressDto>>() {
				});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(2);

		AddressDto savedAddress1 = response.getBody().get(0);
		assertThat(savedAddress1.getStreet()).isEqualTo(address1.getStreet());
		assertThat(savedAddress1.getCep()).isEqualTo(address1.getCep());
		assertThat(savedAddress1.getNumber()).isEqualTo(address1.getNumber());
		assertThat(savedAddress1.getCity()).isEqualTo(address1.getCity());
		assertThat(savedAddress1.getPrincipal()).isEqualTo(address1.getPrincipal());
		assertThat(savedAddress1.getPerson()).isEqualTo(person1.getId());

		AddressDto savedAddress2 = response.getBody().get(1);
		assertThat(savedAddress2.getStreet()).isEqualTo(address2.getStreet());
		assertThat(savedAddress2.getCep()).isEqualTo(address2.getCep());
		assertThat(savedAddress2.getNumber()).isEqualTo(address2.getNumber());
		assertThat(savedAddress2.getCity()).isEqualTo(address2.getCity());
		assertThat(savedAddress2.getPrincipal()).isEqualTo(address2.getPrincipal());
		assertThat(savedAddress2.getPerson()).isEqualTo(person1.getId());
	}

	@Test
	@DirtiesContext
	public void deletePersonTest() {
		person1 = personRepository.save(person1);
		person2 = personRepository.save(person2);

		assertThat(personRepository.findAll()).hasSize(2);

		restTemplate.delete("/pessoas/" + person1.getId());

		assertThat(personRepository.findAll()).hasSize(1);

	}

}
