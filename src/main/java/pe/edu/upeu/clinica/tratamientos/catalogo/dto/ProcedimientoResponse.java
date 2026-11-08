package pe.edu.upeu.clinica.tratamientos.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedimientoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long idEspecialidad;
    private String nombre;
    private BigDecimal precio;
}
