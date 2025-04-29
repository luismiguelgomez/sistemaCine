package co.com.bancolombia.model.reponsemicro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private String error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Solicitud exitosa", data, null);
    }

    public static <T> ApiResponse<T> error(int status, String message, String error) {
        return new ApiResponse<>(status, message, null, error);
    }
}
