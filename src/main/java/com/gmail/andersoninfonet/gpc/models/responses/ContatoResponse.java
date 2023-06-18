package com.gmail.andersoninfonet.gpc.models.responses;

import com.gmail.andersoninfonet.gpc.models.entities.Contato;

public record ContatoResponse(Long id, String nome, String email, String telefone) {

    public ContatoResponse(Contato contato) {
        this(contato.getId(), contato.getNome(), contato.getEmail(), contato.getTelefone());
    }
}
