package com.in28minutes.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.in28minutes.service.configuration.BasicConfiguration;
import com.in28minutes.springboot.jpa.UserRepository;
import com.in28minutes.springboot.model.Question;
import com.in28minutes.springboot.service.SurveyService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SurveyController.class,	excludeAutoConfiguration = BasicConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
// la clase que estamos
																									// // testeando
//OLD CODE - @WebMvcTest(value = SurveyController.class, secure = false)
public class SurveyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// los autowired que habría que incluir en la clase que estamos testeando
	@MockBean
	private SurveyService surveyService;

	// este autowired NO hay que incluirlo en la clase que estamos testeando, pero
	// si no lo incluyo nos da error al crear el contexto
	@MockBean
	private UserRepository userRepository;

	@Test
	public void retrieveDetailsForQuestion() throws Exception {
		String desdeDonde = "retrieveDetailsForQuestion - ";
		System.out.println(desdeDonde + "Inicio");

		Question mockQuestion = new Question("Question1", "El Largest Country in the World", "Russia",
				Arrays.asList("India", "Russia", "United States", "China"));

		Mockito.when(surveyService.retrieveQuestion(Mockito.anyString(), Mockito.anyString())).thenReturn(mockQuestion);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/surveys/Survey1/questions/Question1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println(desdeDonde + "Llegamos hasta aquí 1");
		String expected = "{id:Question1,description:\"El Largest Country in the World\",correctAnswer:Russia}";

		String respuesta = result.getResponse().getContentAsString();
		System.out.println(desdeDonde + "respuesta:" + respuesta);
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

		System.out.println(desdeDonde + "Fin");
		// Assert
	}

	@Test
	public void retrieveSurveyQuestions() throws Exception {
		String desdeDonde = "retrieveSurveyQuestions - ";
		System.out.println(desdeDonde + "Inicio");

		List<Question> mockList = Arrays.asList(
				new Question("Question1", "First Alphabet", "A", Arrays.asList("A", "B", "C", "D")),
				new Question("Question2", "Last Alphabet", "Z", Arrays.asList("A", "X", "Y", "Z")));

		Mockito.when(surveyService.retrieveQuestions(Mockito.anyString())).thenReturn(mockList);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/surveys/Survey1/questions").accept(MediaType.APPLICATION_JSON))
				// .andExpect(status().isOk())
				.andReturn();
		System.out.println(desdeDonde + "hasta aquí todo bien 2");
		String expected = "[" + "{id:Question1,description:\"First Alphabet\",correctAnswer:A,options:[A,B,C,D]},"
				+ "{id:Question2,description:\"Last Alphabet\",correctAnswer:Z,options:[A,X,Y,Z]}" + "]";

		String respuesta = result.getResponse().getContentAsString();
		System.out.println(desdeDonde + "respuesta:" + respuesta);
		JSONAssert.assertEquals(expected, respuesta, false);

		System.out.println(desdeDonde + "Fin");
	}

	@Test
	public void createSurveyQuestion() throws Exception {
		Question mockQuestion = new Question("1", "Smallest Number", "1", Arrays.asList("1", "2", "3", "4"));

		String questionJson = "{\"description\":\"Smallest Number\",\"correctAnswer\":\"1\",\"options\":[\"1\",\"2\",\"3\",\"4\"]}";
		// surveyService.addQuestion to respond back with mockQuestion
		Mockito.when(surveyService.addQuestion(Mockito.anyString(), Mockito.any(Question.class)))
				.thenReturn(mockQuestion);

		// Send question as body to /surveys/Survey1/questions
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/surveys/Survey1/questions")
				.accept(MediaType.APPLICATION_JSON).content(questionJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());

		assertEquals("http://localhost/surveys/Survey1/questions/1", response.getHeader(HttpHeaders.LOCATION));

	}
}