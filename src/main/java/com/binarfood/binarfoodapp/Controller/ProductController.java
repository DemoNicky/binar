package com.binarfood.binarfoodapp.Controller;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ProductResponseDTO>> createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        ResponseHandling<ProductResponseDTO> productResponseDTO = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productResponseDTO);
    }

    @GetMapping(
            path = "/get/{page}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<ProductGetResponseDTO>>>getAllProduct(@PathVariable("page")int page){
        Pageable pageable = PageRequest.of(page, 10);
        ResponseHandling<List<ProductGetResponseDTO>> response = productService.getData(pageable);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<ProductGetResponseDTO>>>getDataByName(@PathVariable("name") String name){
        ResponseHandling<List<ProductGetResponseDTO>> response = productService.getDataByName(name);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(
            path = "/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ProductResponseDTO>>updateData(@PathVariable("code")String productCode,
                                                                          @RequestBody ProductUpdateRequestDTO requestDTO){
        ResponseHandling<ProductResponseDTO> response = productService.updateData(productCode, requestDTO);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(
            path = "/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductDeleteResponseDTO> deleteData(@PathVariable("code")String code){
        ProductDeleteResponseDTO response = productService.deleteData(code);
        if (response.getErrors() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
