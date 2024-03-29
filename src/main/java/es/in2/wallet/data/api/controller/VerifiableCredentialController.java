package es.in2.wallet.data.api.controller;

import es.in2.wallet.data.api.domain.CredentialRequestDTO;
import es.in2.wallet.data.api.domain.VCTypeListDTO;
import es.in2.wallet.data.api.domain.VcBasicDataDTO;
import es.in2.wallet.data.api.service.UserDataFacadeService;
import es.in2.wallet.data.api.util.ApplicationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/v1/credentials")
@Slf4j
@RequiredArgsConstructor
public class VerifiableCredentialController {

    private final UserDataFacadeService userDataFacadeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "List Verifiable Credentials",
            description = "Retrieve a list of Verifiable Credentials",
            tags = {"Verifiable Credential Management"}
    )
    @ApiResponse(responseCode = "200", description = "Verifiable credential retrieved successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request.")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    public Mono<List<VcBasicDataDTO>> getVerifiableCredentialList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.debug("VerifiableCredentialController.getVerifiableCredential()");
        return ApplicationUtils.getUserIdFromToken(authorizationHeader)
                .flatMap(userDataFacadeService::getUserVCs);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete Verifiable Credential",
            description = "Delete the verifiable credential from the context broker.",
            tags = {"Verifiable Credential Management"}
    )
    @ApiResponse(responseCode = "200", description = "Verifiable credential deleted successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request.")
    @ApiResponse(responseCode = "404", description = "Verifiable credential not found")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    public Mono<Void> deleteVerifiableCredential(@RequestParam String credentialId,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.debug("VerifiableCredentialController.deleteVerifiableCredential()");
        return ApplicationUtils.getUserIdFromToken(authorizationHeader)
                .flatMap(userId -> userDataFacadeService.deleteVerifiableCredentialById(credentialId,userId))
                .then();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Save Verifiable Credential",
            description = "Save a verifiable credential",
            tags = {"Verifiable Credential Management"}
    )
    @ApiResponse(responseCode = "201", description = "Verifiable credential saved successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request.")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    public Mono<Void> saveVerifiableCredential(@RequestBody CredentialRequestDTO credentialRequestDTO,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.debug("VerifiableCredentialController.saveVerifiableCredential");
        return ApplicationUtils.getUserIdFromToken(authorizationHeader)
                .flatMap(userId -> userDataFacadeService.saveVerifiableCredentialByUserId(userId, credentialRequestDTO.getCredential()))
                .then();
    }
    @PostMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "List Verifiable Credentials by type",
            description = "Retrieve a list of Verifiable Credentials that matches with the specified types",
            tags = {"Verifiable Credential Management"}
    )
    @ApiResponse(responseCode = "200", description = "Verifiable credential retrieved successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request.")
    @ApiResponse(responseCode = "404", description = "Verifiable credential don't match with the specified types")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    public Mono<List<VcBasicDataDTO>> getSelectableVCs(@RequestBody VCTypeListDTO vcTypeListDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.debug("VerifiableCredentialController.getSelectableVCs()");
        return ApplicationUtils.getUserIdFromToken(authorizationHeader)
                .flatMap(userId -> userDataFacadeService.getVCsByVcTypeList(userId, vcTypeListDTO.getVcTypes()));
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get Verifiable Credential by id",
            description = "Get the Verifiable Credential that matches with the specified id",
            tags = {"Verifiable Credential Management"}
    )
    @ApiResponse(responseCode = "200", description = "Verifiable credential retrieved successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request.")
    @ApiResponse(responseCode = "404", description = "Verifiable credential don't match with the specified id")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    public Mono<String> getSelectableVCs(@RequestParam String credentialId,@RequestParam String format, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.debug("VerifiableCredentialController.getSelectableVCs()");
        return ApplicationUtils.getUserIdFromToken(authorizationHeader)
                .flatMap(userId -> userDataFacadeService.getVerifiableCredentialByIdAndFormat(userId, credentialId,format));
    }

}