package com.binarfood.binarapp.Service.Impl;

import com.binarfood.binarapp.DTO.CreateProductDTO;
import com.binarfood.binarapp.DTO.FindAllProductDTO;
import com.binarfood.binarapp.DTO.FindProdukDTO;
import com.binarfood.binarapp.Entity.Merchant;
import com.binarfood.binarapp.Entity.Product;
import com.binarfood.binarapp.Repository.MerchantRepository;
import com.binarfood.binarapp.Repository.ProductRepository;
import com.binarfood.binarapp.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Boolean createProduct(CreateProductDTO create) {
        logger.info("Creating a new product: {}", create.getProductName());
        if (create != null){
            String kode = getString();
            Merchant merchant = getMerchant(create);

            if (merchant != null){
                Product product = new Product();
                product.setProductName(create.getProductName());
                product.setProductCode(kode);
                product.setPrice(create.getPrice());
                product.setMerchant(merchant);
                productRepository.save(product);

            }else {
                logger.warn("Failed to create product because merchant not found.");
                return false;
            }
            return true;
        }else {
            logger.warn("Failed to create product because input is null.");
            return false;

        }

    }

    private Merchant getMerchant(CreateProductDTO create) {
        Optional<Merchant> merchant = merchantRepository.findByMerchantCode(create.getMerchantCode());
        Merchant merchant1 = merchant.get();
        if (!merchant.isPresent()){
            return null;
        }
        return merchant1;
    }

    private String getString() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);
        return kode;
    }

    @Override
    public List<FindAllProductDTO> findAllProduct(Pageable pageable) {
        logger.info("Finding all products with pageable: {}", pageable);

        try {
            Page<Product> products = productRepository.findAll(pageable);
            if (products != null && !products.isEmpty()) {
                List<FindAllProductDTO> findAllProductDTO = products.stream().map((p) ->{
                    FindAllProductDTO findAllProductDTO1 = new FindAllProductDTO();
                    findAllProductDTO1.setProductCode(p.getProductCode());
                    findAllProductDTO1.setProductName(p.getProductName());
                    findAllProductDTO1.setPrice(p.getPrice());
                    findAllProductDTO1.setKodeMerchant(p.getMerchant().getMerchantCode());
                    return findAllProductDTO1;
                }).collect(Collectors.toList());
                logger.info("Found {} products.", findAllProductDTO.size());

                return findAllProductDTO;
            } else {
                logger.info("No products found.");
                return Collections.emptyList();
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            logger.error("Error while finding products: {}", e.getMessage());
            return Collections.emptyList();
        }

    }

    @Override
    public List<FindProdukDTO> findProductWithName(String nama) {
        logger.info("Finding products with name containing: {}", nama);

        List<Optional<Product>> productsOptional = productRepository.findProductsByProductNameContaining(nama);
        List<FindProdukDTO> findProdukDTOList = new ArrayList<>();

        for (Optional<Product> productOptional : productsOptional) {
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                FindProdukDTO findProdukDTO = FindProdukDTO.builder()
                        .productCode(product.getProductCode())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .kodeMerchant(product.getMerchant().getMerchantCode())
                        .build();

                findProdukDTOList.add(findProdukDTO);
            }
        }
        logger.info("Found {} products.", findProdukDTOList.size());

        return findProdukDTOList;
    }

    @Override
    public String updateProduct(String kode, String nama, BigDecimal harga) {
        logger.info("Updating product with code: {}", kode);

        Optional<Product> product = productRepository.findByProductCode(kode);
        if (product.isPresent()){
            Product product1 = product.get();
            product1.setProductName(nama);
            product1.setPrice(harga);
            productRepository.save(product1);
            logger.info("Product updated: {}", product1);

            return "Produk Berhasil DI simpan";
        }
        logger.warn("Failed to update product because product not found.");

        return "update Gagal !!!";
    }

    @Override
    public Boolean deleteProduct(String kode) {
        logger.info("Deleting product with code: {}", kode);

        Optional<Product> ress = productRepository.findByProductCode(kode);
        if (ress.isPresent()){
            productRepository.deleteByProductCode(kode);
            logger.info("Product deleted with code: {}", kode);

            return true;
        }
        logger.warn("Failed to delete product because product not found.");

        return false;
    }
}
