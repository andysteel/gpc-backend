package com.gmail.andersoninfonet.gpc.models.requests;

import com.gmail.andersoninfonet.gpc.models.entities.Auditoria;
import com.gmail.andersoninfonet.gpc.models.entities.Contato;
import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public record SalvarContatoRequest(
        @NotBlank(message = "O nome do contato precisa ser informado")
        String nome,
        @NotBlank(message = "O telefone precisa ser informado")
        String telefone,

        @Email(message = "Email inválido")
        @NotBlank(message = "O email precisa ser informado")
        String email,
        @Email(message = "Usuario inválido")
        @NotBlank(message = "O usuário que esta executando a operação precisa ser informado")
        String usuario) implements RequestToEntity<Contato> {

        @Override
        public Contato toNewEntity() {
                var contato = new Contato();
                var auditoria = new Auditoria();
                auditoria.setCriadoPor(this.usuario);
                auditoria.setCriadoEm(LocalDateTime.now());
                contato.setAuditoria(auditoria);
                contato.setStatus(EntityStatus.ATIVO);
                BeanUtils.copyProperties(this, contato);
                return contato;
        }
}
