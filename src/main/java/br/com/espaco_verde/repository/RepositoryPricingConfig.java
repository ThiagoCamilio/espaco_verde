package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.PricingConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryPricingConfig extends JpaRepository<PricingConfig, Integer> {
}
