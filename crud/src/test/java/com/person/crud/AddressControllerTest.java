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
public class AddressControllerTest {

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
		person1 = personRepository.save(person1);

		person2 = new Person();
		person2.setName("João da Silva");
		person2.setBirthdate(LocalDate.of(1990, 1, 1));
		person2 = personRepository.save(person2);

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
	public void createAddressTest() {
		ResponseEntity<Address> response = restTemplate.postForEntity("/enderecos", address1, Address.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getId()).isNotNull();

		Optional<Address> optionalPerson = addressRepository.findById(response.getBody().getId());
		assertThat(optionalPerson).isPresent();

		Address savedAddress = optionalPerson.get();
		assertThat(response.getBody().getId()).isEqualTo(savedAddress.getId());
		assertThat(response.getBody().getCep()).isEqualTo(savedAddress.getCep());
		assertThat(response.getBody().getCity()).isEqualTo(savedAddress.getCity());
		assertThat(response.getBody().getNumber()).isEqualTo(savedAddress.getNumber());
	}

	@Test
	@DirtiesContext
	public void editAddressTest() {
		address1 = addressRepository.save(address1);

		AddressDto addressToUpdate = new AddressDto();
		addressToUpdate.setStreet("Rua dos Girassóis");
		addressToUpdate.setCep("98765-432");
		addressToUpdate.setNumber("456");
		addressToUpdate.setCity("Rio de Janeiro");
		addressToUpdate.setPrincipal(true);
		addressToUpdate.setPerson(person2.getId());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<AddressDto> request = new HttpEntity<>(addressToUpdate, headers);
		ResponseEntity<AddressDto> response = restTemplate.exchange("/enderecos/" + address1.getId(), HttpMethod.PUT,
				request, AddressDto.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		Optional<Address> optionalAddress = addressRepository.findById(address1.getId());
		assertThat(optionalAddress).isPresent();

		Address savedAddress = optionalAddress.get();
		assertThat(savedAddress.getId()).isEqualTo(address1.getId());
		assertThat(savedAddress.getCep()).isEqualTo(addressToUpdate.getCep());
		assertThat(savedAddress.getCity()).isEqualTo(addressToUpdate.getCity());
		assertThat(savedAddress.getNumber()).isEqualTo(addressToUpdate.getNumber());
		assertThat(savedAddress.getPrincipal()).isEqualTo(addressToUpdate.getPrincipal());
		assertThat(savedAddress.getPerson().getId()).isEqualTo(addressToUpdate.getPerson());
	}

	@Test
	@DirtiesContext
	public void getOneAddressTest() {
		address1 = addressRepository.save(address1);

		ResponseEntity<AddressDto> response = restTemplate.getForEntity("/enderecos/" + address1.getId(),
				AddressDto.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo(address1.getId());
		assertThat(response.getBody().getCep()).isEqualTo(address1.getCep());
		assertThat(response.getBody().getCity()).isEqualTo(address1.getCity());
		assertThat(response.getBody().getNumber()).isEqualTo(address1.getNumber());
	}

	@Test
	@DirtiesContext
	public void getAddressesTest() {
		addressRepository.save(address1);

		addressRepository.save(address2);

		ResponseEntity<List<AddressDto>> response = restTemplate.exchange("/enderecos", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<AddressDto>>() {
				});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(2);

		AddressDto bodyAddress1 = response.getBody().get(0);
		assertThat(bodyAddress1.getStreet()).isEqualTo(address1.getStreet());
		assertThat(bodyAddress1.getCep()).isEqualTo(address1.getCep());
		assertThat(bodyAddress1.getNumber()).isEqualTo(address1.getNumber());
		assertThat(bodyAddress1.getCity()).isEqualTo(address1.getCity());
		assertThat(bodyAddress1.getPrincipal()).isEqualTo(address1.getPrincipal());
		assertThat(bodyAddress1.getPerson()).isEqualTo(person1.getId());

		AddressDto bodyAddress2 = response.getBody().get(1);
		assertThat(bodyAddress2.getStreet()).isEqualTo(address2.getStreet());
		assertThat(bodyAddress2.getCep()).isEqualTo(address2.getCep());
		assertThat(bodyAddress2.getNumber()).isEqualTo(address2.getNumber());
		assertThat(bodyAddress2.getCity()).isEqualTo(address2.getCity());
		assertThat(bodyAddress2.getPrincipal()).isEqualTo(address2.getPrincipal());
		assertThat(bodyAddress2.getPerson()).isEqualTo(person1.getId());
	}

	@Test
	@DirtiesContext
	public void deleteAddressTest() {
		address1 = addressRepository.save(address1);
		address2 = addressRepository.save(address2);
		List<Address> allAddresses = addressRepository.findAll();

		assertThat(allAddresses).hasSize(2);

		restTemplate.delete("/enderecos/" + address1.getId());

		allAddresses = addressRepository.findAll();
		assertThat(allAddresses).hasSize(1);
		assertThat(allAddresses.get(0).getId()).isEqualTo(address2.getId());

	}

}
