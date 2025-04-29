package co.com.bancolombia.jpa;

import co.com.bancolombia.jpa.modelsDb.BoletoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface JPARepository extends JpaRepository<BoletoEntity, Integer>, QueryByExampleExecutor<BoletoEntity> {
    List<BoletoEntity> findAllByOrderByBoletosVendidosDesc();
}