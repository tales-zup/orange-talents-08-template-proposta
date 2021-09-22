package com.zup.propostaservice.carteira;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {

    boolean existsByCarteiraAndCartao_Id(CarteiraEnum carteira, String id);

}
