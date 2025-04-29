package co.com.bancolombia.usecase.boletos;

import co.com.bancolombia.model.DTO.FuncionDTO;
import co.com.bancolombia.model.funcion.Funcion;
import co.com.bancolombia.model.funcion.gateways.FuncionRepository;
import co.com.bancolombia.model.reponsemicro.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
public class BoletosUseCase {
    private final FuncionRepository responseRepository;

    public ApiResponse<Funcion> comprarBoletos(int codigoFuncion, int boletosAcomprar){
        Optional<Funcion> funcionExistente = responseRepository.obtenerFuncionPorId(codigoFuncion);

        Funcion funcion = funcionExistente.orElseThrow(() -> new IllegalStateException("Funcion no encontrada"));

        if (funcion.getAsientosFuncion() <= boletosAcomprar){
            return ApiResponse.error(409, "Compra no posible", "La funcion no tiene esa cantidad de sillas disponibles");
        }

        int asientos = funcion.getAsientosFuncion() - boletosAcomprar;
        int boletasVendidas = funcion.getBoletosVendidos() + boletosAcomprar;

        funcion.setAsientosFuncion(asientos);
        funcion.setBoletosVendidos(boletasVendidas);

        return actualizarFunciones(String.valueOf(codigoFuncion), mapToFuncionDTO(funcion));
    }

    private FuncionDTO mapToFuncionDTO(Funcion funcion) {
        FuncionDTO dto = new FuncionDTO();
        dto.setCodigoBoleto(funcion.getCodigoBoleto());
        dto.setAsientosFuncion(funcion.getAsientosFuncion());
        dto.setBoletosVendidos(funcion.getBoletosVendidos());
        dto.setNombreFuncion(funcion.getNombreFuncion());
        dto.setAnoFuncion(funcion.getAnoFuncion());
        dto.setMesFuncion(funcion.getMesFuncion());
        dto.setDiaFuncion(funcion.getDiaFuncion());
        dto.setAsientosFuncion(funcion.getAsientosFuncion());
        dto.setBoletosVendidos(funcion.getBoletosVendidos());
        dto.setHoraFuncion(funcion.getHoraFuncion());
        return dto;
    }


    public ApiResponse<Funcion> actualizarFunciones(String codigoFuncion, FuncionDTO funcionDTO){
        try {
            Optional<Funcion> funcionExistente = responseRepository.obtenerFuncionPorId(Integer.parseInt(codigoFuncion));

            Funcion funcion = funcionExistente.orElseThrow(() -> new IllegalStateException("Funcion no encontrada"));

            Funcion funcionActualizada = funcionExistente.get().toBuilder()
                    .codigoBoleto(funcionDTO.getCodigoBoleto())
                    .asientosFuncion(funcionDTO.getAsientosFuncion())
                    .anoFuncion(funcionDTO.getAnoFuncion())
                    .mesFuncion(funcionDTO.getMesFuncion())
                    .diaFuncion(funcionDTO.getDiaFuncion())
                    .horaFuncion(funcionDTO.getHoraFuncion())
                    .boletosVendidos(funcionDTO.getBoletosVendidos())
                    .nombreFuncion(funcionDTO.getNombreFuncion())
                    .build();

            Funcion funcionGuardada = responseRepository.actualizarFuncion(funcionActualizada);
            return ApiResponse.success(funcionGuardada);
        } catch (Exception e){
            return ApiResponse.error(500, "Error interno del servidor", e.getMessage());
        }
    }

    public ApiResponse<Iterable<Funcion>> obtenerFunciones() {
        try {
            Iterable<Funcion> funciones = responseRepository.obtenerFunciones();

            if (!funciones.iterator().hasNext()){
                return ApiResponse.error(404, "No hay funciones", "No se encontraron funciones en la base de datos");
            }
            return ApiResponse.success(funciones);
        }catch (Exception e){
            return ApiResponse.error(500, "Error interno del servidor en obtener all", e.getMessage());
        }
    }

    public ApiResponse<Funcion> guardarFuncion(FuncionDTO funcionDTO){
        try {
            Optional<Funcion> funcionExistente = responseRepository.obtenerFuncionPorId(Integer.parseInt(funcionDTO.getCodigoBoleto().toString()));

            if (funcionExistente.isPresent()) {
                return ApiResponse.error(409, "Funcion ya registrada", "La funcion con el codigo " + funcionDTO.getCodigoBoleto() + " ya existe");
            }

            Funcion nuevaFuncion = Funcion.builder()
                    .asientosFuncion(funcionDTO.getAsientosFuncion())
                    .codigoBoleto(funcionDTO.getCodigoBoleto())
                    .asientosFuncion(funcionDTO.getAsientosFuncion())
                    .anoFuncion(funcionDTO.getAnoFuncion())
                    .mesFuncion(funcionDTO.getMesFuncion())
                    .diaFuncion(funcionDTO.getDiaFuncion())
                    .horaFuncion(funcionDTO.getHoraFuncion())
                    .boletosVendidos(funcionDTO.getBoletosVendidos())
                    .nombreFuncion(funcionDTO.getNombreFuncion())
                    .isActive(true)
                    .build();

            Funcion funcionGuardada = responseRepository.guardarFuncion(nuevaFuncion);
            return ApiResponse.success(funcionGuardada);
        } catch (Exception e){
            return ApiResponse.error(500, "Error interno del servidor", e.getMessage());
        }
    }

    public boolean eliminarFuncion(String codigoFuncion) {
        Optional<Funcion> funcionExistente = responseRepository.obtenerFuncionPorId(Integer.parseInt(codigoFuncion));

        Funcion existente = funcionExistente.orElseThrow(() -> new IllegalStateException("Funcion no encontrada"));
        existente.setActive(false);
        responseRepository.eliminarFuncion(existente);

        return true;
    }

    public List<Funcion> obtenerReporteMasVendidas() {
        return responseRepository.obtenerFuncionesMasVendidas();
    }


    public ByteArrayInputStream exportarFuncionesMasVendidasExcel() {
        List<Funcion> funciones = obtenerReporteMasVendidas(); // Ordenadas de mayor a menor

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Funciones Más Vendidas");

            // Cabecera
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Código", "Nombre Función", "Año", "Mes", "Día", "Hora", "Asientos", "Boletos Vendidos"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Datos
            int rowIdx = 1;
            for (Funcion funcion : funciones) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(funcion.getCodigoBoleto());
                row.createCell(1).setCellValue(funcion.getNombreFuncion());
                row.createCell(2).setCellValue(funcion.getAnoFuncion());
                row.createCell(3).setCellValue(funcion.getMesFuncion());
                row.createCell(4).setCellValue(funcion.getDiaFuncion());
                row.createCell(5).setCellValue(funcion.getHoraFuncion());
                row.createCell(6).setCellValue(funcion.getAsientosFuncion());
                row.createCell(7).setCellValue(funcion.getBoletosVendidos());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }


}
