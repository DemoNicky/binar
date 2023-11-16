package com.binarfood.binarfoodapp.Controller;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<MerchantResponseDTO>> createMerchant(@RequestBody MerchantRequestDTO merchantRequestDTO){
        CompletableFuture<ResponseHandling<MerchantResponseDTO>> future = CompletableFuture.supplyAsync(() ->
                merchantService.createMerchant(merchantRequestDTO));

        return future.thenApplyAsync(responseDTO -> {
            if (responseDTO.getData() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        }).join();
    }

    @GetMapping(
            path = "/{page}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<MerchantResponseDTO>>> getActiveMerchant(@PathVariable("page")int page){
        Pageable pageable = PageRequest.of(page, 10);
        ResponseHandling<List<MerchantResponseDTO>> responseDTOS = merchantService.getData(pageable);
        if (responseDTOS.getData() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTOS);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
    }

    @PutMapping(path = "/{merchantCode}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<MerchantResponseDTO>> updateMerchant(@PathVariable("merchantCode")String code, @RequestBody MerchantUpdateRequest requestDTO){
        CompletableFuture<ResponseHandling<MerchantResponseDTO>> future = CompletableFuture.supplyAsync(() ->
                merchantService.updateMerchant(code, requestDTO));

        return future.thenApplyAsync(response -> {
            if (response.getData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }).join();
    }

    @DeleteMapping(path = "/{merchantCode}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MerchantDeleteResponseDTO>deleteData(@PathVariable("merchantCode")String code){
        MerchantDeleteResponseDTO response = merchantService.deleteData(code);
        if (response.getErrors() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
