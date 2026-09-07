
package jafrinventarios.modelos.ventas;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloVenta {
    
    private Integer idVenta;
    private Date fechaHoraVenta;
    private double totalVenta;
    private Integer idCliente;
    private Integer idUsuario;
    
    private ArrayList<ModeloDetalleVenta> detalles = new ArrayList<>();
    
    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */

    public ModeloVenta() {
    }

    public ModeloVenta(Integer idVenta, Date fechaHoraVenta, double totalVenta, Integer idCliente, Integer idUsuario) {
        this.idVenta = idVenta;
        this.fechaHoraVenta = fechaHoraVenta;
        this.totalVenta = totalVenta;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
    }
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public Integer getIdVenta() {
        return idVenta;
    }

    public Date getFechaHoraVenta() {
        return fechaHoraVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public ArrayList<ModeloDetalleVenta> getDetalles() {
        return new ArrayList<>(detalles);
    }
    
    /*
    ============================================================================
                                  SETTERS
    ============================================================================
    */

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public void setFechaHoraVenta(Date fechaHoraVenta) {
        this.fechaHoraVenta = fechaHoraVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setDetalles(ArrayList<ModeloDetalleVenta> detalles) {
        this.detalles = new ArrayList<>(detalles);
    }
    
    public void agregarDetalle( ModeloDetalleVenta detalle ){
        this.detalles.add( detalle.clonar() );
    }
    
    /*
    ============================================================================
                                EQUALS Y HASCHCODE
    ============================================================================
    */
    
    /*
    El hashCode() es la huella digital numérica del objeto creado
    Si dos objetos son iguales según el método .equals(), entonces 
    obligatoriamente deben devolver el mismo número en .hashCode()
    */

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.idVenta);
        hash = 29 * hash + Objects.hashCode(this.fechaHoraVenta);
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.totalVenta) ^ (Double.doubleToLongBits(this.totalVenta) >>> 32));
        hash = 29 * hash + Objects.hashCode(this.idCliente);
        hash = 29 * hash + Objects.hashCode(this.idUsuario);
        hash = 29 * hash + Objects.hashCode(this.detalles);
        return hash;
    }

    /*
    equals se encarga de comparar el objeto creado con otro objeto, se espera
    que se comparen dos del tipo ModeloCompra para verificar si hubo un cambio
    en sus campos, antes de guardar por medio del servicio los cambios.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModeloVenta other = (ModeloVenta) obj;
        if (Double.doubleToLongBits(this.totalVenta) != Double.doubleToLongBits(other.totalVenta)) {
            return false;
        }
        if (!Objects.equals(this.idVenta, other.idVenta)) {
            return false;
        }
        if (!Objects.equals(this.fechaHoraVenta, other.fechaHoraVenta)) {
            return false;
        }
        if (!Objects.equals(this.idCliente, other.idCliente)) {
            return false;
        }
        if (!Objects.equals(this.idUsuario, other.idUsuario)) {
            return false;
        }
        if (!Objects.equals(this.detalles, other.detalles)) {
            return false;
        }
        return true;
    }
    
    
    public boolean sonIgualesDatosGenerales ( ModeloVenta ventaAVerificar ){
    
        if(!Objects.equals(this.fechaHoraVenta, ventaAVerificar.fechaHoraVenta))
            return false;
        
        if(!Objects.equals(this.idCliente, ventaAVerificar.idCliente))
            return false;
        
        if(!Objects.equals(this.idUsuario, ventaAVerificar.idUsuario))
            return false;
        
        if(Double.doubleToLongBits(this.totalVenta) != Double.doubleToLongBits(ventaAVerificar.totalVenta))
            return false;
        
        return true;
        
    }
    
    public boolean sonIgualesDetalles ( ModeloVenta ventaAVerficar ){
    
        return ( Objects.equals(this.detalles, ventaAVerficar.detalles));
        
    }
    
    /*
    ============================================================================
                             MÉTODO PARA CLONAR 
    ============================================================================
    */
    
    public ModeloVenta clonar(){
        
        ArrayList<ModeloDetalleVenta> detallesClonados = new ArrayList<>();
        for (ModeloDetalleVenta detalle: this.detalles){
            detallesClonados.add(detalle.clonar());
        }
        
        ModeloVenta clonVenta =  new ModeloVenta(
                                        this.idVenta, 
                                        this.fechaHoraVenta, 
                                        this.totalVenta, 
                                        this.idCliente, 
                                        this.idUsuario
        );
        
        clonVenta.setDetalles(detallesClonados);
        
        return clonVenta;
    
    }
    
    /*
    ============================================================================
                MÉTODO OBTENER UN STRING CON TODOS LOS DATOS
    ============================================================================
    Se usara para cuando se necesite imprimir en consola y verificar que 
    los datos almacenados corresponden a los esperados.
    */

    @Override
    public String toString() {
        
        String ventaString;
        
        ventaString =  "ModeloVenta" + 
                        "\nidVenta=" + idVenta + 
                        "\nfechaHoraVenta=" + fechaHoraVenta + 
                        "\ntotalVenta=" + totalVenta + 
                        "\nidCliente=" + idCliente + 
                        "\nidUsuario=" + idUsuario + 
                        "\ndetalles=" + detalles + 
                        '\n';
        
        for( ModeloDetalleVenta detalle: detalles){
            ventaString+= detalle.toString();
        }
        
        return ventaString;
    }
    
    
    
}
