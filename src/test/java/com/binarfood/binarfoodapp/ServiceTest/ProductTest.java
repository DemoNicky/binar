package com.binarfood.binarfoodapp.ServiceTest;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Merchant;
import com.binarfood.binarfoodapp.Entity.Product;
import com.binarfood.binarfoodapp.Repository.MerchantRepository;
import com.binarfood.binarfoodapp.Repository.ProductRepository;
import com.binarfood.binarfoodapp.Service.Impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductTest{

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MerchantRepository merchantRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Merchant merchant1 = new Merchant();
        merchant1.setMerchantCode("12345");
        merchant1.setMerchantName("Merchant1");
        merchant1.setMerchantLocation("Jakarta");
        merchant1.setOpen(true);
        merchant1.setDeleted(false);

        Merchant merchant2 = new Merchant();
        merchant2.setMerchantCode("67890");
        merchant2.setMerchantName("Merchant2");
        merchant2.setMerchantLocation("Bogor");
        merchant2.setOpen(true);
        merchant2.setDeleted(false);

        product1 = new Product();
        product1.setProductCode("23456");
        product1.setProductName("Product1");
        product1.setPrice(BigDecimal.valueOf(20000));
        product1.setActive(true);
        product1.setMerchant(merchant1);

        product2 = new Product();
        product2.setProductCode("78901");
        product2.setProductName("Product2");
        product2.setPrice(BigDecimal.valueOf(25000));
        product2.setActive(true);
        product2.setMerchant(merchant2);
    }

    @Test
    public void testCreateProductPositive() {
        Merchant merchant = new Merchant();
        merchant.setMerchantCode("M123");
        when(merchantRepository.findByMerchantCode("M123")).thenReturn(Optional.of(merchant));

        Product product = new Product();
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setMerchantCode("M123");
        productRequestDTO.setProductName("Product A");
        productRequestDTO.setPrice(BigDecimal.valueOf(100.0));

        ResponseHandling<ProductResponseDTO> response = productService.createProduct(productRequestDTO);

        assertEquals("success create new product", response.getMessage());
        assertEquals("M123", response.getData().getKodeMerchant());
    }

    @Test
    public void testCreateProductNegative() {
        when(merchantRepository.findByMerchantCode("NonExistentMerchant")).thenReturn(Optional.empty());

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setMerchantCode("NonExistentMerchant");
        productRequestDTO.setProductName("Product A");
        productRequestDTO.setPrice(BigDecimal.valueOf(100.0));
        ResponseHandling<ProductResponseDTO> response = productService.createProduct(productRequestDTO);
        assertEquals("Merchant kode Not found!!!", response.getErrors());
    }

    @Test
    public void getData_Success() {
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);

        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(productList));

        ResponseHandling<List<ProductGetResponseDTO>> result = productService.getData(pageable);

        assertEquals("Success get all data", result.getMessage());
        assertEquals(2, result.getData().size());
        assertEquals("Product1", result.getData().get(0).getProductName());
        assertEquals(BigDecimal.valueOf(20000), result.getData().get(0).getPrice());
        assertEquals("Merchant1", result.getData().get(0).getMerchantName());
        assertEquals("Product2", result.getData().get(1).getProductName());
        assertEquals(BigDecimal.valueOf(25000), result.getData().get(1).getPrice());
        assertEquals("Merchant2", result.getData().get(1).getMerchantName());
    }

    @Test
    public void getData_Empty() {
        List<Product> productList = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(productList));

        ResponseHandling<List<ProductGetResponseDTO>> result = productService.getData(pageable);

        assertEquals("Can't get Data", result.getMessage());
        assertEquals("All Product Data is empty", result.getErrors());
        assertEquals(null, result.getData());
    }

    @Test
    public void testUpdateDataPositive() {
        Product product = new Product();
        product.setProductCode("P123");
        product.setProductName("Product A");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setActive(true);

        Merchant merchant = new Merchant();
        merchant.setMerchantCode("M123");
        product.setMerchant(merchant);

        ProductUpdateRequestDTO requestDTO = new ProductUpdateRequestDTO();
        requestDTO.setProductName("Updated Product");
        requestDTO.setPrice(BigDecimal.valueOf(150.0));
        requestDTO.setActive(false);

        when(productRepository.findByProductCode("P123")).thenReturn(Optional.of(product));

        ResponseHandling<ProductResponseDTO> response = productService.updateData("P123", requestDTO);

        assertEquals("Success to update Product", response.getMessage());
        assertEquals("P123", response.getData().getProductCode());
        assertEquals("Updated Product", response.getData().getProductName());
        assertEquals(BigDecimal.valueOf(150.0), response.getData().getPrice());
        assertEquals("M123", response.getData().getKodeMerchant());
    }

    @Test
    public void testUpdateDataNegative() {
        when(productRepository.findByProductCode("P123")).thenReturn(Optional.empty());

        ProductUpdateRequestDTO requestDTO = new ProductUpdateRequestDTO();
        requestDTO.setProductName("Updated Product");
        requestDTO.setPrice(BigDecimal.valueOf(150.0));
        requestDTO.setActive(false);

        ResponseHandling<ProductResponseDTO> response = productService.updateData("P123", requestDTO);

        assertEquals("Fail to update Product", response.getMessage());
        assertEquals("Product with code P123 not found", response.getErrors());
    }

    @Test
    public void testGetDataByNamePositive() {
        Product product1 = new Product();
        product1.setProductCode("P123");
        product1.setProductName("Product A");
        product1.setPrice(BigDecimal.valueOf(100.0));
        Merchant merchant1 = new Merchant();
        merchant1.setMerchantCode("M123");
        merchant1.setMerchantName("Merchant A");
        product1.setMerchant(merchant1);

        Product product2 = new Product();
        product2.setProductCode("P124");
        product2.setProductName("Product B");
        product2.setPrice(BigDecimal.valueOf(150.0));
        Merchant merchant2 = new Merchant();
        merchant2.setMerchantCode("M124");
        merchant2.setMerchantName("Merchant B");
        product2.setMerchant(merchant2);

        when(productRepository.findProductsByProductNameContaining("Product")).thenReturn(Arrays.asList(
                Optional.of(product1), Optional.of(product2)
        ));

        ResponseHandling<List<ProductGetResponseDTO>> response = productService.getDataByName("Product");

        assertEquals("Success get data", response.getMessage());
        List<ProductGetResponseDTO> data = response.getData();
        assertEquals(2, data.size());
        assertEquals("P123", data.get(0).getProductCode());
        assertEquals("Product A", data.get(0).getProductName());
        assertEquals(BigDecimal.valueOf(100.0), data.get(0).getPrice());
        assertEquals("M123", data.get(0).getMerchantCode());
        assertEquals("Merchant A", data.get(0).getMerchantName());
    }

    @Test
    public void testGetDataByNameNegative() {
        when(productRepository.findProductsByProductNameContaining("NonexistentProduct")).thenReturn(Arrays.asList());

        ResponseHandling<List<ProductGetResponseDTO>> response = productService.getDataByName("NonexistentProduct");

        assertEquals("failed to get product", response.getMessage());
        assertEquals("product with name NonexistentProduct not found", response.getErrors());
    }

    @Test
    public void testDeleteDataPositive() {
        Product product = new Product();
        when(productRepository.findByProductCode("P123")).thenReturn(Optional.of(product));

        ProductDeleteResponseDTO response = productService.deleteData("P123");

        assertEquals("success to delete product", response.getMessage());
    }

    @Test
    public void testDeleteDataNegative() {
        when(productRepository.findByProductCode("NonexistentProduct")).thenReturn(Optional.empty());

        ProductDeleteResponseDTO response = productService.deleteData("NonexistentProduct");

        assertEquals("product fail to delete", response.getMessage());
        assertEquals("product code not found", response.getErrors());
    }

}
