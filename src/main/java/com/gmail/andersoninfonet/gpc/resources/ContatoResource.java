package com.gmail.andersoninfonet.gpc.resources;

import com.gmail.andersoninfonet.gpc.models.requests.AtualizarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarContatoRequest;
import com.gmail.andersoninfonet.gpc.models.responses.ContatoResponse;
import com.gmail.andersoninfonet.gpc.models.responses.SalvarContatoResponse;
import com.gmail.andersoninfonet.gpc.services.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/contatos")
@Validated
@Tag(name = "Contatos", description = "Gerenciamento de contatos")
public class ContatoResource {

    private final ContatoService contatoService;

    public ContatoResource(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @GetMapping("/pessoa/{pessoaId}")
    @Operation(description = "Consulta paginada de todos os contatos de uma pessoa", method = "GET",
            parameters = {@Parameter(name = "pessoaId", schema = @Schema(implementation = Long.class)),
                            @Parameter(schema = @Schema(implementation = Pageable.class))})
    @ApiResponse(description = "Lista paginada de contatos", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class))})
    public ResponseEntity<Page<ContatoResponse>> consultarContatos(
            @PathVariable("pessoaId")
            @Min(value = 1, message = "Um identificador de Pessoa válido precisa ser informado")
            @NotNull(message = "Um identificador de Pessoa precisa ser informado")
            Long pessoaId, Pageable pageable) {
        return ResponseEntity.ok(this.contatoService.consultarContatos(pessoaId, pageable));
    }

    @PostMapping("/pessoa/{pessoaId}")
    @Operation(description = "Salvar um novo contato para uma pessoa", method = "POST",
            parameters = {@Parameter(name = "pessoaId", schema = @Schema(implementation = Long.class))},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalvarContatoRequest.class))}))
    @ApiResponse(description = "Contato salvo", responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalvarContatoResponse.class))})
    public ResponseEntity<SalvarContatoResponse> salvarContato(
            @PathVariable("pessoaId")
            @Min(value = 1, message = "Um identificador de Pessoa válido precisa ser informado")
            @NotNull(message = "Um identificador de Pessoa precisa ser informado")
            Long pessoaId,
            @RequestBody @Valid SalvarContatoRequest contatoRequest) {
        var contato = contatoRequest.toNewEntity();
        contato.setPessoaId(pessoaId);
        var contatoResponse = this.contatoService.salvarContato(contato);
        var resourceURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(contatoResponse.id())
                .toUriString();
        return ResponseEntity.created(URI.create(resourceURI)).body(contatoResponse);
    }

    @GetMapping("/{idContato}")
    @Operation(description = "Buscar Contato por id", method = "GET",
            parameters = {@Parameter(name = "idContato", schema = @Schema(implementation = Long.class))})
    @ApiResponse(description = "Contato", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ContatoResponse.class))})
    public ResponseEntity<ContatoResponse> buscarContatoPorId(
            @PathVariable("idContato")
            @Min(value = 1, message = "Um identificador de Contato válido precisa ser informado")
            @NotNull(message = "Um identificador de Contato precisa ser informado")
            Long idContato) {
        return ResponseEntity.ok(new ContatoResponse(this.contatoService.buscarContatoPorId(idContato)));
    }

    @PutMapping
    @Operation(description = "Atualizar um contato", method = "PUT",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AtualizarContatoRequest.class))}))
    @ApiResponse(description = "Contato atualizado", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ContatoResponse.class))})
    public ResponseEntity<ContatoResponse> atualizarContato(@RequestBody @Valid AtualizarContatoRequest atualizarContatoRequest) {
        return ResponseEntity.ok(this.contatoService.atualizarContato(atualizarContatoRequest));
    }

    @DeleteMapping("/{idContato}")
    @Operation(description = "Inativar um contato", method = "DELETE",
            parameters = {@Parameter(name = "idContato", schema = @Schema(implementation = Long.class)), @Parameter(name = "usuario", schema = @Schema(implementation = String.class))})
    @ApiResponse(description = "Contato inativado", responseCode = "204")
    public ResponseEntity<Void> inativarContato(
            @PathVariable("idContato")
            @Min(value = 1, message = "Um identificador de Contato válido precisa ser informado")
            @NotNull(message = "Um identificador de Contato precisa ser informado")
            Long idContato,
            @RequestParam("usuario")
            @Email(message = "Usuario inválido")
            @NotBlank(message = "O usuário que esta executando a operação precisa ser informado")
            String usuario
    ) {
        this.contatoService.inativarContato(idContato, usuario);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
