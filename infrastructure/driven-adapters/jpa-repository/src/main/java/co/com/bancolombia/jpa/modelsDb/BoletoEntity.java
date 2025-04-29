package co.com.bancolombia.jpa.modelsDb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Boleto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoletoEntity {
    @Id
    private Long codigoBoleto;
    private String nombreFuncion;
    private String anoFuncion;
    private String mesFuncion;
    private String diaFuncion;
    private String horaFuncion;
    private int asientosFuncion;
    private int boletosVendidos;
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;
}