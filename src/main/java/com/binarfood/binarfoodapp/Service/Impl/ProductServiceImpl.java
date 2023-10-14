package com.binarfood.binarfoodapp.Service.Impl;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Merchant;
import com.binarfood.binarfoodapp.Entity.Product;
import com.binarfood.binarfoodapp.Repository.MerchantRepository;
import com.binarfood.binarfoodapp.Repository.ProductRepository;
import com.binarfood.binarfoodapp.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public ResponseHandling<ProductResponseDTO> createProduct(ProductRequestDTO productRequestDTO) {
        Optional<Merchant> merchant = merchantRepository.findByMerchantCode(productRequestDTO.getMerchantCode());
        ResponseHandling<ProductResponseDTO> response = new ResponseHandling<>();

        if (merchant.isPresent()){
            String uuidCode = GetUUIDCode();
            Product product = new Product();
            product.setProductCode(uuidCode);
            product.setProductName(productRequestDTO.getProductName());
            product.setPrice(productRequestDTO.getPrice());
            product.setActive(true);
            product.setMerchant(merchant.get());
            productRepository.save(product);

            ProductResponseDTO responseDTO = new ProductResponseDTO();
            responseDTO.setProductCode(product.getProductCode());
            responseDTO.setProductName(product.getProductName());
            responseDTO.setPrice(product.getPrice());
            responseDTO.setKodeMerchant(product.getMerchant().getMerchantCode());

            response.setData(responseDTO);
            response.setMessage("success create new product");
            return response;
        }else {
            response.setErrors("Merchant kode Not found!!!");
            return response;
        }
    }

    @Override
    public ResponseHandling<List<ProductGetResponseDTO>> getData(Pageable pageable) {
        Page<Product> product = productRepository.findAll(pageable);
        ResponseHandling<List<ProductGetResponseDTO>> response = new ResponseHandling<>();
        if (product.isEmpty() || product == null){
            response.setMessage("Can't get Data");
            response.setErrors("All Product Data is empty");
            return response;
        }else {
            List<ProductGetResponseDTO> responseDTOS = product.stream().map((p)->{
                ProductGetResponseDTO res = new ProductGetResponseDTO();
                res.setProductCode(p.getProductCode());
                res.setProductName(p.getProductName());
                res.setPrice(p.getPrice());
                res.setMerchantCode(p.getMerchant().getMerchantCode());
                res.setMerchantName(p.getMerchant().getMerchantName());
                return res;
            }).collect(Collectors.toList());
            response.setData(responseDTOS);
            response.setMessage("Success get all data");
            return response;
        }
    }

    @Override
    public ResponseHandling<ProductResponseDTO> updateData(String productCode,
                                                           ProductUpdateRequestDTO requestDTO) {
        Optional<Product> product = productRepository.findByProductCode(productCode);
        ResponseHandling<ProductResponseDTO> response = new ResponseHandling<>();
        if (!product.isPresent()){
            response.setMessage("Fail to update Product");
            response.setErrors("Product with code " +productCode+ " not found");
            return response;
        }
        Product product1 = product.get();
        product1.setProductName(requestDTO.getProductName());
        product1.setPrice(requestDTO.getPrice());
        product1.setActive(requestDTO.isActive());
        productRepository.save(product1);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductCode(product1.getProductCode());
        productResponseDTO.setProductName(product1.getProductName());
        productResponseDTO.setPrice(product1.getPrice());
        productResponseDTO.setKodeMerchant(product1.getMerchant().getMerchantCode());
        response.setData(productResponseDTO);
        response.setMessage("Success to update Product");
        return response;
    }

    @Override
    public ResponseHandling<List<ProductGetResponseDTO>> getDataByName(String name) {
        List<Optional<Product>> response = productRepository.findProductsByProductNameContaining(name);
        ResponseHandling<List<ProductGetResponseDTO>> responseHandling = new ResponseHandling<>();
        if (response.isEmpty()){
            responseHandling.setMessage("failed to get product");
            responseHandling.setErrors("product with name "+name+ " not found");
            return responseHandling;
        }
        List<Product> products = response.stream()
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        List<ProductGetResponseDTO> productGetResponseDTOS = products.stream().map((p)->{
            ProductGetResponseDTO productGetResponseDTO = new ProductGetResponseDTO();
            productGetResponseDTO.setProductCode(p.getProductCode());
            productGetResponseDTO.setProductName(p.getProductName());
            productGetResponseDTO.setPrice(p.getPrice());
            productGetResponseDTO.setMerchantCode(p.getMerchant().getMerchantCode());
            productGetResponseDTO.setMerchantName(p.getMerchant().getMerchantName());
            return productGetResponseDTO;
        }).collect(Collectors.toList());
        responseHandling.setData(productGetResponseDTOS);
        responseHandling.setMessage("Success get data");
        return responseHandling;
    }

    @Override
    public ProductDeleteResponseDTO deleteData(String code) {
        Optional<Product> product = productRepository.findByProductCode(code);
        if (!product.isPresent()){
            return ProductDeleteResponseDTO.builder()
                    .message("product fail to delete")
                    .errors("product code not found")
                    .build();
        }
        Product product1 = product.get();
        product1.setActive(false);
        productRepository.save(product1);
        return ProductDeleteResponseDTO.builder()
                .message("success to delete product")
                .build();
    }


    private String GetUUIDCode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);
        return kode;
    }



}
