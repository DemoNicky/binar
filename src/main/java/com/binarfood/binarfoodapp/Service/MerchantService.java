package com.binarfood.binarfoodapp.Service;

import com.binarfood.binarfoodapp.DTO.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MerchantService {
    ResponseHandling<MerchantResponseDTO> createMerchant(MerchantRequestDTO merchantRequestDTO);

    ResponseHandling<List<MerchantResponseDTO>> getData(Pageable pageable);

    ResponseHandling<MerchantResponseDTO> updateMerchant(String code, MerchantUpdateRequest requestDTO);

    MerchantDeleteResponseDTO deleteData(String code);
}
