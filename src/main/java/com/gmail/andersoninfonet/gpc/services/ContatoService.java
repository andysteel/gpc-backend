package com.gmail.andersoninfonet.gpc.services;

import com.gmail.andersoninfonet.gpc.models.entities.Contato;
import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import com.gmail.andersoninfonet.gpc.models.exceptions.GpcNotFoundException;
import com.gmail.andersoninfonet.gpc.models.requests.AtualizarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.responses.ContatoResponse;
import com.gmail.andersoninfonet.gpc.models.responses.SalvarContatoResponse;
import com.gmail.andersoninfonet.gpc.repositories.ContatoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ContatoService {

    private static final String CONTATO_NAO_ENCONTRADO = "Contato não encontrado na base de dados.";
    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    @Transactional
    public SalvarContatoResponse salvarContato(Contato contato) {
        return new SalvarContatoResponse(this.contatoRepository.save(contato));
    }

    public Page<ContatoResponse> consultarContatos(Long pessoaId, Pageable pageable) {
        return this.contatoRepository.findByPessoaId(pessoaId, pageable, EntityStatus.ATIVO).map(ContatoResponse::new);
    }

    public Contato buscarContatoPorId(Long id) {
        return this.contatoRepository.findById(id)
                .orElseThrow(() -> new GpcNotFoundException(CONTATO_NAO_ENCONTRADO));
    }

    @Transactional
    public void inativarContato(Long id, String usuario) {
        var contato = this.contatoRepository.findByIdAndStatus(id, EntityStatus.ATIVO);
        if(Objects.isNull(contato)) {
            throw new GpcNotFoundException(CONTATO_NAO_ENCONTRADO);
        }
        contato.setStatus(EntityStatus.INATIVO);
        contato.getAuditoria().setInativadoPor(usuario);
        contato.getAuditoria().setInativadoEm(LocalDateTime.now());
        this.contatoRepository.save(contato);
    }

    @Transactional
    public ContatoResponse atualizarContato(AtualizarContatoRequest atualizarContatoRequest) {
        var contato = this.contatoRepository.findByIdAndStatus(atualizarContatoRequest.id(), EntityStatus.ATIVO);
        if(Objects.isNull(contato)) {
            throw new GpcNotFoundException(CONTATO_NAO_ENCONTRADO);
        }

        contato.setEmail(atualizarContatoRequest.email());
        contato.setTelefone(atualizarContatoRequest.telefone());
        contato.setNome(atualizarContatoRequest.nome());

        contato.getAuditoria().setAtualizadoEm(LocalDateTime.now());
        contato.getAuditoria().setAtualizadoPor(atualizarContatoRequest.usuario());
        return new ContatoResponse(this.contatoRepository.save(contato));
    }
}
