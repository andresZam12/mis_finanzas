package com.ucc.finanzas.service;

import com.ucc.finanzas.model.Menu;
import com.ucc.finanzas.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Retorna el árbol de menús. La recursividad está en la entidad:
 * cada Menu tiene una lista de hijos del mismo tipo, que a su vez
 * pueden tener más hijos (árbol n-ario de profundidad arbitraria).
 */
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> listarMenusRaiz() {
        return menuRepository.findByPadreIsNullOrderByOrdenAsc();
    }
}
