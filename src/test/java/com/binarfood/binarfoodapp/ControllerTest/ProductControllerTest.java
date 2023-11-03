package com.binarfood.binarfoodapp.ControllerTest;

import com.binarfood.binarfoodapp.Controller.ProductController;
import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct_Success() {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setProductName("Sample Product");
        requestDTO.setPrice(new BigDecimal("10.99"));
        requestDTO.setMerchantCode("MRC12345");

        ProductResponseDTO productResponse = new ProductResponseDTO();
        productResponse.setProductCode(UUID.randomUUID().toString());
        productResponse.setProductName(requestDTO.getProductName());
        productResponse.setPrice(requestDTO.getPrice());
        productResponse.setKodeMerchant(requestDTO.getMerchantCode());

        Mockito.when(productService.createProduct(requestDTO)).thenReturn(new ResponseHandling<>(productResponse, "Success message", null));

        ResponseEntity<ResponseHandling<ProductResponseDTO>> response = productController.createProduct(requestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseHandling<ProductResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Success message", responseBody.getMessage());

        ProductResponseDTO productResponseBody = responseBody.getData();
        assertNotNull(productResponseBody);
        assertEquals(requestDTO.getProductName(), productResponseBody.getProductName());
        assertEquals(requestDTO.getPrice(), productResponseBody.getPrice());
        assertEquals(requestDTO.getMerchantCode(), productResponseBody.getKodeMerchant());
    }

    @Test
    public void testCreateProduct_InvalidRequest() {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setProductName("");
        requestDTO.setPrice(new BigDecimal("10.99"));
        requestDTO.setMerchantCode("MRC12345");

        String errorMessage = "Product name cannot be blank";
        Mockito.when(productService.createProduct(requestDTO)).thenReturn(new ResponseHandling<>(null, null, errorMessage));

        ResponseEntity<ResponseHandling<ProductResponseDTO>> response = productController.createProduct(requestDTO);

        assertNotNull(response);
        ResponseHandling<ProductResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(errorMessage, responseBody.getErrors());
    }

    @Test
    public void testGetAllProduct_Success() {
        int page = 0;

        List<ProductGetResponseDTO> productDTOList = new ArrayList<>();
        ProductGetResponseDTO productDTO = new ProductGetResponseDTO();
        productDTO.setProductCode(UUID.randomUUID().toString());
        productDTO.setProductName("Sample Product");
        productDTO.setPrice(new BigDecimal("10.99"));
        productDTO.setMerchantCode("MRC12345");
        productDTO.setMerchantName("Sample Merchant");
        productDTOList.add(productDTO);

        Mockito.when(productService.getData(PageRequest.of(page, 10))).thenReturn(new ResponseHandling<>(productDTOList, "Success message", null));

        ResponseEntity<ResponseHandling<List<ProductGetResponseDTO>>> response = productController.getAllProduct(page);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseHandling<List<ProductGetResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Success message", responseBody.getMessage());

        List<ProductGetResponseDTO> productResponseBody = responseBody.getData();
        assertNotNull(productResponseBody);
        assertEquals(1, productResponseBody.size());

        ProductGetResponseDTO firstProduct = productResponseBody.get(0);
        assertEquals(productDTO.getProductCode(), firstProduct.getProductCode());
        assertEquals(productDTO.getProductName(), firstProduct.getProductName());
        assertEquals(productDTO.getPrice(), firstProduct.getPrice());
        assertEquals(productDTO.getMerchantCode(), firstProduct.getMerchantCode());
        assertEquals(productDTO.getMerchantName(), firstProduct.getMerchantName());
    }

    @Test
    public void testGetAllProduct_NoDataFound() {
        int page = 0;

        Mockito.when(productService.getData(PageRequest.of(page, 10))).thenReturn(new ResponseHandling<>(null, null, "No data found"));

        ResponseEntity<ResponseHandling<List<ProductGetResponseDTO>>> response = productController.getAllProduct(page);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseHandling<List<ProductGetResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("No data found", responseBody.getErrors());
    }

    @Test
    public void testGetDataByName_Success() {
        String name = "Sample Product";

        List<ProductGetResponseDTO> productDTOList = new ArrayList<>();
        ProductGetResponseDTO productDTO = new ProductGetResponseDTO();
        productDTO.setProductCode(UUID.randomUUID().toString());
        productDTO.setProductName(name);
        productDTO.setPrice(new BigDecimal("10.99"));
        productDTO.setMerchantCode("MRC12345");
        productDTO.setMerchantName("Sample Merchant");
        productDTOList.add(productDTO);

        Mockito.when(productService.getDataByName(name)).thenReturn(new ResponseHandling<>(productDTOList, "Success message", null));

        ResponseEntity<ResponseHandling<List<ProductGetResponseDTO>>> response = productController.getDataByName(name);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseHandling<List<ProductGetResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Success message", responseBody.getMessage());

        List<ProductGetResponseDTO> productResponseBody = responseBody.getData();
        assertNotNull(productResponseBody);
        assertEquals(1, productResponseBody.size());

        ProductGetResponseDTO firstProduct = productResponseBody.get(0);
        assertEquals(productDTO.getProductCode(), firstProduct.getProductCode());
        assertEquals(productDTO.getProductName(), firstProduct.getProductName());
        assertEquals(productDTO.getPrice(), firstProduct.getPrice());
        assertEquals(productDTO.getMerchantCode(), firstProduct.getMerchantCode());
        assertEquals(productDTO.getMerchantName(), firstProduct.getMerchantName());
    }

    @Test
    public void testGetDataByName_NoDataFound() {
        String name = "Non-existent Product";

        Mockito.when(productService.getDataByName(name)).thenReturn(new ResponseHandling<>(null, null, "No data found"));

        ResponseEntity<ResponseHandling<List<ProductGetResponseDTO>>> response = productController.getDataByName(name);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseHandling<List<ProductGetResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("No data found", responseBody.getErrors());
    }

    @Test
    public void testUpdateData_BadRequest() {
        String productCode = UUID.randomUUID().toString();

        ProductUpdateRequestDTO requestDTO = new ProductUpdateRequestDTO();
        requestDTO.setProductName("");

        Mockito.when(productService.updateData(productCode, requestDTO))
                .thenReturn(new ResponseHandling<>(null, null, "Bad request"));

        ResponseEntity<ResponseHandling<ProductResponseDTO>> response = productController.updateData(productCode, requestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ResponseHandling<ProductResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Bad request", responseBody.getErrors());
    }

    @Test
    public void testDeleteData_Success() {
        String productCode = UUID.randomUUID().toString();

        ProductDeleteResponseDTO successResponse = ProductDeleteResponseDTO.builder()
                .message("Product deleted successfully")
                .errors(null)
                .build();

        Mockito.when(productService.deleteData(productCode))
                .thenReturn(successResponse);

        ResponseEntity<ProductDeleteResponseDTO> response = productController.deleteData(productCode);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ProductDeleteResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Product deleted successfully", responseBody.getMessage());
        assertEquals(null, responseBody.getErrors());
    }

    @Test
    public void testDeleteData_NotFound() {
        String productCode = UUID.randomUUID().toString();

        ProductDeleteResponseDTO errorResponse = ProductDeleteResponseDTO.builder()
                .message(null)
                .errors("Product not found")
                .build();

        Mockito.when(productService.deleteData(productCode))
                .thenReturn(errorResponse);

        ResponseEntity<ProductDeleteResponseDTO> response = productController.deleteData(productCode);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ProductDeleteResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(null, responseBody.getMessage());
        assertEquals("Product not found", responseBody.getErrors());
    }



}
