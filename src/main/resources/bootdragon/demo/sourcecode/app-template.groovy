/*

 ***************************************************************************************
 This Spring Boot application was generated for ${user}
 from ${source}
 ***************************************************************************************

 Spring CLI Installation instructions are available from
 http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#getting-started-installing-the-cli

 To run the application save this file as `app.groovy` then type `spring run app.groovy`
 once the app starts, open a web browser to http://localhost:8080/

Lean more about Spring Boot at http://spring.io/projects/spring-boot

*/

@RestController
class Example {
	@ResponseBody
	public String helloWorld() {
		${quotedmessage}
	}
}

