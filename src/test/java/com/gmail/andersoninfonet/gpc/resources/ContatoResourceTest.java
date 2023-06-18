package com.gmail.andersoninfonet.gpc.resources;

import com.gmail.andersoninfonet.gpc.models.requests.AtualizarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarPessoaRequest;
import com.gmail.andersoninfonet.gpc.services.ContatoService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContatoResourceTest {

    @Autowired
    private RequestSpecification requestSpecification;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private ContatoService contatoService;
    @LocalServerPort
    private int port;
    private Long contatoIdTest;
    private Long pessoaIdTest;

    @BeforeAll
    public void setPort()  {
        RestAssured.port = this.port;
        var pessoaRequest = new SalvarPessoaRequest("Anderson Dias", "98358747004", LocalDate.of(1982, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");
        this.pessoaIdTest = this.pessoaService.salvarPessoa(pessoaRequest).id();
        var page = this.contatoService.consultarContatos(this.pessoaIdTest, PageRequest.of(0, 10));
        this.contatoIdTest = page.getContent().get(0).id();
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {

        this.requestSpecification = new RequestSpecBuilder()
                .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation))
                .build();

    }

    @Test
    @Order(1)
    void deveSalvarUmContato() {
        var contatoRequest = new SalvarContatoRequest("Luciana", "21987651234", "mae@gmail.com", "luciana@gmail.com");

        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .headers("Origin", "http://localhost:4200")
                .body(contatoRequest)
                .when()
                .post("/api/v1/contatos/pessoa/"+this.pessoaIdTest)
                .then()
                .assertThat().statusCode(201);

    }

    @Test
    @Order(2)
    void deveBuscarContatoPorId() {
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .headers("Origin", "http://localhost:4200")
                .when()
                .get("/api/v1/contatos/"+this.contatoIdTest)
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    @Order(3)
    void deveConsultarContatosDeUmaPessoa() {
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
                .get("/api/v1/contatos/pessoa/"+this.pessoaIdTest)
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    @Order(4)
    void deveAtualizarUmContato() {
        var contatoRequest = new AtualizarContatoRequest(this.contatoIdTest, "blabla","21999998888", "blabla@gmail.com", "luciana@gmail.com");
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(contatoRequest)
                .headers("Origin", "http://localhost:4200")
                .when()
                .put("/api/v1/contatos")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    @Order(5)
    void deveLancarExecaoAoTentarInativarUmContato() {
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .headers("Origin", "http://localhost:4200")
                .when()
                .delete("/api/v1/contatos/"+this.contatoIdTest)
                .then()
                .assertThat().statusCode(400);
    }

    @Test
    @Order(6)
    void deveInativarUmContato() {
        RestAssured.given(this.requestSpecification)
                .log().all()
                .filter(RestAssuredRestDocumentation.document("{methodName}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .param("usuario", "anderson@gmail.com")
                .headers("Origin", "http://localhost:4200")
                .when()
                .delete("/api/v1/contatos/"+this.contatoIdTest)
                .then()
                .assertThat().statusCode(204);
    }
}
