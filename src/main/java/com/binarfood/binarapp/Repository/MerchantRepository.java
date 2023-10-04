package com.binarfood.binarapp.Repository;

import com.binarfood.binarapp.Entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    boolean existsByMerchantName(String merchantName);

    boolean existsByMerchantLocation(String merchantLocation);

    Optional<Merchant> findByMerchantCode(String kode);

    List<Merchant> findByDeletedFalse();

    Optional<List<Merchant>> findByMerchantNameContaining(String nama);
}
