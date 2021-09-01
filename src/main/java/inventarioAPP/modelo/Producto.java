/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventarioAPP.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 *
 * @author Sebastián Beltrán
 */
@Table("Productos")
public class Producto {
    @Id
    @Column("codigo")
    private Long codigo;
    @Column("nombre")
    private String nombre;
    @Column("precio")
    private double precio;
    @Column("inventario")
    private int inventario;

    /**
     * Constructor de objeto Producto por defecto.
     */
    public Producto(){
    }
    
    /**
     * Constructor con todos los parámetros.
     * @param codigo
     * @param nombre
     * @param precio
     * @param inventario 
     */
    private Producto(Long codigo, String nombre, double precio, int inventario){
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.inventario = inventario;
    }
    
    /**
     * Crear objeto de tipo Producto con todos los parámetros.
     * @param codigo
     * @param nombre
     * @param precio
     * @param inventario
     * @return Un objeto Producto con codigo, nombre, precio, inventario.
     */
    public static Producto crearProducto(Long codigo, String nombre, double precio, int inventario){
        return new Producto(codigo, nombre, precio, inventario);
    }
    
    /**
     * Crear objeto de tipo Producto con nombre, precio e inventario.
     * @param nombre
     * @param precio
     * @param inventario
     * @return Un objeto Producto con nombre, precio e inventario.
     */
    public static Producto crearProducto(String nombre, double precio, int inventario){
        return new Producto(null, nombre, precio, inventario);
    }
    
    /**
     * @return the codigo
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * @return the inventario
     */
    public int getInventario() {
        return inventario;
    }

    /**
     * @param inventario the inventario to set
     */
    public void setInventario(int inventario) {
        this.inventario = inventario;
    }
}
