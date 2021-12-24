package com.ketul.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

	@GetMapping("/")
	public String welcome() {
		return "<h1>Hello All</h1>";
	}
	
	@GetMapping("/user")
	public String welcomeUser() {
		return "<h1>Hello User</h1>";
	}
	
	@GetMapping("/admin")
	public String welcomeAdmin() {
		return "<h1>Hello Admin</h1>";
	}
}
