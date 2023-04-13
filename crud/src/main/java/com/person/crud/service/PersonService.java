package com.person.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.person.crud.model.Address;
import com.person.crud.model.Person;
import com.person.crud.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	public Person createPerson(Person person) {
		return personRepository.save(person);
	}

	public Person updatePerson(Long id, Person person) {
		Optional<Person> optionalPerson = personRepository.findById(id);

		if (optionalPerson.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");

		Person personSaved = optionalPerson.get();
		person.setId(personSaved.getId());
		person.setAdresses(personSaved.getAdresses());
		return personRepository.save(person);
	}

	public Person getOnePerson(Long id) {
		Optional<Person> optionalPerson = personRepository.findById(id);

		if (optionalPerson.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");

		return optionalPerson.get();
	}

	public List<Person> getPeople() {
		return personRepository.findAll();
	}

	public List<Address> getPersonAddresses(Long id) {
		Optional<Person> optionalPerson = personRepository.findById(id);

		if (optionalPerson.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");

		return optionalPerson.get().getAdresses();
	}

	public void deletePerson(Long id) {
		personRepository.deleteById(id);
	}
}
