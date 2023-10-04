package com.binarfood.binarapp.Service;

import com.binarfood.binarapp.DTO.CreateProductDTO;
import com.binarfood.binarapp.DTO.FindAllProductDTO;
import com.binarfood.binarapp.DTO.FindProdukDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Boolean createProduct(CreateProductDTO create);

    List<FindAllProductDTO> findAllProduct(Pageable pageable);

    List<FindProdukDTO> findProductWithName(String nama);

    String updateProduct(String kode, String nama, BigDecimal harga);

    Boolean deleteProduct(String kode);
}
