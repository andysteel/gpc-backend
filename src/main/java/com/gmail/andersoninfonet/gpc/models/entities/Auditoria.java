package com.gmail.andersoninfonet.gpc.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class Auditoria implements Serializable {

    @Column(name = "criado_por", nullable = false)
    private String criadoPor;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_por")
    private String atualizadoPor;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "inativado_por")
    private String inativadoPor;

    @Column(name = "inativado_em")
    private LocalDateTime inativadoEm;

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(String atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public String getInativadoPor() {
        return inativadoPor;
    }

    public void setInativadoPor(String inativadoPor) {
        this.inativadoPor = inativadoPor;
    }

    public LocalDateTime getInativadoEm() {
        return inativadoEm;
    }

    public void setInativadoEm(LocalDateTime inativadoEm) {
        this.inativadoEm = inativadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auditoria auditoria = (Auditoria) o;
        return Objects.equals(criadoPor, auditoria.criadoPor) && Objects.equals(criadoEm, auditoria.criadoEm) && Objects.equals(atualizadoPor, auditoria.atualizadoPor) && Objects.equals(atualizadoEm, auditoria.atualizadoEm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(criadoPor, criadoEm, atualizadoPor, atualizadoEm);
    }

    @Override
    public String toString() {
        return "Auditoria{" +
                "criadoPor='" + criadoPor + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoPor='" + atualizadoPor + '\'' +
                ", atualizadoEm=" + atualizadoEm +
                ", inativadoPor='" + inativadoPor + '\'' +
                ", inativadoEm=" + inativadoEm +
                '}';
    }
}
