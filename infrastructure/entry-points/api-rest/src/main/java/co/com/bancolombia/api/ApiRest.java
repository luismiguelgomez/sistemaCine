package co.com.bancolombia.api;
import co.com.bancolombia.model.DTO.FuncionDTO;
import co.com.bancolombia.model.funcion.Funcion;
import co.com.bancolombia.model.reponsemicro.ApiResponse;
import co.com.bancolombia.usecase.boletos.BoletosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Boletos", description = "Operaciones relacionadas al cine")
public class ApiRest {
    private final BoletosUseCase boletosUseCase;

    @Operation(summary = "Comprar boletas para una función")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Compra realizada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/comprarBoletas")
    public ResponseEntity<ApiResponse<Funcion>> comprarBoletas(@RequestParam int codigoFuncion, @RequestParam int boletosAcomprar){
        ApiResponse<Funcion> response = boletosUseCase.comprarBoletos(codigoFuncion, boletosAcomprar);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Crear una nueva función de cine")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Función creada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crearFuncion")
    public ApiResponse<Funcion> guardarFuncion(@RequestBody FuncionDTO funcionDTO){
        ApiResponse<Funcion> response = boletosUseCase.guardarFuncion(funcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response).getBody();
    }

    @Operation(summary = "Obtener funciones")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Función creada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/obtenerFunciones")
    public ApiResponse<Iterable<Funcion>> obtenerFunciones() {
        ApiResponse<Iterable<Funcion>> response = boletosUseCase.obtenerFunciones();
        return ResponseEntity.status(HttpStatus.OK).body(response).getBody();
    }

    @Operation(summary = "Eliminar una función",
            description = "Elimina una función existente dado su código. Retorna 200 si se eliminó correctamente o 404 si no se encontró.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Función eliminada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Función no encontrada")
    })
    @DeleteMapping("/eliminarFuncion")
    public ResponseEntity<String> eliminarFuncion(@RequestParam int codigoFuncion) {
        boolean eliminado = boletosUseCase.eliminarFuncion(String.valueOf(codigoFuncion));

        if (eliminado) {
            return ResponseEntity.ok("La función se eliminó exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la función con el código proporcionado.");
        }
    }

    @GetMapping("/reporte/mas-vendidas")
    @Operation(summary = "Obtener funciones más vendidas", description = "Devuelve una lista de funciones ordenadas por cantidad de boletos vendidos en orden descendente.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de funciones obtenida correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al consultar funciones")
    })
    public ResponseEntity<InputStreamResource> exportarExcel() {
        ByteArrayInputStream excel = boletosUseCase.exportarFuncionesMasVendidasExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=funciones_mas_vendidas.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }

}
