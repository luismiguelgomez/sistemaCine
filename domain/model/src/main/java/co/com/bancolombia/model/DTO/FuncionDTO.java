package co.com.bancolombia.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionDTO {
    private Long codigoBoleto;
    private String nombreFuncion;
    private String anoFuncion;
    private String mesFuncion;
    private String diaFuncion;
    private String horaFuncion;
    private int asientosFuncion;
    private int boletosVendidos;
}
