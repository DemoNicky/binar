package com.binarfood.binarfoodapp.Service;

import com.binarfood.binarfoodapp.DTO.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ResponseHandling<ProductResponseDTO> createProduct(ProductRequestDTO productRequestDTO);

    ResponseHandling<List<ProductGetResponseDTO>> getData(Pageable pageable);

    ResponseHandling<ProductResponseDTO> updateData(String productCode, ProductUpdateRequestDTO requestDTO);

    ResponseHandling<List<ProductGetResponseDTO>> getDataByName(String name);

    ProductDeleteResponseDTO deleteData(String code);
}
