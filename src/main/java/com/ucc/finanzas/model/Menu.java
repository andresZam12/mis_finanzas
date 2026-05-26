package com.ucc.finanzas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ítem de navegación. Un menú puede tener un padre (submenú) o ser raíz (padre null).
 * La relación padre-hijo es recursiva: cada nodo puede tener N hijos del mismo tipo.
 */
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 200)
    private String url;

    @Column(length = 50)
    private String icono;

    @Column(nullable = false)
    private int orden;

    // @JsonIgnore evita recursion infinita al serializar: el hijo no repite a su padre
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "padre_id")
    private Menu padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("orden ASC")
    private List<Menu> hijos = new ArrayList<>();

    public Menu() {}

    public Menu(String nombre, String url, String icono, int orden, Menu padre) {
        this.nombre = nombre;
        this.url = url;
        this.icono = icono;
        this.orden = orden;
        this.padre = padre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUrl() { return url; }
    public String getIcono() { return icono; }
    public int getOrden() { return orden; }
    public Menu getPadre() { return padre; }
    public List<Menu> getHijos() { return hijos; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUrl(String url) { this.url = url; }
    public void setIcono(String icono) { this.icono = icono; }
    public void setOrden(int orden) { this.orden = orden; }
    public void setPadre(Menu padre) { this.padre = padre; }
    public void setHijos(List<Menu> hijos) { this.hijos = hijos; }
}
