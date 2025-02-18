package com.example.mimir.controllers;

import com.example.mimir.dto.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.SessionException;
import org.hibernate.annotations.Filter;
import org.springframework.web.bind.annotation.*;

@RestController()
public class TestController {
    @GetMapping("/api/test")
    @Operation(summary = "Get Test String", description = "Retorna uma string simples de teste.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requisição bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public String test() {
        return "test!!!!!!!";
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new entity", description = "Recebe 4 parâmetros no corpo da requisição e retorna uma mensagem de sucesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidade criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public String createEntity(Task request) {
        return "Entidade criada com os parâmetros: ";
    }
}
