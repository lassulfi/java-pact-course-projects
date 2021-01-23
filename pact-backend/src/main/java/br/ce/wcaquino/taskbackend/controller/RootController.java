package br.ce.wcaquino.taskbackend.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value ="/")
public class RootController {

	@SuppressWarnings({ "unused" })
	@GetMapping
	public String hello() {
		return "Hello World!";
	}

	@SuppressWarnings({ "unused", "uncheked", "rawtypes" })
	@PostMapping(value = "/barrigaPactStateChange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> stateChange(@RequestBody Map<String, Object> body) {
		final String TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTMwMjV9.RWDm2EDM3oYwupLMa8qou7esS-I31qQtlP0qOOllblA";

		Map<String, String> response = new HashMap<>();

		String state = (String) body.get("state");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", TOKEN);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		RestTemplate rest = new RestTemplate();
		rest.exchange("https://barrigarest.wcaquino.me/reset", HttpMethod.GET, entity, Object.class);

		response.put("reset", "ok");

		switch (state) {
			case "I have an accountId":
				ResponseEntity<List> restAccount = rest.exchange("https://barrigarest.wcaquino.me/contas",
						HttpMethod.GET, entity, List.class);
				Map<String, Object> firstAccount = (Map<String, Object>) restAccount.getBody().get(0);
				String accountId = String.valueOf(firstAccount.get("id"));
				response.put("accountId", accountId);
				break;
			case "I have a valid token":
				response.put("token", TOKEN);
				break;
			default:
				break;
		}

		System.out.println(response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
