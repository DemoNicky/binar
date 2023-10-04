package com.binarfood.binarapp.Service.Impl;

import com.binarfood.binarapp.DTO.*;
import com.binarfood.binarapp.Entity.Merchant;
import com.binarfood.binarapp.Entity.Product;
import com.binarfood.binarapp.Repository.MerchantRepository;
import com.binarfood.binarapp.Repository.ProductRepository;
import com.binarfood.binarapp.Service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MerchantServiceImpl implements MerchantService {

    private static final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<FindAllMerchantDTO> findAllMerchant() {
        logger.info("Finding all merchants");
        List<Merchant> merchants = merchantRepository.findByDeletedFalse();
        List<FindAllMerchantDTO> findAllMerchantDTOS =merchants.stream().map((p) -> {
            FindAllMerchantDTO findAllMerchantDTO = new FindAllMerchantDTO();
            findAllMerchantDTO.setMerchantName(p.getMerchantName());
            findAllMerchantDTO.setMerchantLocation(p.getMerchantLocation());
            findAllMerchantDTO.setMerchantCode(p.getMerchantCode());
            findAllMerchantDTO.setOpen(p.getOpen());
            return findAllMerchantDTO;
        }).collect(Collectors.toList());
        return findAllMerchantDTOS;
    }

    @Override
    public Boolean createMerchant(CreateMerchantDTO createMerchantDTO) {
        if (merchantRepository.existsByMerchantName(createMerchantDTO.getMerchantName()) ||
                merchantRepository.existsByMerchantLocation(createMerchantDTO.getMerchantLocation())){
            logger.warn("Gagal Mmebuat Merchant Karena : ", createMerchantDTO);
            return false;
        }
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);

        Merchant merchant = new Merchant();
        merchant.setMerchantName(createMerchantDTO.getMerchantName());
        merchant.setMerchantLocation(createMerchantDTO.getMerchantLocation());
        merchant.setMerchantCode(kode);
        merchant.setOpen(true);
        merchant.setDeleted(false);
        merchantRepository.save(merchant);
        logger.info("Merchant Sukses di Buat", merchant);
        return true;

    }

    @Override
    public FindMerchantDTO findMerchant(String kode) {
        logger.info("Finding merchant by code: {}", kode);
        Optional<Merchant> merchant = merchantRepository.findByMerchantCode(kode);
        if (merchant.isPresent()){
            Merchant merchant1 = merchant.get();
            return FindMerchantDTO.builder()
                    .merchantName(merchant1.getMerchantName())
                    .merchantLocation(merchant1.getMerchantLocation())
                    .merchantCode(merchant1.getMerchantCode())
                    .open(merchant1.getOpen())
                    .build();
        }
        return null;
    }

    @Override
    public String updateMerchant(String kode, String nama, String lokasi, int open) {
        logger.info("Updating merchant with code: {}", kode);
        boolean opn = false;
        Optional<Merchant> merchant = merchantRepository.findByMerchantCode(kode);
        if (open <= 1 && open >= 2){
            return "update gagal angka tidak valid";
        }else if(open == 1){
            opn = true;
        }else if (open == 2){
            opn = false;
        }
        if (merchant.isPresent()){
            Merchant merchant1 = merchant.get();
            merchant1.setMerchantName(nama);
            merchant1.setMerchantLocation(lokasi);
            merchant1.setOpen(opn);
            merchantRepository.save(merchant1);
            return "update berhasil";
        }
        return "update Gagal !!!";
    }

    @Override
    public String deleteMerchant(String kode) {
        logger.info("Deleting merchant with code: {}", kode);
        Optional<Merchant> merchant = merchantRepository.findByMerchantCode(kode);
        if (!merchant.isPresent()){
            return "kode tidak valid!!!";
        }else {
            Merchant merchant1 = merchant.get();
            merchant1.setDeleted(true);
            merchantRepository.save(merchant1);
            return "Merchant sudah di hapus";
        }
    }

    @Override
    public List<FindMerchantAndProductDTO> findMerchantUser(String nama) {
        logger.info("Finding merchants by name containing: {}", nama);
        Optional<List<Merchant>> merchant = merchantRepository.findByMerchantNameContaining(nama);
        if (merchant.isPresent()){
            List<FindMerchantAndProductDTO> merchants = merchant.get().stream().map((p) -> {
                FindMerchantAndProductDTO merchant1 = new FindMerchantAndProductDTO();
                merchant1.setMerchantName(p.getMerchantName());
                merchant1.setMerchantLocation(p.getMerchantLocation());
                merchant1.setMerchantCode(p.getMerchantCode());
                merchant1.setOpen(p.getOpen());
                List<Product> products = productRepository.findByMerchant(p);
                merchant1.setProducts(products);
                return merchant1;
            }).collect(Collectors.toList());

            return merchants;
        }else {
            throw new NullPointerException("Merchant dengan nama " + nama + " tidak ditemukan.");
        }
    }

}
