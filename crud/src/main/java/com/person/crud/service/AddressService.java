package com.person.crud.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.person.crud.dto.AddressDto;
import com.person.crud.model.Address;
import com.person.crud.model.Person;
import com.person.crud.repository.AddressRepository;
import com.person.crud.repository.PersonRepository;

@Service
public class AddressService {
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<Address> getAddressList() {
		return addressRepository.findAll();
	}

	public void deleteAddress(Long id) {
		addressRepository.deleteById(id);
	}

	public Address createAddress(AddressDto addressDto) {
		Optional<Person> optionalPerson = personRepository.findById(addressDto.getPerson());

		if (optionalPerson.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");

		addressDto.setPerson(null);
		Address address = modelMapper.map(addressDto, Address.class);
		Person person = optionalPerson.get();
		address.setPerson(optionalPerson.get());
		Address newAddress = addressRepository.save(address);
		setEnderecoPrincipal(person, newAddress);
		return addressRepository.save(newAddress);

	}

	public Address findOneAddress(Long id) {
		Optional<Address> optionalAddress = addressRepository.findById(id);

		if (optionalAddress.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");

		return optionalAddress.get();
	}

	public Address editAddress(AddressDto addressDto, Long id) {
		Optional<Address> optionalAddress = addressRepository.findById(id);
		Optional<Person> optionalPerson = personRepository.findById(addressDto.getPerson());

		if (optionalAddress.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
		if (optionalPerson.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");

		Address address = optionalAddress.get();
		Person person = optionalPerson.get();
		address.setCep(addressDto.getCep());
		address.setCity(addressDto.getCity());
		address.setNumber(addressDto.getNumber());
		address.setPerson(person);
		address.setPrincipal(addressDto.getPrincipal());
		address.setStreet(addressDto.getStreet());
		setEnderecoPrincipal(person, address);
		return addressRepository.save(address);
	}

	public void setPrincipalAddress(Long personId, Long addressId) {
		Optional<Person> optionalPerson = personRepository.findById(personId);
		Optional<Address> optionalAddress = addressRepository.findById(addressId);

		if (optionalAddress.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
		if (optionalPerson.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");

		Person person = optionalPerson.get();
		Address address = optionalAddress.get();

		if (!person.getAdresses().contains(address))
			throw new IllegalArgumentException("Address does not belong to the given person");

		person.getAdresses().forEach(e -> {
			if (e.getId().equals(addressId)) {
				e.setPrincipal(true);
			} else {
				e.setPrincipal(false);
			}
		});

		personRepository.save(person);

	}

	public void setEnderecoPrincipal(Person person, Address address) {
		if (Boolean.TRUE.equals(address.getPrincipal())) {
			person.getAdresses().forEach(e -> {
				if (e.getId().equals(address.getId())) {
					e.setPrincipal(true);
				} else {
					e.setPrincipal(false);
				}
			});

			personRepository.save(person);
		}
	}
}
