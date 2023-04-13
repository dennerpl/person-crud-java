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

import com.person.crud.dto.AddressDto;
import com.person.crud.model.Address;
import com.person.crud.service.AddressService;

@RestController
@RequestMapping("/enderecos")
public class AddressController {
	@Autowired
	private AddressService addressService;

	@GetMapping
	public ResponseEntity<List<Address>> getAddresses() {
		List<Address> addresses = addressService.getAddressList();
		return ResponseEntity.ok(addresses);
	}

	@GetMapping("{id}")
	public ResponseEntity<Address> getOneAddress(@PathVariable Long id) {
		Address address = addressService.findOneAddress(id);
		return ResponseEntity.ok(address);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
		addressService.deleteAddress(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping
	public ResponseEntity<Address> createAddress(@RequestBody AddressDto addressDto) {
		Address address = addressService.createAddress(addressDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(address);
	}

	@PutMapping("{id}")
	public ResponseEntity<Address> editAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
		Address address = addressService.editAddress(addressDto, id);
		return ResponseEntity.ok(address);
	}
}
