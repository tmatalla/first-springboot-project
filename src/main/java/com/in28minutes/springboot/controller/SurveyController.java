package com.in28minutes.springboot.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.springboot.model.Question;
import com.in28minutes.springboot.service.SurveyService;

@RestController
public class SurveyController {

	@Autowired
	private SurveyService surveyService;

	// GET
	@GetMapping("/surveys/{surveyId}/questions")
	public List<Question> retrieveQuestions(@PathVariable String surveyId) {
		String desdeDonde = "retrieveQuestions -";
		System.out.println(desdeDonde + 1);
		return surveyService.retrieveQuestions(surveyId);
	}

	// GET
	@GetMapping("/surveys/{surveyId}/questions/{questionId}")
	public Question retrieveDetailsForQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
		String desdeDonde = "retrieveDetailsForQuestion -";
		System.out.println(desdeDonde + 1);
		return surveyService.retrieveQuestion(surveyId, questionId);
	}

	// POST
	// en el body recibire un json con la new question
	// {"id":"Question99","description":"some
	// description","correctAnswer":"correctAnswer","options":["option1","correctAnswer","option3"]}
	@PostMapping("/surveys/{surveyId}/questions")
	public ResponseEntity<Void> addQuestionToSurvey(@PathVariable String surveyId, @RequestBody Question newQuestion) {
		String desdeDonde = "addQuestionToSurvey -";
		System.out.println(desdeDonde+1);
		Question createdTodo = surveyService.addQuestion(surveyId, newQuestion);
		if (createdTodo == null) {
			return ResponseEntity.noContent().build();
		}
		// ServletUriComponentsBuilder.fromCurrentRequest() da la url actual, 
		//a la que añadimos /{id} y lo reemplazamos con question.getId
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(createdTodo.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

}
