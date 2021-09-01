/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventarioAPP.controlador;


import inventarioAPP.modelo.Producto;
import inventarioAPP.modelo.RepositorioProducto;
import inventarioAPP.vista.Vista;
import inventarioAPP.vista.VistaActualizar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sebastián Beltrán
 */
public class ControladorProducto implements ActionListener {
    /**
     * Instancia de clase RepositorioProducto.
     */
    private RepositorioProducto repositorio;
    /**
     * Instancia de clase Vista.
     */
    private Vista vista;
    /**
     * Instancia de clase VistaActualizar.
     */
    private VistaActualizar vistaActualizar;
    
    /**
     * Controlador por defecto.
     */
    public ControladorProducto(){
        super();
    }
    
    /**
     * Construir controlador con repositorio y vista.
     * @param repositorio
     * @param vista 
     * @param vistaActualizar
     */
    public ControladorProducto(RepositorioProducto repositorio, Vista vista, VistaActualizar vistaActualizar){
        super();
        this.repositorio = repositorio;
        this.vista = vista;
        this.vistaActualizar = vistaActualizar;
        agregarEventos();
        cargarProductos();
    }
    
    /**
     * Añade ActionListeners a los botones recuperados de la vista.
     */
    public void agregarEventos(){
        vista.getBtnAgregarProducto().addActionListener(this);
        vista.getBtnBorrarProducto().addActionListener(this);
        vista.getBtnActualizar().addActionListener(this);
        vista.getBtnInformes().addActionListener(this);
        vistaActualizar.getBtnActualizarProducto().addActionListener(this);
    }
    
    /**
     * Carga los productos del inventario en la tabla de la vista.
     */
    public void cargarProductos(){
        /**
         * Recuperar objeto JTable de la vista.
         */
        JTable tablaProductos = vista.getTableProductos();
        /**
         * Traer todos los registros de la BD y guardarlos en una lista.
         */
        List<Producto> listaProductos = (List<Producto>) repositorio.findAll();
        /**
         * Ajustar el número de filas de la tabla de acuerdo con el número de
         * elementos de la BD.
         */
        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.setRowCount(listaProductos.size());
        /**
         * Contador para determinar en qué fila agregar los productos.
         */
        int contador = 0;
        /**
         * Agregar Productos de la base de datos en la tabla.
         */
        for (Producto p: listaProductos){
            tablaProductos.setValueAt(p.getNombre(), contador, 0);
            tablaProductos.setValueAt(p.getPrecio(), contador, 1);
            tablaProductos.setValueAt(p.getInventario(), contador, 2);
            contador++;
        }
    }
    
    /**
     * Decide qué acción realizar de acuerdo al botón clickeado.
     * @param e evento "escuchado"
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == vista.getBtnAgregarProducto()){
            agregarClic(e);
        }
        
        if (e.getSource() == vista.getBtnBorrarProducto()){
            borrarClic(e);
        }
        
        if (e.getSource() == vista.getBtnActualizar()){
            actualizarClic(e, 0);
        }
        
        if (e.getSource() == vista.getBtnInformes()){
            informesClic(e);
        }
        
        if (e.getSource() == vistaActualizar.getBtnActualizarProducto()){
            actualizarClic(e, 1);
        }
    }
    
    /**
     * Añade un nuevo Producto a la BD del inventario.
     * @param e 
     */
    public void agregarClic(ActionEvent e){
        /**
         * Recuperar objeto JTextField que recibe el nombre del producto.
         */
        JTextField txtNombre = vista.getTxtNombre();
        /**
         * Recuperar objeto JTextField que recibe el precio del producto.
         */
        JTextField txtPrecio = vista.getTxtPrecio();
        /**
         * Recuperar objeto JTextField que recibe el inventario del producto.
         */
        JTextField txtInventario = vista.getTxtInventario();
        
        /**
         * Confirmar que todos los campos tengan información; lanzar alerta si
         * no es así.
         */
        if (txtNombre.getText().isEmpty()
            || txtPrecio.getText().isEmpty()
            || txtInventario.getText().isEmpty()){
            vista.imprimirAlertaCampos();
        } else {
            /**
             * Obtener los valores de los JTextFields.
             */
            String nombreProducto = txtNombre.getText();
            double precioProducto = Double.parseDouble(txtPrecio.getText());
            int invProducto = Integer.parseInt(txtInventario.getText());
            
            /**
             * Revisar si el producto que se quiere agregar ya existe en la BD;
             * si es así, lanzar advertencia.
             */
            Long idProducto = obtenerIdProducto(nombreProducto);
            if (idProducto != null){
               vista.imprimirAlertaExistencia(true);
               /**
                * Limpiar los campos de texto para recibir nueva información.
                */
               txtNombre.setText(null);
               txtPrecio.setText(null);
               txtInventario.setText(null);
            } else {
                /**
                 * Crear nuevo Producto con los valores recuperados.
                 */
                Producto nuevoProducto = Producto.crearProducto(nombreProducto, precioProducto, invProducto);

                /**
                 * Añadir nuevo producto al inventario.
                 */
                repositorio.save(nuevoProducto);

                /**
                 * Notificar al usuario del éxito de la operación.
                 */
                vista.imprimirMensajeExito("Agregar");

                /**
                 * Limpiar los campos de texto para recibir nueva información.
                 */
                txtNombre.setText(null);
                txtPrecio.setText(null);
                txtInventario.setText(null);

                /**
                 * Actualizar los productos disponibles en la tabla.
                 */
                cargarProductos();
            }
        }
    }
    
    /**
     * Borrar el elemento seleccionado de la BD.
     * @param e 
     */
    public void borrarClic(ActionEvent e){
        /**
         * Recuperar objeto JTable de la vista.
         */
        JTable tablaProductos = vista.getTableProductos();
        /**
         * Obtener índice de fila seleccionada.
         */
        int indexFila = tablaProductos.getSelectedRow();
        
        /**
         * Validar que se haya seleccionado una fila.
         */
        if (indexFila == -1){
            vista.imprimirAlertaSeleccion();
        } else {
            /**
             * Recuperar nombre de producto de la fila seleccionada.
             */
            String nombreProducto = tablaProductos.getValueAt(indexFila, 0).toString();
            /**
             * Obtener ID de producto.
             */
            Long idProducto = obtenerIdProducto(nombreProducto);
            
            /**
             * Borrar producto de la BD.
             */
            repositorio.deleteById(idProducto);
            
            /**
             * Imprimir mensaje de operación exitosa.
             */
            vista.imprimirMensajeExito("Borrar");
            
            /**
             * Actualizar los productos disponibles en la tabla.
             */
            cargarProductos();
        }
    }
    
    /**
     * Actualizar los valores de un producto en la BD.
     * @param e 
     * @param opcion determina la acción a realizar.
     */
    public void actualizarClic(ActionEvent e, int opcion){
        /**
         * Si opcion == 0, mostar la ventana pop-up solamente;
         * si opcion == 1, ejecutar proceso de actualización.
         */
        if (opcion == 0){
            vistaActualizar.setVisible(true);
        } else if (opcion == 1){
            /**
             * Recuperar JTextField con el valor del nombre del producto.
             */
            JTextField txtNombreActu = vistaActualizar.getTxtNombreActu();
            /**
             * Recuperar JTextField con el valor del precio del producto.
             */
            JTextField txtPrecioActu = vistaActualizar.getTxtPrecioActu();
            /**
             * Recuperar JTextField con el valor del inventario del producto.
             */
            JTextField txtInventarioActu = vistaActualizar.getTxtInventarioActu();
            /**
             * Confirmar que todos los campos tengan información; lanzar alerta si
             * no es así.
             */
            if (txtNombreActu.getText().isEmpty()
                || txtPrecioActu.getText().isEmpty()
                || txtInventarioActu.getText().isEmpty()){
                vista.imprimirAlertaCampos();
            } else {
                /**
                 * Obtener los valores de los JTextFields.
                 */
                String nombreProducto = txtNombreActu.getText();
                double precioActualizado = Double.parseDouble(txtPrecioActu.getText());
                int invActualizado = Integer.parseInt(txtInventarioActu.getText());

                /**
                 * Obtener el ID del producto que se actualizará; lanzar
                 * advertencia si no se encuentra el producto en la BD.
                 */
                Long idProducto = obtenerIdProducto(nombreProducto);
                if (idProducto == null){
                    vista.imprimirAlertaExistencia(false);
                    /**
                    * Limpiar los campos de texto para recibir nueva información.
                    */
                    txtNombreActu.setText(null);
                    txtPrecioActu.setText(null);
                    txtInventarioActu.setText(null);

                } else {
                    /**
                     * Recuperar el producto que se actualizará.
                     */
                    Producto productoRecuperado = repositorio.findById(idProducto).get();
                    /**
                     * Actualizar valores del producto.
                     */
                    productoRecuperado.setPrecio(precioActualizado);
                    productoRecuperado.setInventario(invActualizado);
                    /**
                     * Guardar valores actualizados en la BD.
                     */
                    repositorio.save(productoRecuperado);
                    /**
                     * Notificar al usuario que la operación fue exitosa.
                     */
                    vista.imprimirMensajeExito("Actualizar");
                    /**
                    * Limpiar los campos de texto para recibir nueva información.
                    */
                    txtNombreActu.setText(null);
                    txtPrecioActu.setText(null);
                    txtInventarioActu.setText(null);
                    /**
                     * Cerrar ventana pop-up.
                     */
                    vistaActualizar.dispose();
                    /**
                     * Actualizar los productos disponibles en la tabla.
                     */
                    cargarProductos();
                }
            }
        }
    }
    
    /**
     * Obtener informes del inventario
     * @param e 
     */
    public void informesClic(ActionEvent e){
        /**
         * Variable que guarda el resultado de obtenerMayor().
         */
        String prodMayor = obtenerMayor();
        /**
         * Variable que guarda el resultado de obtenerMenor().
         */
        String prodMenor = obtenerMenor();
        /**
         * Variable que guarda el resultado de obtenerPromedio().
         */
        double promPrecios = obtenerPromedio();
        /**
         * Variable que guarda el resultado de obtenerTotalInventario().
         */
        double totalInventario = obtenerTotalInventario();
        
        /**
         * Imprimir el informe en una ventana emergente.
         */
        vista.imprimirInforme(prodMayor, prodMenor,
                String.format("%.1f", promPrecios),
                String.format("%.1f", totalInventario));
    }
    
    /**
     * Busca el producto con el precio mayor en el inventario.
     * @return nombre del producto con precio mayor en el inventario. 
     */
    public String obtenerMayor(){
        /**
         * Variable que controlará el precio mayor mientras se itera en la lista
         * de productos.
         */
        double precioMayor = 0.0;
        /**
         * Variable que controlará el nombre del precio mayor mientras se itera
         * en la lista de productos.
         */
        String nombreMayor = null;
        
        /**
         * Traer todos los registros de la BD y guardarlos en una lista.
         */
        List<Producto> listaProductos = (List<Producto>) repositorio.findAll();
        
        /**
         * Iterar en la lista para encontrar el producto de precio mayor.
         */
        for (Producto p: listaProductos){            
            if (p.getPrecio() > precioMayor){
                precioMayor = p.getPrecio();
                nombreMayor = p.getNombre();
            }
        }
        
        return nombreMayor;
    }
    
    /**
     * Busca el producto con el precio menor en el inventario.
     * @return nombre del producto con precio menor en el inventario.  
     */
    public String obtenerMenor(){
        /**
         * Variable que controlará el precio menor mientras se itera en la lista
         * de productos.
         */
        double precioMenor = 0.0;
        /**
         * Variable que controlará el nombre del precio menor mientras se itera
         * en la lista de productos.
         */
        String nombreMenor = null;
        
        /**
         * Traer todos los registros de la BD y guardarlos en una lista.
         */
        List<Producto> listaProductos = (List<Producto>) repositorio.findAll();
        
        /**
         * Iterar en la lista para encontrar el producto de precio menor.
         */
        for (Producto p: listaProductos){            
            if (precioMenor == 0.0){
                precioMenor = p.getPrecio();
                nombreMenor = p.getNombre();
            } else if (p.getPrecio() < precioMenor){
                precioMenor = p.getPrecio();
                nombreMenor = p.getNombre();
            }
        }
        
        return nombreMenor;
    }
    
    /**
     * Obtiene el promedio de los precios del inventario.
     * @return promedio de los precios del inventario.
     */
    public double obtenerPromedio(){
        /**
         * Variable que acumulará el total de la suma de los precios.
         */
        double totalPrecios = 0.0;
        
        /**
         * Traer todos los registros de la BD y guardarlos en una lista.
         */
        List<Producto> listaProductos = (List<Producto>) repositorio.findAll();
        
        /**
         * Iterar en la lista para sumar todos los precios.
         */
        for (Producto p: listaProductos){
            totalPrecios += p.getPrecio();
        }
        
        return totalPrecios / listaProductos.size();
    }
    
    /**
     * Obtiene el total valor del inventario.
     * @return valor total del inventario.
     */
    public double obtenerTotalInventario(){
        /**
         * Variable que acumulará el total de la suma de los precios por los
         * inventarios.
         */
        double totalInventario = 0.0;
        
        /**
         * Traer todos los registros de la BD y guardarlos en una lista.
         */
        List<Producto> listaProductos = (List<Producto>) repositorio.findAll();
        
        /**
         * Iterar en la lista para sumar todos los precios multiplicados por
         * los inventarios.
         */
        for (Producto p: listaProductos){
            totalInventario += p.getPrecio() * p.getInventario();
        }
        
        return totalInventario;
    }
    
    public Long obtenerIdProducto(String nombreProducto){
        /**
         * Variable que guardará el ID del producto.
         */
        Long idProducto = null;
        /**
         * Traer todos los registros de la BD y guardarlos en una lista.
         */        
        List <Producto> listaProductos = (List <Producto>) repositorio.findAll();
        /**
         * Iterar en la lista de productos para encontrar el código del producto.
         */
        for (Producto p: listaProductos){
            if (p.getNombre().equals(nombreProducto)){
                idProducto = p.getCodigo();
            }
        }
        
        return idProducto;
    }
}
