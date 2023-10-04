package com.binarfood.binarapp.Repository;

import com.binarfood.binarapp.Entity.Merchant;
import com.binarfood.binarapp.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String > {

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :nama, '%'))")
    List<Optional<Product>> findProductsByProductNameContaining(@Param("nama") String nama);

    Optional<Product> findByProductCode(String kode);

    void deleteByProductCode(String kode);


    List<Product> findByMerchant(Merchant merchant);
}
