package com.in28minutes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WelcomeService{
	//clase business logic
	@Value("${welcome.message}")
	private String wellcomeMesagge;  

	//hola
	public String retrieveWelcome() {
//		return "good morning new!";
		return wellcomeMesagge;
	}
}
