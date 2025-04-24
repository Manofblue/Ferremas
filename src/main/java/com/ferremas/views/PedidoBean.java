package com.ferremas.views;

import com.ferremas.model.*;
import com.ferremas.service.EmpleadoService;
import com.ferremas.service.EstadopedidoService;
import com.ferremas.service.PedidoService;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named("pedidoBean")
@ViewScoped
public class PedidoBean implements Serializable {

    private List<Pedido> pedidos;
    private List<Pedido> filteredPedidos;
    private Pedido pedidoSeleccionado;
    private List<Detallepedido> detallesPedido;

    private Sucursal sucursal;
    private String estadoFiltro;
    private Estadopedido estadoSeleccionado;
    private List<Estadopedido> estados;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private EstadopedidoService estadopedidoService;
    @Autowired
    private HttpSession session;

    @PostConstruct
    public void init() {
        // Obtener usuario desde la sesión
      
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            var empleado = empleadoService.findById(usuario.getRutUsuario()).orElse(null);
            if (empleado != null) {
                sucursal = empleado.getSucursal();
                  Logger.logInfo("Viendo Pedidos de "+sucursal.getNombre());
            }
        }

        if (sucursal != null) {
            pedidos = pedidoService.obtenerPedidosPorSucursal(sucursal.getIdSucursal());
        } else {
            pedidos = new ArrayList<>();
        }

        estados = estadopedidoService.findAll();
    }

    // === ACCIONES ===

    public void editarEstado(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
        this.estadoSeleccionado = pedido.getEstadopedido();

    }

    public void verDetallesPedido(Pedido pedido) {
        Logger.logInfo("Viendo los datalles del pedido");
        this.pedidoSeleccionado = pedido;
        //this.detallesPedido = pedidoService.obtenerDetallesPorPedido(pedido.getIdPedido()); // Debes tener este método en PedidoService
        this.detallesPedido = pedido.getDetallepedidos();
    }


    public void guardarEstado() {
        Logger.logInfo("Cambiando el estado xdxd");
        if (pedidoSeleccionado != null && estadoSeleccionado != null) {
            Logger.logInfo("Cambiando el estado");
            pedidoSeleccionado.setEstadopedido(estadoSeleccionado);
            pedidoService.guardar(pedidoSeleccionado); // Método a implementar si no existe
        } else {
            Logger.logInfo("Estado nulo");
        }
    }

    // === GETTERS & SETTERS ===


    public Pedido getPedidoSeleccionado() {
        return pedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
        this.pedidoSeleccionado = pedidoSeleccionado;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Pedido> getFilteredPedidos() {
        return filteredPedidos;
    }

    public void setFilteredPedidos(List<Pedido> filteredPedidos) {
        this.filteredPedidos = filteredPedidos;
    }

    public List<Detallepedido> getDetallesPedido() {
        return detallesPedido;
    }

    public void setDetallesPedido(List<Detallepedido> detallesPedido) {
        this.detallesPedido = detallesPedido;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public String getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(String estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public Estadopedido getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(Estadopedido estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public List<Estadopedido> getEstados() {
        return estados;
    }

    public void setEstados(List<Estadopedido> estados) {
        this.estados = estados;
    }



}
