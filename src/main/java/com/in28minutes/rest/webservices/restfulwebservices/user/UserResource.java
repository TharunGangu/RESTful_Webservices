package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
 
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserResource {
	
	@Autowired
	private UserDaoService service;
	
	//GET
	//retrieveAllUsers
	@GetMapping(path = "/users")
	public List<User> retriveAllUsers( ){
		
		return service.findAll();
	}
	
	@GetMapping(path = "/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		User user = service.findOne(id);	
		if(user == null) 
			throw new UserNotFoundException("id-"+ id);
			
			//"all-users" , SERVER_PATH + "/users"
			//retriveAllUsers
			EntityModel<User> resource = EntityModel.of(user);
			
			WebMvcLinkBuilder linkTo = 
					linkTo(methodOn(this.getClass()).retriveAllUsers());
			
			resource.add(linkTo.withRel("all-users"));
			
			//HATEOAS
			
			return resource;
		}
		
		
	
	@DeleteMapping(path = "/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User user = service.deleteById(id);	
		if(user == null) {
			throw new UserNotFoundException("id-"+ id);
		}
		
	}
	
	//input -details of the user
	//output - CREATED & Return the created URI
	@PostMapping(path = "/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User saveUser = service.save(user);
		
		//CREATED
		// /user/{id}  saveUser.getId()
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}

}
