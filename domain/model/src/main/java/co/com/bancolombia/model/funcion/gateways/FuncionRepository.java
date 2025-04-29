package co.com.bancolombia.model.funcion.gateways;

import co.com.bancolombia.model.funcion.Funcion;

import java.util.List;
import java.util.Optional;

public interface FuncionRepository {
    Iterable<Funcion> obtenerFunciones();
    Optional<Funcion> obtenerFuncionPorId(int funcion);
    Funcion guardarFuncion(Funcion funcion);
    Funcion actualizarFuncion(Funcion funcion);
    boolean eliminarFuncion(Funcion funcion);

    //metodo para reporte
    List<Funcion> obtenerFuncionesMasVendidas();
}
