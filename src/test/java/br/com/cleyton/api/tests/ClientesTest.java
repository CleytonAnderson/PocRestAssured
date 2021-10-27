package br.com.cleyton.api.tests;

import org.junit.Test;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;

public class ClientesTest {
	
	public ClientesTest() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8888;
		RestAssured.basePath = "/api";
		RestAssured.authentication = basic("cleyton", "teste");
	}
	
	@Test
	public void buscarClientesIniciadosPorPEntaoRetornaraCodigo200() {
		given()
			.header("x-meu-token", "1c0bda575202fb8d7ff108fca4c0eccc")
			.param("clientenome", "Mauro")
		.when()
			.get("/clientes")
		.then()
			.statusCode(200)
			.body("code", equalTo(200))
			.body("message", containsString("1 cliente"))
			.body("data[0].clientetwitter", hasItems("@mauro", "@belle"))
			.body(matchesJsonSchemaInClasspath("getClientesSchema.json"));
	}
	
	@Test
	public void removerOCliente1EntaoRetornaraCodigo204() {
		given()
			.header("x-meu-token", "1c0bda575202fb8d7ff108fca4c0eccc")
		.when()
			.delete("/clientes/1")
		.then()
			.statusCode(204);
	}
	
	@Test
	public void inserirUmClienteEEntaoRetornaraCodigo201EAvaliarCorpo() {
		given()
			.header("x-meu-token", "1c0bda575202fb8d7ff108fca4c0eccc")
			.body("{"
					+ "\"clientenome\": \"Cleyton\", "
					+ "\"clienteemail\": \"iam@cleyton.com.br\", "
					+ "\"clientetwitter\": [\"@Cleyton\", \"@BehindThePostCh\"]"
			+ "}")
		.when()
			.post("/clientes")
		.then()
			.statusCode(201)
			.body("code", equalTo(201))
			.body("message", equalTo("cliente inserido com sucesso"))
			.body("data.clienteid", instanceOf(Integer.class))
			.body("data.clientenome", equalTo("Cleyton"))
			.body("data.clienteemail", equalTo("iam@cleyton.com.br"))
			.body("data.clientetwitter", hasItems("@Cleyton", "@BehindThePostCh"));
	}
	
	@Test
	public void alterarUmClienteEEntaoRetornaraCodigo200EAvaliarCorpo() {
		given()
			.header("x-meu-token", "1c0bda575202fb8d7ff108fca4c0eccc")
			.body("{"
				+ "\"clientenome\": \"Cleyton Teste\", "
				+ "\"clienteemail\": \"iam@cleytonteste.com.br\", "
				+ "\"clientetwitter\": [\"@cleyton\", \"@BehindThePostCh\"]"
			+ "}")
		.when()
			.put("/clientes/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("code", equalTo(200))
			.body("message", equalTo("cliente alterado com sucesso"))
			.body("data.clienteid", equalTo(1))
			.body("data.clientenome", equalTo("Cleyton"))
			.body("data.clienteemail", equalTo("iam@cleyton.com.br"))
			.body("data.clientetwitter", hasItems("@Cleyton", "@BehindThePostCh"));
	}
}
