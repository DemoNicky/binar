package com.binarfood.binarfoodapp.ServiceTest;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Merchant;
import com.binarfood.binarfoodapp.Repository.MerchantRepository;
import com.binarfood.binarfoodapp.Service.Impl.MerchantServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MerchantTest {

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @Mock
    private MerchantRepository merchantRepository;

    @Test
    public void testCreateMerchantSuccess() {
        MerchantRequestDTO requestDTO = new MerchantRequestDTO();
        requestDTO.setMerchantName("SampleMerchant");
        requestDTO.setMerchantLocation("SampleLocation");

        when(merchantRepository.existsByMerchantName("SampleMerchant")).thenReturn(false);
        when(merchantRepository.existsByMerchantLocation("SampleLocation")).thenReturn(false);

        ResponseHandling<MerchantResponseDTO> response = merchantService.createMerchant(requestDTO);

        assertEquals("success create new merchant", response.getMessage());
        assertEquals("SampleMerchant", response.getData().getMerchantName());
        assertEquals("SampleLocation", response.getData().getMerchantLocation());
    }

    @Test
    public void testCreateMerchantFailure() {
        MerchantRequestDTO requestDTO = new MerchantRequestDTO();
        requestDTO.setMerchantName("ExistingMerchant");
        requestDTO.setMerchantLocation("SampleLocation");

        when(merchantRepository.existsByMerchantName("ExistingMerchant")).thenReturn(true);

        ResponseHandling<MerchantResponseDTO> response = merchantService.createMerchant(requestDTO);

        assertEquals("name/location of merchant is already exists!!!", response.getErrors());
    }

    @Test
    public void testGetDataSuccess() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Merchant> merchantList = new ArrayList<>();
        Merchant merchant1 = new Merchant();
        merchant1.setMerchantCode("M1");
        merchant1.setMerchantName("Merchant1");
        merchant1.setMerchantLocation("Location1");
        merchant1.setOpen(true);

        Merchant merchant2 = new Merchant();
        merchant2.setMerchantCode("M2");
        merchant2.setMerchantName("Merchant2");
        merchant2.setMerchantLocation("Location2");
        merchant2.setOpen(true);

        merchantList.add(merchant1);
        merchantList.add(merchant2);

        Page<Merchant> merchantPage = new PageImpl<>(merchantList);
        when(merchantRepository.findByDeletedIsFalseAndOpenIsTrue(pageable)).thenReturn(merchantPage);

        ResponseHandling<List<MerchantResponseDTO>> response = merchantService.getData(pageable);

        assertEquals("Success get data", response.getMessage());
        List<MerchantResponseDTO> responseDTOs = response.getData();
        assertEquals(2, responseDTOs.size());

        assertEquals("M1", responseDTOs.get(0).getMerchantCode());
        assertEquals("Merchant1", responseDTOs.get(0).getMerchantName());
        assertEquals("Location1", responseDTOs.get(0).getMerchantLocation());
        assertEquals("Buka", responseDTOs.get(0).getOpen());
    }

    @Test
    public void testGetDataFailure() {
        Pageable pageable = PageRequest.of(1, 10);

        Page<Merchant> emptyMerchantPage = new PageImpl<>(Collections.emptyList());
        when(merchantRepository.findByDeletedIsFalseAndOpenIsTrue(pageable)).thenReturn(emptyMerchantPage);

        ResponseHandling<List<MerchantResponseDTO>> response = merchantService.getData(pageable);

        assertEquals("Fail to get data", response.getMessage());
        assertEquals("Merchant with page number 1 is empty", response.getErrors());

        if (response.getData() != null) {
            assertEquals(0, response.getData().size());
        }
    }

    @Test
    public void testUpdateMerchantSuccess() {
        String merchantCode = "M123";
        MerchantUpdateRequest requestDTO = new MerchantUpdateRequest();
        requestDTO.setMerchantName("New Merchant Name");
        requestDTO.setMerchantLocation("New Location");
        requestDTO.setOpen(true);

        Merchant existingMerchant = new Merchant();
        existingMerchant.setMerchantCode(merchantCode);
        when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(Optional.of(existingMerchant));

        ResponseHandling<MerchantResponseDTO> response = merchantService.updateMerchant(merchantCode, requestDTO);

        assertNotNull(response);
        assertEquals("Success update data with code " + merchantCode, response.getMessage());
        assertNull(response.getErrors());
        assertNotNull(response.getData());

        MerchantResponseDTO responseDTO = response.getData();
        assertEquals(merchantCode, responseDTO.getMerchantCode());
        assertEquals(requestDTO.getMerchantName(), responseDTO.getMerchantName());
        assertEquals(requestDTO.getMerchantLocation(), responseDTO.getMerchantLocation());
        assertEquals("Buka", responseDTO.getOpen()); // Assuming "true" should be "Buka"
    }

    @Test
    public void testUpdateMerchantNotFound() {
        String merchantCode = "M123";
        MerchantUpdateRequest requestDTO = new MerchantUpdateRequest();
        requestDTO.setMerchantName("New Merchant Name");
        requestDTO.setMerchantLocation("New Location");
        requestDTO.setOpen(true);

        when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(Optional.empty());

        ResponseHandling<MerchantResponseDTO> response = merchantService.updateMerchant(merchantCode, requestDTO);

        assertNotNull(response);
        assertEquals("Failed to update data", response.getMessage());
        assertEquals("Merchant with code " + merchantCode + " is not found", response.getErrors());
        assertNull(response.getData());
    }

    @Test
    public void testDeleteMerchantSuccess() {
        String merchantCode = "M123";

        Merchant existingMerchant = new Merchant();
        existingMerchant.setMerchantCode(merchantCode);

        when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(Optional.of(existingMerchant));

        MerchantDeleteResponseDTO response = merchantService.deleteData(merchantCode);

        assertNotNull(response);
        assertEquals("Success delete data with code " + merchantCode, response.getMessage());
        assertNull(response.getErrors());
    }

    @Test
    public void testDeleteMerchantNotFound() {
        String merchantCode = "M123";

        when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(Optional.empty());

        MerchantDeleteResponseDTO response = merchantService.deleteData(merchantCode);

        assertNotNull(response);
        assertEquals("Failed to delete data", response.getMessage());
        assertEquals("code not found", response.getErrors());
    }
}
