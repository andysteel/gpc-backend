package com.gmail.andersoninfonet.gpc.services;

import com.gmail.andersoninfonet.gpc.models.entities.Pessoa;
import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import com.gmail.andersoninfonet.gpc.models.exceptions.GpcNotFoundException;
import com.gmail.andersoninfonet.gpc.models.requests.AtualizarPessoaRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarPessoaRequest;
import com.gmail.andersoninfonet.gpc.models.responses.PessoaResponse;
import com.gmail.andersoninfonet.gpc.models.responses.SalvarPessoaResponse;
import com.gmail.andersoninfonet.gpc.repositories.PessoaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PessoaService {

    private static final String PESSOA_NAO_ENCONTRADA = "Pessoa não encontrada na base de dados.";
    private final PessoaRepository repository;
    private final ContatoService contatoService;

    public PessoaService(PessoaRepository repository, ContatoService contatoService) {
        this.repository = repository;
        this.contatoService = contatoService;
    }

    public Page<PessoaResponse> consultarPessoas(Pageable pageable) {
        return this.repository.findAllByStatus(pageable, EntityStatus.ATIVO).map(PessoaResponse::new);
    }

    @Transactional
    public SalvarPessoaResponse salvarPessoa(SalvarPessoaRequest pessoaRequest) {
        var pessoa = pessoaRequest.toNewEntity();
        pessoa.setCpf(pessoa.getCpf().replaceAll("\\D", ""));
        var pessoaSalva = this.repository.save(pessoa);
        pessoaRequest.contatos().forEach(contatoRequest -> {
            var contato = contatoRequest.toNewEntity();
            contato.setPessoaId(pessoaSalva.getId());
            this.contatoService.salvarContato(contato);
        });
        return new SalvarPessoaResponse(pessoaSalva);
    }

    public Pessoa buscarPessoaPorId(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new GpcNotFoundException(PESSOA_NAO_ENCONTRADA));
    }

    @Transactional
    public void inativarPessoa(Long id, String usuario) {
        var pessoa = this.repository.findByIdAndStatus(id, EntityStatus.ATIVO);
        if(Objects.isNull(pessoa)) {
            throw new GpcNotFoundException(PESSOA_NAO_ENCONTRADA);
        }
        pessoa.setStatus(EntityStatus.INATIVO);
        pessoa.getAuditoria().setInativadoPor(usuario);
        pessoa.getAuditoria().setInativadoEm(LocalDateTime.now());
        this.repository.save(pessoa);
    }

    @Transactional
    public PessoaResponse atualizarPessoa(AtualizarPessoaRequest atualizarPessoaRequest) {
        var pessoa = this.repository.findByIdAndStatus(atualizarPessoaRequest.id(), EntityStatus.ATIVO);
        if(Objects.isNull(pessoa)) {
            throw new GpcNotFoundException(PESSOA_NAO_ENCONTRADA);
        }

        pessoa.setCpf(atualizarPessoaRequest.cpf());
        pessoa.setNome(atualizarPessoaRequest.nome());
        pessoa.setDataNascimento(atualizarPessoaRequest.dataNascimento());
        pessoa.getAuditoria().setAtualizadoEm(LocalDateTime.now());
        pessoa.getAuditoria().setAtualizadoPor(atualizarPessoaRequest.usuario());
        return new PessoaResponse(this.repository.save(pessoa));
    }
}
