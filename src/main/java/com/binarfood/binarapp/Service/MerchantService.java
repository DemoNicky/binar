package com.binarfood.binarapp.Service;

import com.binarfood.binarapp.DTO.CreateMerchantDTO;
import com.binarfood.binarapp.DTO.FindAllMerchantDTO;
import com.binarfood.binarapp.DTO.FindMerchantAndProductDTO;
import com.binarfood.binarapp.DTO.FindMerchantDTO;
import com.binarfood.binarapp.Entity.Merchant;

import java.util.List;

public interface MerchantService {
    List<FindAllMerchantDTO> findAllMerchant();

    Boolean createMerchant(CreateMerchantDTO createMerchantDTO);

    FindMerchantDTO findMerchant(String kode);


    String updateMerchant(String kode, String nama, String lokasi, int open);

    String deleteMerchant(String kode);

    List<FindMerchantAndProductDTO> findMerchantUser(String nama);
}
