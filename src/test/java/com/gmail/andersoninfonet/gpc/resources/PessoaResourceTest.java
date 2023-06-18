package com.gmail.andersoninfonet.gpc.resources;

import com.gmail.andersoninfonet.gpc.models.requests.AtualizarPessoaRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarPessoaRequest;
import com.gmail.andersoninfonet.gpc.services.PessoaService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PessoaResourceTest {

    @Autowired
    private RequestSpecification requestSpecification;
    @Autowired
    private PessoaService pessoaService;
    @LocalServerPort
    private int port;
    private Long idTest;

    @BeforeAll
    public void setPort() {
        RestAssured.port = this.port;
        var pessoaRequest = new SalvarPessoaRequest("Anderson Dias", "52091213055", LocalDate.of(1982, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");
        this.idTest = this.pessoaService.salvarPessoa(pessoaRequest).id();
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {

        this.requestSpecification = new RequestSpecBuilder()
                                    .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation))
                                    .build();

    }


    @Test
    @Order(1)
    void deveSalvarUmaPessoa() {
        var pessoaRequest = new SalvarPessoaRequest("Nubia Dias", "65517196004", LocalDate.of(1984, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");

        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .headers("Origin", "http://localhost:4200")
                .body(pessoaRequest)
                .when()
                .post("/api/v1/pessoas")
                .then()
                .assertThat().statusCode(201);

    }

    @Test
    @Order(2)
    void deveBuscarPessoaPorId() {
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .headers("Origin", "http://localhost:4200")
                .when()
                .get("/api/v1/pessoas/"+this.idTest)
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    @Order(3)
    void deveConsultarPessoas() {
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .param("page", 0)
                .param("size", 10)
                .headers("Origin", "http://localhost:4200")
                .when()
                .get("/api/v1/pessoas")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    @Order(4)
    void deveAtualizarUmaPessoa() {
        var pessoaRequest = new AtualizarPessoaRequest(this.idTest, "blabla","17914387096", LocalDate.of(1984, Month.JUNE, 5), "luciana@gmail.com");
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pessoaRequest)
                .headers("Origin", "http://localhost:4200")
                .when()
                .put("/api/v1/pessoas")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    @Order(5)
    void deveLancarExcecaoQuantoTentarSalvarUmaPessoaSemContato() {
        var pessoaRequest = new SalvarPessoaRequest("Nubia Dias", "37135536029", LocalDate.of(1984, Month.JUNE, 5), new HashSet<>(),"luciana@gmail.com");

        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pessoaRequest)
                .headers("Origin", "http://localhost:4200")
                .when()
                .post("/api/v1/pessoas")
                .then()
                .assertThat().statusCode(400);

    }

    @Test
    @Order(6)
    void deveLancarExcecaoQuantoTentarSalvarUmaPessoaComCPFInvalido() {
        var pessoaRequest = new SalvarPessoaRequest("Nubia Dias", "37595598000", LocalDate.of(1984, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");

        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pessoaRequest)
                .headers("Origin", "http://localhost:4200")
                .when()
                .post("/api/v1/pessoas")
                .then()
                .assertThat().statusCode(400);

    }

    @Test
    @Order(6)
    void deveLancarExcecaoQuantoTentarSalvarUmaPessoaComDataDeNascimentoFutura() {
        var pessoaRequest = new SalvarPessoaRequest("Nubia Dias", "26391363056", LocalDate.of(2050, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");

        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pessoaRequest)
                .headers("Origin", "http://localhost:4200")
                .when()
                .post("/api/v1/pessoas")
                .then()
                .assertThat().statusCode(400);

    }

    @Test
    @Order(7)
    void deveLancarExcecaoQuantoTentarSalvarUmaPessoaComEmailInvalido() {
        var pessoaRequest = new SalvarPessoaRequest("Nubia Dias", "93611374091", LocalDate.of(1982, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@.com", "luciana@gmail.com")),"luciana@gmail.com");

        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pessoaRequest)
                .headers("Origin", "http://localhost:4200")
                .when()
                .post("/api/v1/pessoas")
                .then()
                .assertThat().statusCode(400);

    }

    @Test
    @Order(8)
    void deveInativarUmaPessoa() {
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .param("usuario", "usuario@gmail.com")
                .headers("Origin", "http://localhost:4200")
                .when()
                .delete("/api/v1/pessoas/"+this.idTest)
                .then()
                .assertThat().statusCode(204);
    }
}
