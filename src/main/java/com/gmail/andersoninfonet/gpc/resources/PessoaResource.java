package com.gmail.andersoninfonet.gpc.resources;

import com.gmail.andersoninfonet.gpc.models.requests.AtualizarPessoaRequest;
import com.gmail.andersoninfonet.gpc.models.requests.SalvarPessoaRequest;
import com.gmail.andersoninfonet.gpc.models.responses.PessoaResponse;
import com.gmail.andersoninfonet.gpc.models.responses.SalvarPessoaResponse;
import com.gmail.andersoninfonet.gpc.services.PessoaService;
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
@RequestMapping("/v1/pessoas")
@Validated
@Tag(name = "Pessoas", description = "Gerenciamento de pessoas")
public class PessoaResource {

    private final PessoaService pessoaService;

    public PessoaResource(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    @Operation(description = "Consulta paginada de pessoas", method = "GET",
            parameters = {@Parameter(schema = @Schema(implementation = Pageable.class))})
    @ApiResponse(description = "Lista paginada de pessoas", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class))})
    public ResponseEntity<Page<PessoaResponse>> consultarPessoas(Pageable pageable) {
        return ResponseEntity.ok(this.pessoaService.consultarPessoas(pageable));
    }

    @GetMapping("/{idPessoa}")
    @Operation(description = "Buscar Pessoa por id", method = "GET",
            parameters = {@Parameter(name = "idPessoa", schema = @Schema(implementation = Long.class))})
    @ApiResponse(description = "Pessoa", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PessoaResponse.class))})
    public ResponseEntity<PessoaResponse> buscarPessoaPorId(
            @PathVariable("idPessoa")
            @Min(value = 1, message = "Um identificador de Pessoa válido precisa ser informado")
            @NotNull(message = "Um identificador de Pessoa precisa ser informado")
            Long idPessoa) {
        return ResponseEntity.ok(new PessoaResponse(this.pessoaService.buscarPessoaPorId(idPessoa)));
    }

    @PostMapping
    @Operation(description = "Salvar uma nova pessoa", method = "POST",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalvarPessoaRequest.class))}))
    @ApiResponse(description = "Pessoa salva", responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalvarPessoaResponse.class))})
    public ResponseEntity<SalvarPessoaResponse> salvarPessoa(@RequestBody @Valid SalvarPessoaRequest pessoaRequest) {
        var pessoaResponse = this.pessoaService.salvarPessoa(pessoaRequest);
        var resourceURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pessoaResponse.id())
                .toUriString();
        return ResponseEntity.created(URI.create(resourceURI)).body(pessoaResponse);
    }

    @PutMapping
    @Operation(description = "Atualizar uma pessoa", method = "PUT",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AtualizarPessoaRequest.class))}))
    @ApiResponse(description = "Pessoa atualizada", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PessoaResponse.class))})
    public ResponseEntity<PessoaResponse> atualizarPessoa(@RequestBody @Valid AtualizarPessoaRequest atualizarPessoaRequest) {
        return ResponseEntity.ok(this.pessoaService.atualizarPessoa(atualizarPessoaRequest));
    }

    @DeleteMapping("/{idPessoa}")
    @Operation(description = "Inativar uma pessoa", method = "DELETE",
            parameters = {@Parameter(name = "idPessoa", schema = @Schema(implementation = Long.class)), @Parameter(name = "usuario", schema = @Schema(implementation = String.class))})
    @ApiResponse(description = "Pessoa inativada", responseCode = "204")
    public ResponseEntity<Void> inativarPessoa(
            @PathVariable("idPessoa")
            @Min(value = 1, message = "Um identificador de Pessoa válido precisa ser informado")
            @NotNull(message = "Um identificador de Pessoa precisa ser informado")
            Long idPessoa,
            @RequestParam("usuario")
            @Email(message = "Usuario inválido")
            @NotBlank(message = "O usuário que esta executando a operação precisa ser informado")
            String usuario
    ) {
        this.pessoaService.inativarPessoa(idPessoa, usuario);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
