package com.gmail.andersoninfonet.gpc.models.requests;

import com.gmail.andersoninfonet.gpc.models.entities.Auditoria;
import com.gmail.andersoninfonet.gpc.models.entities.Pessoa;
import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record SalvarPessoaRequest(
        @NotBlank(message = "O nome da pessoa precisa ser informado")
        String nome,
        @CPF(message = "Número do CPF inválido.")
        @NotBlank(message = "O CPF da pessoa precisa ser informado")
        String cpf,

        @PastOrPresent(message = "A data de nascimento tem que ser menor ou igual ao dia de hoje")
        @NotNull(message = "A data de nascimento precisa ser informada")
        LocalDate dataNascimento,

        @NotEmpty(message = "Ao menos um contato precisa ser informado")
        Set<@Valid SalvarContatoRequest> contatos,
        @Email(message = "Usuario inválido")
        @NotBlank(message = "O usuário que esta executando a operação precisa ser informado")
        String usuario) implements RequestToEntity<Pessoa> {

    @Override
    public Pessoa toNewEntity() {
        var pessoa = new Pessoa();
        var auditoria = new Auditoria();
        auditoria.setCriadoPor(this.usuario);
        auditoria.setCriadoEm(LocalDateTime.now());
        pessoa.setAuditoria(auditoria);
        pessoa.setStatus(EntityStatus.ATIVO);
        BeanUtils.copyProperties(this, pessoa);
        return pessoa;
    }
}
