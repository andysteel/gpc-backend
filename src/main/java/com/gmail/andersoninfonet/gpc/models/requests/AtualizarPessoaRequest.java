package com.gmail.andersoninfonet.gpc.models.requests;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record AtualizarPessoaRequest(

        @Min(value = 1, message = "Um identificador de Pessoa válido precisa ser informado")
        @NotNull(message = "Um identificador de Pessoa precisa ser informado para a atualização")
        Long id,
        @NotBlank(message = "O nome da pessoa precisa ser informado")
        String nome,
        @CPF(message = "Número do CPF inválido.")
        @NotBlank(message = "O CPF da pessoa precisa ser informado")
        String cpf,

        @Past(message = "A data de nascimento tem que ser menor que o dia de hoje")
        @NotNull(message = "A data de nascimento precisa ser informada")
        LocalDate dataNascimento,

        @Email(message = "Usuario inválido")
        @NotBlank(message = "O usuário que esta executando a operação precisa ser informado")
        String usuario) {
}
