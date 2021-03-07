package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
 
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {
	
	@Autowired
	private UserDaoService service;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	//GET
	//retrieveAllUsers
	@GetMapping(path = "/jpa/users")
	public List<User> retriveAllUsers( ){
		
		return userRepository.findAll();
	}
	
	@GetMapping(path = "/jpa/users/{id}")
	public Optional<User> retrieveUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);	
		if(!user.isPresent()) 
			throw new UserNotFoundException("id-"+ id);
			
			//"all-users" , SERVER_PATH + "/users"
			//retriveAllUsers
			//EntityModel<User> resource = EntityModel.of(user.get());
			
//			Resource<User> resource = new Resource<User>(user.get());
//		
//			ControllerLinkBuilder linkTo = 
//					linkTo(methodOn(this.getClass()).retriveAllUsers());
//			
//			resource.add(linkTo.withRel("all-users"));
			
			//HATEOAS
			
			return user;
		}
		
		
	
	@DeleteMapping(path = "/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);	
		
		
	}
	
	//input -details of the user
	//output - CREATED & Return the created URI
	@PostMapping(path = "/jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User saveUser = userRepository.save(user);
		
		//CREATED
		// /user/{id}  saveUser.getId()
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
	//Retrieve post related to specific user
	@GetMapping(path = "/jpa/users/{id}/post")
	public List<Post> retriveAllUsers(@PathVariable int id ){
		
		Optional<User> userOption = userRepository.findById(id);	
		if(!userOption.isPresent()) 
			throw new UserNotFoundException("id-"+ id);
		
		return userOption.get().getPost();
	}

	//Create a post for specific user
	@PostMapping(path = "/jpa/users/{id}/post")
	public ResponseEntity<Object> createPost(@PathVariable int id , @RequestBody Post post){
		
		Optional<User> userOption = userRepository.findById(id);	
		if(!userOption.isPresent()) 
			throw new UserNotFoundException("id-"+ id);
		
		User user = userOption.get();
		post.setUser(user);
		
		postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
}
