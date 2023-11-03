package com.binarfood.binarfoodapp.ControllerTest;

import com.binarfood.binarfoodapp.Controller.MerchantController;
import com.binarfood.binarfoodapp.DTO.MerchantRequestDTO;
import com.binarfood.binarfoodapp.DTO.MerchantResponseDTO;
import com.binarfood.binarfoodapp.DTO.ResponseHandling;
import com.binarfood.binarfoodapp.Service.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MerchantControllerTest {

    @InjectMocks
    private MerchantController merchantController;

    @Mock
    private MerchantService merchantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMerchant_Success() {
        // Create a request DTO
        MerchantRequestDTO merchantRequestDTO = new MerchantRequestDTO();
        merchantRequestDTO.setMerchantName("Test Merchant");
        merchantRequestDTO.setMerchantLocation("Test Location");

        // Create a successful response using your ResponseHandling class
        MerchantResponseDTO successResponse = new MerchantResponseDTO();
        successResponse.setMerchantCode("M123");
        successResponse.setMerchantName("Test Merchant");
        successResponse.setMerchantLocation("Test Location");
        successResponse.setOpen("Yes");

        ResponseHandling<MerchantResponseDTO> successHandling = new ResponseHandling<>(successResponse, "Success message", null);

        Mockito.when(merchantService.createMerchant(merchantRequestDTO))
                .thenReturn(successHandling);

        ResponseEntity<ResponseHandling<MerchantResponseDTO>> response = merchantController.createMerchant(merchantRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseHandling<MerchantResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("M123", responseBody.getData().getMerchantCode());
        assertEquals("Test Merchant", responseBody.getData().getMerchantName());
        assertEquals("Test Location", responseBody.getData().getMerchantLocation());
        assertEquals("Yes", responseBody.getData().getOpen());
    }

    @Test
    public void testCreateMerchant_BadRequest() {
        // Create an invalid request DTO
        MerchantRequestDTO invalidRequest = new MerchantRequestDTO();

        // Create an error response using your ResponseHandling class
        ResponseHandling<MerchantResponseDTO> errorHandling = new ResponseHandling<>(null, "Error message", "Invalid request");

        Mockito.when(merchantService.createMerchant(invalidRequest))
                .thenReturn(errorHandling);

        ResponseEntity<ResponseHandling<MerchantResponseDTO>> response = merchantController.createMerchant(invalidRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseHandling<MerchantResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error message", responseBody.getMessage());
        assertEquals("Invalid request", responseBody.getErrors());
    }

    @Test
    public void testGetActiveMerchant_Success() {
        int page = 0;
        List<MerchantResponseDTO> activeMerchants = new ArrayList<>();

        ResponseHandling<List<MerchantResponseDTO>> successHandling = new ResponseHandling<>(activeMerchants, "Success message", null);

        Mockito.when(merchantService.getData(Mockito.any(Pageable.class)))
                .thenReturn(successHandling);

        ResponseEntity<ResponseHandling<List<MerchantResponseDTO>>> response = merchantController.getActiveMerchant(page);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseHandling<List<MerchantResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Success message", responseBody.getMessage());
        assertEquals(activeMerchants, responseBody.getData());
    }

    @Test
    public void testGetActiveMerchant_NotFound() {
        int page = 0;
        List<MerchantResponseDTO> emptyMerchants = new ArrayList<>();
        ResponseHandling<List<MerchantResponseDTO>> errorHandling = new ResponseHandling<>(null, "Error message", "No active merchants found");

        Mockito.when(merchantService.getData(Mockito.any(Pageable.class)))
                .thenReturn(errorHandling);

        ResponseEntity<ResponseHandling<List<MerchantResponseDTO>>> response = merchantController.getActiveMerchant(page);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ResponseHandling<List<MerchantResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error message", responseBody.getMessage());
        assertEquals("No active merchants found", responseBody.getErrors());
    }

}
