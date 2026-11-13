
package com.uriel.grueact3t4.dto;

import java.math.BigDecimal;

public record ProductoDTO(
    Long id,
    String nombre,
    BigDecimal precio,
    Long categoriaId,
    String categoriaNombre
) {
}
