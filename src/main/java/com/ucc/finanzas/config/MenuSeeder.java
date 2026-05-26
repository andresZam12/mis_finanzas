package com.ucc.finanzas.config;

import com.ucc.finanzas.model.Menu;
import com.ucc.finanzas.repository.MenuRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Puebla la tabla 'menu' con los ítems de navegación al arrancar la app,
 * solo si la tabla está vacía (evita duplicados en reinicios).
 */
@Component
public class MenuSeeder implements CommandLineRunner {

    private final MenuRepository menuRepository;

    public MenuSeeder(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(String... args) {
        if (menuRepository.count() > 0) return;

        // Menú raíz sin hijos
        menuRepository.save(new Menu("Dashboard", "/dashboard", "📊", 1, null));

        // Movimientos — padre sin URL, con 4 hijos
        Menu movimientos = menuRepository.save(new Menu("Movimientos", null, "💸", 2, null));
        menuRepository.save(new Menu("Ingresos",      "/movimientos/ingresos",      "📈", 1, movimientos));
        menuRepository.save(new Menu("Gastos",         "/movimientos/gastos",        "📉", 2, movimientos));
        menuRepository.save(new Menu("Nuevo ingreso",  "/movimientos/nuevo-ingreso", "➕", 3, movimientos));
        menuRepository.save(new Menu("Nuevo gasto",    "/movimientos/nuevo-gasto",   "➖", 4, movimientos));

        // Categorías
        Menu categorias = menuRepository.save(new Menu("Categorías", null, "🏷️", 3, null));
        menuRepository.save(new Menu("Ver categorías",  "/categorias",       "📋", 1, categorias));
        menuRepository.save(new Menu("Nueva categoría", "/categorias/nueva", "➕", 2, categorias));

        // Deudas
        Menu deudas = menuRepository.save(new Menu("Deudas", null, "🤝", 4, null));
        menuRepository.save(new Menu("Mis deudas",  "/deudas",       "📋", 1, deudas));
        menuRepository.save(new Menu("Nueva deuda", "/deudas/nueva", "➕", 2, deudas));

        // Metas
        Menu metas = menuRepository.save(new Menu("Metas", null, "🎯", 5, null));
        menuRepository.save(new Menu("Mis metas",    "/metas",       "📋", 1, metas));
        menuRepository.save(new Menu("Meta del mes", "/metas/mes",   "📅", 2, metas));
        menuRepository.save(new Menu("Nueva meta",   "/metas/nueva", "➕", 3, metas));

        // Ahorros
        Menu ahorros = menuRepository.save(new Menu("Ahorros", null, "💰", 6, null));
        menuRepository.save(new Menu("Mis ahorros",       "/ahorros",       "📊", 1, ahorros));
        menuRepository.save(new Menu("Registrar ahorro",  "/ahorros/nuevo", "➕", 2, ahorros));
    }
}
