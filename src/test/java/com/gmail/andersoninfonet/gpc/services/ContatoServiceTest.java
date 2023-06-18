package com.gmail.andersoninfonet.gpc.services;

import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import com.gmail.andersoninfonet.gpc.models.exceptions.GpcNotFoundException;
import com.gmail.andersoninfonet.gpc.models.requests.AtualizarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarPessoaRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContatoServiceTest {

    @Autowired
    private ContatoService contatoService;
    @Autowired
    private PessoaService  pessoaService;
    private Long contatoIdTest;
    private Long pessoaIdTest;

    @BeforeAll
    public void setUp()  {
        var pessoaRequest = new SalvarPessoaRequest("Anderson Dias", "10779671015", LocalDate.of(1982, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");
        this.pessoaIdTest = this.pessoaService.salvarPessoa(pessoaRequest).id();
        var page = this.contatoService.consultarContatos(this.pessoaIdTest, PageRequest.of(0, 10));
        this.contatoIdTest = page.getContent().get(0).id();
    }

    @Test
    @Order(1)
    void deveSalvarUmContato() {
        var contatoRequest = new SalvarContatoRequest("Luciana", "21555556666", "mae@gmail.com", "luciana@gmail.com");
        var contato = contatoRequest.toNewEntity();
        contato.setPessoaId(this.pessoaIdTest);
        var contatoSalvo = this.contatoService.salvarContato(contato);
        Assertions.assertNotNull(contatoSalvo);
        Assertions.assertEquals("Luciana", contatoSalvo.nome());
    }

    @Test
    @Order(2)
    void deveBuscarUmContatoPorId() {
        var contatoSalvo = this.contatoService.buscarContatoPorId(this.contatoIdTest);
        Assertions.assertNotNull(contatoSalvo);
        Assertions.assertEquals(this.contatoIdTest, contatoSalvo.getId());
    }

    @Test
    @Order(3)
    void deveLancarExcecaoQuandoTentarBuscarUmContatoPorId() {
        Assertions.assertThrows(GpcNotFoundException.class, () -> this.contatoService.buscarContatoPorId(0L));
    }

    @Test
    @Order(4)
    void deveLancarExcecaoQuandoTentarInativarUmContato() {
        Assertions.assertThrows(GpcNotFoundException.class, () -> this.contatoService.inativarContato(0L, "usuario"));
    }

    @Test
    @Order(5)
    void deveAtualizarUmContato() {
        var contatoRequest = new AtualizarContatoRequest(this.contatoIdTest, "blabla","21999998888", "blabla@gmail.com", "luciana@gmail.com");
        var contatoSalvo = this.contatoService.atualizarContato(contatoRequest);
        Assertions.assertNotNull(contatoSalvo);
        Assertions.assertEquals("21999998888", contatoSalvo.telefone());
    }

    @Test
    @Order(6)
    void deveLancarExcecaoQuandoTentarAtualizarUmContato() {
        var contatoRequest = new AtualizarContatoRequest(0L, "blabla","21999998888", "blabla@gmail.com", "luciana@gmail.com");
        Assertions.assertThrows(GpcNotFoundException.class, () -> this.contatoService.atualizarContato(contatoRequest));
    }

    @Test
    @Order(7)
    void deveInativarUmContato() {
        this.contatoService.inativarContato(this.contatoIdTest, "usuario@gmail.com");
        var contatoInativo = this.contatoService.buscarContatoPorId(this.contatoIdTest);
        Assertions.assertNotNull(contatoInativo);
        Assertions.assertEquals(EntityStatus.INATIVO, contatoInativo.getStatus());
    }

    @Test
    @Order(8)
    void deveConsultarContatosDeFormaPaginada() {
        var contatos = this.contatoService.consultarContatos(this.pessoaIdTest, PageRequest.of(0,10, Sort.unsorted()));
        Assertions.assertNotNull(contatos);
        Assertions.assertFalse(contatos.isEmpty());
    }
}
