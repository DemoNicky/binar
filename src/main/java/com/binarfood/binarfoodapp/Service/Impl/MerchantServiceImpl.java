package com.binarfood.binarfoodapp.Service.Impl;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Merchant;
import com.binarfood.binarfoodapp.Repository.MerchantRepository;
import com.binarfood.binarfoodapp.Service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    private static final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public ResponseHandling<MerchantResponseDTO> createMerchant(MerchantRequestDTO merchantRequestDTO) {
        ResponseHandling<MerchantResponseDTO> response = new ResponseHandling<>();
        if (merchantRepository.existsByMerchantName(merchantRequestDTO.getMerchantName()) ||
                merchantRepository.existsByMerchantLocation(merchantRequestDTO.getMerchantLocation())) {
                logger.warn("Fail to Create Merchant");
                response.setErrors("name/location of merchant is already exists!!!");
                return response;
        }else {
            String kode = getKode();

            Merchant merchant = new Merchant();
            merchant.setMerchantCode(kode);
            merchant.setMerchantName(merchantRequestDTO.getMerchantName());
            merchant.setMerchantLocation(merchantRequestDTO.getMerchantLocation());
            merchant.setOpen(true);
            merchant.setDeleted(false);
            merchantRepository.save(merchant);
            logger.info("Merchant Sukses di Buat", merchant);
            MerchantResponseDTO merchantResponseDTO = new MerchantResponseDTO();
            merchantResponseDTO.setMerchantCode(merchant.getMerchantCode());
            merchantResponseDTO.setMerchantName(merchant.getMerchantName());
            merchantResponseDTO.setMerchantLocation(merchant.getMerchantLocation());
            merchantResponseDTO.setOpen("Buka");

            response.setData(merchantResponseDTO);
            response.setMessage("success create new merchant");
            return response;
        }
    }

    private String getKode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);
        return kode;
    }

    @Override
    public ResponseHandling<List<MerchantResponseDTO>> getData(Pageable pageable) {
        Page<Merchant> merchants = merchantRepository.findByDeletedIsFalseAndOpenIsTrue(pageable);
        ResponseHandling<List<MerchantResponseDTO>> response = new ResponseHandling<>();
        if (merchants.isEmpty()){
            response.setMessage("Fail to get data");
            response.setErrors("Merchant with page number " +pageable.getPageNumber()+ "is empty");
            return response;
        }else {
            List<MerchantResponseDTO> responseDTOS = merchants.stream().map((p) -> {
                MerchantResponseDTO merchantResponseDTO = new MerchantResponseDTO();
                merchantResponseDTO.setMerchantCode(p.getMerchantCode());
                merchantResponseDTO.setMerchantName(p.getMerchantName());
                merchantResponseDTO.setMerchantLocation(p.getMerchantLocation());
                merchantResponseDTO.setOpen("Buka");
                return merchantResponseDTO;
            }).collect(Collectors.toList());
            response.setData(responseDTOS);
            response.setMessage("Success get data");
            return response;
        }
    }

    @Override
    public ResponseHandling<MerchantResponseDTO> updateMerchant(String code, MerchantUpdateRequest requestDTO) {
        Optional<Merchant> merchant = merchantRepository.findByMerchantCode(code);
        ResponseHandling<MerchantResponseDTO> response = new ResponseHandling<>();
        if (!merchant.isPresent()){
            response.setMessage("Failed to update data");
            response.setErrors("Merchant with code "+code+" is not found");
            return response;
        }else {
            Merchant merchant1 = merchant.get();
            merchant1.setMerchantName(requestDTO.getMerchantName());
            merchant1.setMerchantLocation(requestDTO.getMerchantLocation());
            merchant1.setOpen(requestDTO.isOpen());
            merchantRepository.save(merchant1);
            MerchantResponseDTO merchantResponseDTO = new MerchantResponseDTO();
            merchantResponseDTO.setMerchantCode(merchant1.getMerchantCode());
            merchantResponseDTO.setMerchantName(merchant1.getMerchantName());
            merchantResponseDTO.setMerchantLocation(merchant1.getMerchantLocation());
            if (merchant1.getOpen() == true){
                merchantResponseDTO.setOpen("Buka");
            }else {
                merchantResponseDTO.setOpen("Tutup");
            }
            response.setData(merchantResponseDTO);
            response.setMessage("Success update data with code " +code);
            return response;
        }
    }

    @Override
    public MerchantDeleteResponseDTO deleteData(String code) {
        Optional<Merchant> merchant = merchantRepository.findByMerchantCode(code);
        MerchantDeleteResponseDTO response = new MerchantDeleteResponseDTO();
        if (!merchant.isPresent()){
            response.setMessage("Failed to delete data ");
            response.setErrors("code not found");
            return response;
        }else {
            Merchant merchant2 = merchant.get();
            merchant2.setDeleted(true);
            merchantRepository.save(merchant2);
            response.setMessage("Success delete data with code "+ code);
            return response;
        }
    }
}
