package com.person.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.crud.model.Address;
import com.person.crud.model.Person;
import com.person.crud.service.PersonService;

@RestController
@RequestMapping("/pessoas")
public class PersonController {
	@Autowired
	private PersonService personService;

	@PostMapping
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		Person createdPerson = personService.createPerson(person);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
		personService.deletePerson(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
		Person updatedPerson = personService.updatePerson(id, person);
		return ResponseEntity.ok(updatedPerson);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Person> getOnePerson(@PathVariable Long id) {
		Person person = personService.getOnePerson(id);
		return ResponseEntity.ok(person);
	}

	@GetMapping
	public ResponseEntity<List<Person>> getPeople() {
		List<Person> people = personService.getPeople();
		return ResponseEntity.ok(people);
	}

	@GetMapping("/{id}/enderecos")
	public ResponseEntity<List<Address>> getPersonAddresses(@PathVariable Long id) {
		List<Address> addresses = personService.getPersonAddresses(id);
		return ResponseEntity.ok(addresses);
	}
}
