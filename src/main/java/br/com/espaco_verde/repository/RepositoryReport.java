package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryReport extends JpaRepository<Report, Integer> {
}
