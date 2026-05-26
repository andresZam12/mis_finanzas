package com.ucc.finanzas.controller;

import com.ucc.finanzas.model.Menu;
import com.ucc.finanzas.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<List<Menu>> listar() {
        return ResponseEntity.ok(menuService.listarMenusRaiz());
    }
}
