package org.cristian.webapp.jsf3.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cristian.webapp.jsf3.entities.Categoria;
import org.cristian.webapp.jsf3.entities.Producto;
import org.cristian.webapp.jsf3.services.ProductoService;

import java.util.List;

@Model
public class ProductoController {

    private Producto producto;
    private Long id;

    @Inject
    private ProductoService service;

    @Inject
    @Named("customFacesContext")
    private FacesContext facesContext;

    @Produces
    @Model
    public String titulo() {
        return "Hola mundo JavaServer Faces 3.0";
    }

    @Produces
    @RequestScoped
    @Named("listado")
    public List<Producto> findAll() {
        return service.listar();
    }

    @Produces
    @Model
    public List<Categoria> categorias() {
        return service.listarCategorias();
    }

    @Produces
    @Model
    public Producto producto() {
        this.producto = new Producto();
        if (id != null && id > 0) {
            service.porId(id).ifPresent(p -> {
                this.producto = p;
            });
        }
        return producto;
    }

    public String guardar() {
        System.out.println(producto);
        service.guardar(producto);
        if(producto.getId() != null && producto.getId()>0){
            facesContext.addMessage(null, new FacesMessage("Producto "+ producto.getNombre() + " ACTUALIZADO con éxito!"));
        }else {
            facesContext.addMessage(null, new FacesMessage("Producto "+ producto.getNombre() + " CREADO con éxito!"));
        }
        return "index.xhtml?faces-redirect=true";
    }

    public String editar(Long id) {
        this.id = id;
        return "form.xhtml";
    }

    public String eliminar(Producto producto) {
        service.eliminar(producto.getId());
        facesContext.addMessage(null, new FacesMessage("Producto "+ producto.getNombre() + " ELIMINADO con éxito!"));
        return "index.xhtml?faces-redirect=true";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
