package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    // Retorna solo los nodos raíz (sin padre), ordenados por 'orden'
    List<Menu> findByPadreIsNullOrderByOrdenAsc();
}
