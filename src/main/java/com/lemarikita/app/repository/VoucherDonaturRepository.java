package com.lemarikita.app.repository;

import com.lemarikita.app.model.StatusVoucher;
import com.lemarikita.app.model.Voucher;
import com.lemarikita.app.model.VoucherDonatur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VoucherDonaturRepository extends JpaRepository<VoucherDonatur, Long> {
    List<VoucherDonatur> findByUserIdUserOrderByTanggalPenukaranDesc(Long idUser);
    List<VoucherDonatur> findByUserIdUserAndStatusPenggunaan(Long idUser, StatusVoucher status);
    Optional<VoucherDonatur> findByKodeVoucher(String kodeVoucher);
    long countByVoucher(Voucher voucher);
}
