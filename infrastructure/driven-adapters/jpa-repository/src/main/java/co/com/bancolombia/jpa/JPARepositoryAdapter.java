package co.com.bancolombia.jpa;

import co.com.bancolombia.jpa.modelsDb.BoletoEntity;
import co.com.bancolombia.model.funcion.Funcion;
import co.com.bancolombia.model.funcion.gateways.FuncionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class JPARepositoryAdapter implements FuncionRepository {

    private static final Logger logger = LoggerFactory.getLogger(JPARepositoryAdapter.class);
    private final JPARepository repository;
    private final ObjectMapper mapper;

    @Override
    public Iterable<Funcion> obtenerFunciones() {
       logger.debug("consultar funciones en la base de datos");
       return StreamSupport.stream(repository.findAll().spliterator(), false)
               .map(entity -> mapper.convertValue(entity, Funcion.class))
               .collect(Collectors.toList());
    }

    @Override
    public Optional<Funcion> obtenerFuncionPorId(int funcion) {
        logger.debug("Buscando funcion por ID en la base de datos: {}", funcion);
        return repository.findById(funcion)
                .map(entity -> {
                    logger.info("encontramos la funcion con id: {}",funcion);
                    return  mapper.convertValue(entity, Funcion.class);
                });
    }

    @Override
    public Funcion guardarFuncion(Funcion funcion) {
        logger.debug("Guardando una nueva funcion en la base de datos: {}", funcion);
        BoletoEntity entity = mapper.convertValue(funcion, BoletoEntity.class);
        BoletoEntity entityGuardada = repository.save(entity);
        return mapper.convertValue(entityGuardada, Funcion.class);
    }

    @Override
    public Funcion actualizarFuncion(Funcion funcion) {
        logger.debug("Actualizando .... una nueva funcion en la base de datos: {}", funcion);
        BoletoEntity entity = mapper.convertValue(funcion, BoletoEntity.class);
        BoletoEntity updateEntity = repository.save(entity);
        logger.debug("Actualizado! una nueva funcion en la base de datos: {}", funcion);
        return mapper.convertValue(updateEntity, Funcion.class);
    }

    @Override
    public boolean eliminarFuncion(Funcion funcion) {
        logger.debug("Eliminar funcion");
        BoletoEntity entity = mapper.convertValue(funcion, BoletoEntity.class);
        BoletoEntity updatedEntity = repository.save(entity);
        return true;
    }

    public List<Funcion> obtenerFuncionesMasVendidas() {
        logger.debug("Consultando funciones ordenadas por boletos vendidos DESC...");
        return repository.findAllByOrderByBoletosVendidosDesc().stream()
                .map(entity -> mapper.convertValue(entity, Funcion.class))
                .collect(Collectors.toList());
    }
}
