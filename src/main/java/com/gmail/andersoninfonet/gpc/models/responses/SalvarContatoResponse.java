package com.gmail.andersoninfonet.gpc.models.responses;

import com.gmail.andersoninfonet.gpc.models.entities.Contato;

public record SalvarContatoResponse(Long id, String nome) {

    public SalvarContatoResponse(Contato contato) {
        this(contato.getId(), contato.getNome());
    }

}
