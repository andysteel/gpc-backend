package com.gmail.andersoninfonet.gpc.services;

import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import com.gmail.andersoninfonet.gpc.models.exceptions.GpcNotFoundException;
import com.gmail.andersoninfonet.gpc.models.requests.AtualizarPessoaRequest;
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
class PessoaServiceTest {

    @Autowired
    private PessoaService  pessoaService;

    private Long idTest;

    @BeforeAll
    public void setup()  {
        var pessoaRequest = new SalvarPessoaRequest("Anderson Dias", "52091213055", LocalDate.of(1982, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");
        this.idTest = this.pessoaService.salvarPessoa(pessoaRequest).id();
    }

    @Test
    @Order(1)
    void deveSalvarUmaPessoa() {
        var pessoaRequest = new SalvarPessoaRequest("Nubia Dias", "65517196004", LocalDate.of(1984, Month.JUNE, 5), Set.of(new SalvarContatoRequest("Mãe", "21987651234", "mae@gmail.com", "luciana@gmail.com")),"luciana@gmail.com");
        var pessoaSalva = this.pessoaService.salvarPessoa(pessoaRequest);
        Assertions.assertNotNull(pessoaSalva);
        Assertions.assertEquals("Nubia Dias", pessoaSalva.nome());
    }

    @Test
    @Order(2)
    void deveBuscarUmaPessoaPorId() {
        var pessoaSalva = this.pessoaService.buscarPessoaPorId(this.idTest);
        Assertions.assertNotNull(pessoaSalva);
        Assertions.assertEquals(this.idTest, pessoaSalva.getId());
    }

    @Test
    @Order(3)
    void deveLancarExcecaoQuandoTentarBuscarUmaPessoaPorId() {
        Assertions.assertThrows(GpcNotFoundException.class, () -> this.pessoaService.buscarPessoaPorId(0L));
    }

    @Test
    @Order(4)
    void deveLancarExcecaoQuandoTentarInativarUmaPessoa() {
        Assertions.assertThrows(GpcNotFoundException.class, () -> this.pessoaService.inativarPessoa(0L, "usuario"));
    }

    @Test
    @Order(5)
    void deveAtualizarUmaPessoa() {
        var pessoaRequest = new AtualizarPessoaRequest(this.idTest, "blabla","17914387096", LocalDate.of(1984, Month.JUNE, 5), "luciana@gmail.com");
        var pessoaSalva = this.pessoaService.atualizarPessoa(pessoaRequest);
        Assertions.assertNotNull(pessoaSalva);
        Assertions.assertEquals("17914387096", pessoaSalva.cpf());
    }

    @Test
    @Order(6)
    void deveLancarExcecaoQuandoTentarAtualizarUmaPessoa() {
        var pessoaRequest = new AtualizarPessoaRequest(0L, "blabla","17914387096", LocalDate.of(1984, Month.JUNE, 5), "luciana@gmail.com");
        Assertions.assertThrows(GpcNotFoundException.class, () -> this.pessoaService.atualizarPessoa(pessoaRequest));
    }

    @Test
    @Order(7)
    void deveInativarUmaPessoa() {
        this.pessoaService.inativarPessoa(this.idTest, "usuario@gmail.com");
        var pessoaInativa = this.pessoaService.buscarPessoaPorId(this.idTest);
        Assertions.assertNotNull(pessoaInativa);
        Assertions.assertEquals(EntityStatus.INATIVO, pessoaInativa.getStatus());
    }

    @Test
    @Order(8)
    void deveConsultarPessoasDeFormaPaginada() {
       var pessoas = this.pessoaService.consultarPessoas(PageRequest.of(0,10, Sort.unsorted()));
       Assertions.assertNotNull(pessoas);
       Assertions.assertFalse(pessoas.isEmpty());
       Assertions.assertEquals(1, pessoas.getTotalElements());
    }

}
