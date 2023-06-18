package com.gmail.andersoninfonet.gpc.models.responses;

import com.gmail.andersoninfonet.gpc.models.entities.Pessoa;

import java.time.LocalDate;

public record PessoaResponse(Long id, String nome, String cpf, LocalDate dataNascimento) {

    public PessoaResponse(Pessoa pessoa) {
        this(pessoa.getId(), pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento());
    }
}
