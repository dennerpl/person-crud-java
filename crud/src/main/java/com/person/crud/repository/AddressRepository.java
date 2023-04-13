package com.person.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.person.crud.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
