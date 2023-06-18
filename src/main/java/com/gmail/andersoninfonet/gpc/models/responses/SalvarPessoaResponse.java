package com.gmail.andersoninfonet.gpc.models.responses;

import com.gmail.andersoninfonet.gpc.models.entities.Pessoa;

public record SalvarPessoaResponse(Long id, String nome) {

    public SalvarPessoaResponse(Pessoa pessoa) {
        this(pessoa.getId(), pessoa.getNome());
    }

}
