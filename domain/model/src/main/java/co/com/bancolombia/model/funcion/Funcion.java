package co.com.bancolombia.model.funcion;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Funcion {
    private Long codigoBoleto;
    private String nombreFuncion;
    private String anoFuncion;
    private String mesFuncion;
    private String diaFuncion;
    private String horaFuncion;
    private int asientosFuncion;
    private int boletosVendidos;
    private boolean isActive;
}
