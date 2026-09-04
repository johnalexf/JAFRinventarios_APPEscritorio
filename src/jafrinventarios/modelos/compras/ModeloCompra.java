
package jafrinventarios.modelos.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloCompra {
    
    private Integer idCompra;
    private Date fechaHoraCompra;
    private double totalCompra;
    private Integer idProveedor;
    private Integer idUsuario;
    
    private ArrayList<ModeloDetalleCompra> detalles = new ArrayList<>();

    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */
    
    //Constructor para crear una nueva compra
    public ModeloCompra() {
    }

    //Constructor para recolectar la consulta a la base de datos y poder editar el registro
    public ModeloCompra(    
                    Integer idCompra,
                    Date fechaHoraCompra, 
                    double totalCompra, 
                    Integer idProveedor, 
                    Integer idUsuario) {
        this.idCompra = idCompra;
        this.fechaHoraCompra = fechaHoraCompra;
        this.totalCompra = totalCompra;
        this.idProveedor = idProveedor;
        this.idUsuario = idUsuario;
    }

    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */
    
    public Integer getIdCompra() {
        return idCompra;
    }

    public Date getFechaHoraCompra() {
        return fechaHoraCompra;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public ArrayList<ModeloDetalleCompra> getDetalles() {
        return new ArrayList<>(detalles);
    }
    
    /*
    ============================================================================
                                  SETTERS
    ============================================================================
    */


    public void setFechaHoraCompra(Date fechaHoraCompra) {
        this.fechaHoraCompra = fechaHoraCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setDetalles(ArrayList<ModeloDetalleCompra> detalles) {
        this.detalles = new ArrayList<>(detalles);
    }

    public void agregarDetalle( ModeloDetalleCompra detalle ){
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
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.idCompra);
        hash = 79 * hash + Objects.hashCode(this.fechaHoraCompra);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.totalCompra) ^ (Double.doubleToLongBits(this.totalCompra) >>> 32));
        hash = 79 * hash + Objects.hashCode(this.idProveedor);
        hash = 79 * hash + Objects.hashCode(this.idUsuario);
        hash = 79 * hash + Objects.hashCode(this.detalles);
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
        final ModeloCompra other = (ModeloCompra) obj;
        if (Double.doubleToLongBits(this.totalCompra) != Double.doubleToLongBits(other.totalCompra)) {
            return false;
        }
        if (!Objects.equals(this.idCompra, other.idCompra)) {
            return false;
        }
        if (!Objects.equals(this.fechaHoraCompra, other.fechaHoraCompra)) {
            return false;
        }
        if (!Objects.equals(this.idProveedor, other.idProveedor)) {
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

   
    /*
    ============================================================================
                             MÉTODO PARA CLONAR 
    ============================================================================
    */
    
    public ModeloCompra clonar(){
        
        ArrayList<ModeloDetalleCompra> detallesClonados = new ArrayList<>();
        for (ModeloDetalleCompra detalle : this.detalles) {
            detallesClonados.add(detalle.clonar());
        }
    
        ModeloCompra clonCompra =  new ModeloCompra(
                                            this.idCompra,
                                            this.fechaHoraCompra,
                                            this.totalCompra,
                                            this.idProveedor,
                                            this.idUsuario
                                    );
        
        clonCompra.setDetalles(detallesClonados);
        
        return clonCompra;
        
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
        
        String compraString;
        
        compraString =
                "ModeloCompra\n" + 
                "\nidCompra=" + idCompra + 
                "\nfechaHoraCompra=" + fechaHoraCompra + 
                "\ntotalCompra=" + totalCompra + 
                "\nidProveedor=" + idProveedor + 
                "\nidUsuario=" + idUsuario + 
                "\ndetalles=" + '\n';
        
        for(ModeloDetalleCompra detalle : detalles){
            compraString+= detalle.toString();
        }
        
        return compraString;
    }
    
    
    
    
    
}
