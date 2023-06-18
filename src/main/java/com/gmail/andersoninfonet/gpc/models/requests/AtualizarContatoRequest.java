package com.gmail.andersoninfonet.gpc.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarContatoRequest(
        @Min(value = 1, message = "Um identificador de Contato válido precisa ser informado")
        @NotNull(message = "Um identificador de Contato precisa ser informado para a atualização")
        Long id,
        @NotBlank(message = "O nome do contato precisa ser informado")
        String nome,
        @NotBlank(message = "O telefone precisa ser informado")
        String telefone,

        @Email(message = "Email inválido")
        @NotBlank(message = "O email precisa ser informado")
        String email,
        @Email(message = "Usuario inválido")
        @NotBlank(message = "O usuário que esta executando a operação precisa ser informado")
        String usuario
) {
}
