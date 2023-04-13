package com.person.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.person.crud.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
