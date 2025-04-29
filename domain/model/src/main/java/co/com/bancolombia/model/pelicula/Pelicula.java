package co.com.bancolombia.model.pelicula;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Pelicula {
    private String nombrePelicula;
}
