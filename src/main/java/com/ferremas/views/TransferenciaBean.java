package com.ferremas.views;

import com.ferremas.model.Detallepedido;
import com.ferremas.model.Pedido;
import com.ferremas.model.Transaccion;
import com.ferremas.model.Transferencia;
import com.ferremas.service.EstadopedidoService;
import com.ferremas.service.EstadotransferenciaService;
import com.ferremas.service.PedidoService;
import com.ferremas.service.TransferenciaService;
import com.ferremas.util.Logger;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named("transferenciaBean")
@ViewScoped
public class TransferenciaBean implements Serializable {

    private Transferencia transferencia = new Transferencia();

    private UploadedFile file;

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private EstadotransferenciaService estadotransferenciaService;

    @Autowired
    private EstadopedidoService estadopedidoService;
    @Autowired
    private PedidoService pedidoService;
    @Inject
    private CarritoBean carritoBean;

    public String guardarTransferencia() {
        try {
            // Set datos adicionales
            Pedido pedidoGuardado = guardarPedidoDesdeCarrito();

            var estadoTransa=estadotransferenciaService.findById(1).get();
            transferencia.setFecha(new Date());
            transferencia.setEstadotransferencia(estadoTransa);
            transferencia.setPedido(pedidoGuardado); // Asociar al pedido actual

            if (file != null) {
                transferencia.setComprobante(file.getFileName()); // o guarda como base64

                try {
                    InputStream in = file.getInputStream();
                    String nombre = file.getFileName();
                    Path rutaBase = Paths.get("src", "main", "webapp", "resources", "images", "transa");
                    Files.createDirectories(rutaBase); // Asegura que los directorios existen
                    upload(nombre, rutaBase.toString(), in); // O ajusta `upload` para recibir un Path si es posible
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // aquí puedes subir el archivo a disco o base de datos
            }

            transferenciaService.save(transferencia); // guarda en DB

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Transferencia registrada exitosamente, Recibira un notificacion al correo de confirmacion"));

            transferencia = new Transferencia(); // Reset para nuevo ingreso
        } catch (Exception e) {
            Logger.logInfo(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la transferencia"));
        }


        return null;
    }

    private Pedido guardarPedidoDesdeCarrito() {
        Pedido pedidoCarrito = carritoBean.getPedido();
        Pedido nuevoPedido = new Pedido();

        nuevoPedido.setRutcliente(pedidoCarrito.getRutcliente());
        nuevoPedido.setFecha(new Date());
        nuevoPedido.setSucursal(pedidoCarrito.getSucursal());
        nuevoPedido.setEstadopedido(estadopedidoService.findById(2)
                .orElseThrow(() -> new IllegalArgumentException("EstadoPedido no encontrado")));
        nuevoPedido.setEstado("espera");

        List<Detallepedido> nuevosDetalles = new ArrayList<>();
        for (Detallepedido detalle : pedidoCarrito.getDetallepedidos()) {
            Detallepedido nuevoDetalle = new Detallepedido();
            nuevoDetalle.setCantidad(detalle.getCantidad());
            nuevoDetalle.setProducto(detalle.getProducto());
            nuevoDetalle.setPedido(nuevoPedido);
            nuevosDetalles.add(nuevoDetalle);
        }

        nuevoPedido.setDetallepedidos(nuevosDetalles);
        nuevoPedido.setTotal(pedidoCarrito.getTotal());

        return pedidoService.guardar(nuevoPedido);
    }



    public void upload(String fileName, String destination, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(destination + File.separator + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            Logger.logInfo("Archivo creeado en: "+destination + File.separator + fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    // Getters y Setters

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }
}
