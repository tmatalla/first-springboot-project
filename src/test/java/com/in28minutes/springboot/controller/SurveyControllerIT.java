package com.in28minutes.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.springboot.Application;
import com.in28minutes.springboot.model.Question;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyControllerIT {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@BeforeEach
	public void Before() {
		// para la autenticacion
		// headers.set("Authoritation", createHttpAuthenticationHeaderValue("user1",
		// "secret1"));
		headers.add("Authorization", createHttpAuthenticationHeaderValue("user1", "secret1"));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	@Test
	void testJsonAssert() throws JSONException {
		// para comparar JSONs ignora espacios, el orden en el json ...
		// JSONAssert.assertEquals(expected, actual, extrict);
		// devuelve true
		String actual = "{id :1,name: ranga}";
		JSONAssert.assertEquals("{id:1}", actual, false);

		// devuelven false:
		// JSONAssert.assertEquals("{id:1,name:ranga}", "{id:1}", false);
		// JSONAssert.assertEquals("{id:1,name:ranga}", "{id:2,name:ranga}", false);
	}

	@Test
	void testRetrieveSurveyQuestion() throws JSONException {
		String desdeDonde = "testRetrieveSurveyQuestion - ";
//		fail("Not yet implemented");
		System.out.println(desdeDonde + "port: " + port);

//		String output = restTemplate.getForObject(url, String.class);
//		System.out.println("output: " + output);

//		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		// entity: primer parametro: body, segundo: headers
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		// añadimos Accept application/json en nuestro request
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/surveys/Survey1/questions/Question1"), HttpMethod.GET, entity, String.class);
		System.out.println(desdeDonde + "output: " + response.getStatusCodeValue() + " - " + response.getBody());

		String expected = "{id:Question1,correctAnswer:Russia,description:\"Largest Country in the World\"}";

		// assertTrue(response.getBody().contains("id\":\"Question1\""));
		JSONAssert.assertEquals(expected, response.getBody(), false);

	}

	// NEEDS REFACTORING
	@Test
	public void retrieveAllSurveyQuestions() throws Exception {
		String desdeDonde = "retrieveAllSurveyQuestions - ";
//		fail("Not yet implemented");
		System.out.println(desdeDonde + "port: " + port);

		// entity: primer parametro: body, segundo: headers
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<List<Question>> response = restTemplate.exchange(createURLWithPort("/surveys/Survey1/questions"),
				HttpMethod.GET, entity,
				// new HttpEntity<String>("DUMMY_DOESNT_MATTER", headers),
				new ParameterizedTypeReference<List<Question>>() {
				});

		System.out.println(desdeDonde + "output: " + response.getStatusCodeValue() + " - " + response.getBody());
		Question sampleQuestion = new Question("Question1", "Largest Country in the World", "Russia",
				Arrays.	asList("India", "Russia", "United States", "China"));

		assertTrue(response.getBody().contains(sampleQuestion));
	}

	@Test
	public void createSurveyQuestion() throws Exception {
		String desdeDonde = "createSurveyQuestion - ";
//		fail("Not yet implemented");
		System.out.println(desdeDonde + "port: " + port);

		TestRestTemplate restTemplate = new TestRestTemplate();

		// Question question = new Question("id", "Smallest Number", "1",
		// Arrays.asList("1", "2", "3", "4"));
		Question question = new Question("Question1", "Largest Country in the World", "Russia",
				Arrays.asList("India", "Russia", "United States", "China"));

		// entity: primer parametro: body, segundo: headers
		HttpEntity entity = new HttpEntity<Question>(question, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/surveys/Survey1/questions"),
				HttpMethod.POST, entity, String.class);

		System.out.println(desdeDonde + "output: " + response.getStatusCodeValue() + " - " + response.getBody());
		assertTrue(response.getHeaders().get(HttpHeaders.LOCATION).get(0).contains("/surveys/Survey1/questions/"));
		//assertTrue(response.getStatusCodeValue() > 0);
	}

	private String createURLWithPort(final String uri) {
		return "http://localhost:" + port + uri;
	}

	private String createHttpAuthenticationHeaderValue(String userId, String password) {
		String auth = userId + ":" + password;

		byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));

		String headerValue = "Basic " + new String(encodedAuth);

		return headerValue;
	}
}
