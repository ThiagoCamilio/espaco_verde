package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.PricingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryPricingCategory extends JpaRepository<PricingCategory, Integer> {
}
