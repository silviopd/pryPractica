/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import java.awt.Color;
import java.awt.PopupMenu;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import negocio.producto;
import negocio.cliente;
import negocio.comanda;
import negocio.comandaDetalle;
import negocio.mesa;
import util.Funciones;

/**
 *
 * @author USER
 */
public class frmComanda extends javax.swing.JDialog {

//    private ResultSet resultado;
    public int valorRetorno = 0;

    public frmComanda(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);

        cargarComboMesa();
        tblCompra.setCellSelectionEnabled(true);
        tblCompra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCompra.setRowHeight(25);

        obtenerFecha();
        cargarComboCliente();
        configurarCabeceraDetalleCompra();
    }

    private void obtenerFecha() {
        java.util.Date obj = new java.util.Date();
        txtFecha.setDate(obj);
    }

    private boolean validarDatos() {

        if (cboMesa.getSelectedItem().toString().isEmpty()) {
            Funciones.mensajeAdvertencia("Debe elegir una mesa", "Verificar");
            cboMesa.requestFocus();
            return false;
        } else if (cboCliente.getSelectedItem().toString().isEmpty()) {
            Funciones.mensajeAdvertencia("Debe elegir un cliente", "Verifique");
            cboCliente.requestFocus();
            return false;
        } else if (txtFecha.getDate() == null) {
            Funciones.mensajeAdvertencia("Debe ingresar una fecha", "Verifique");
            txtFecha.requestFocus();
            return false;
        }else if (tblCompra.getRowCount()<=0) {
            Funciones.mensajeAdvertencia("Debe ingresar producto(s)", "Verifique");
            return false;
        }

        int cantidad = 0;
        String producto = "";
        for (int i = 0; i < tblCompra.getRowCount(); i++) {
            cantidad = Integer.parseInt(this.tblCompra.getValueAt(i, 2).toString());
            producto = this.tblCompra.getValueAt(i, 1).toString();
            if (cantidad == 0) {
                Funciones.mensajeAdvertencia("Debe agregar una cantidad al producto " + producto, "Verifique");
                tblCompra.changeSelection(i, 2, false, false);
                tblCompra.requestFocus();
                return false;
            }
        }

        return true;
    }
    
    
    public void calcularTotales() {
        double subtotal=0,total=0;
        for (int i = 0; i < tblCompra.getRowCount(); i++) {
            subtotal = Double.parseDouble(tblCompra.getValueAt(i, 4).toString().replace(",", ""));
            total=subtotal+total;
        }
        lblTotal.setText(String.valueOf(total));
    }
    
    private void configurarCabeceraDetalleCompra() {
        try {
            ResultSet resultado = new comandaDetalle().configurarTablaDetalleCompra();
            int anchoCol[] = {80, 400, 80, 80, 80};
            String alineCol[] = {"C", "I", "D", "D", "D"};
            Funciones.llenarTabla(tblCompra, resultado, anchoCol, alineCol);
        } catch (Exception e) {
            Funciones.mensajeInformacion(e.getMessage(), "ERROR...");
        }
    }

    private void cargarComboMesa() {
        try {
            new mesa().cargarCombo(cboMesa);
        } catch (Exception e) {
            Funciones.mensajeError(e.getMessage(), Funciones.NOMBRE_SOFTWARE);
        }
    }
    
    private void cargarComboCliente() {
        try {
            new cliente().cargarCombo(cboCliente);
        } catch (Exception e) {
            Funciones.mensajeError(e.getMessage(), Funciones.NOMBRE_SOFTWARE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cboCliente = new javax.swing.JComboBox();
        btnQuitar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        /*parte 1*/
        final JTextField field = new JTextField("0");
        final DefaultCellEditor edit = new DefaultCellEditor(field);
        field.setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.red));
        field.setForeground(Color.blue);
        /*parte 1*/
        tblCompra = new javax.swing.JTable(){
            /*parte 2*/
            public boolean isCellEditable(int fila, int columna){
                if (columna == 2){
                    return true;
                }
                return false;
            }

            public TableCellEditor getCellEditor(int row, int col) {
                if (col == 2){
                    field.setDocument(new util.ValidaNumeros());
                }else{
                    field.setDocument(new util.ValidaNumeros(util.ValidaNumeros.ACEPTA_DECIMAL));
                }
                edit.setClickCountToStart(2);
                field.addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        field.select(0,0);
                    }
                });
                return edit;
            }
            /*parte 2*/

        };
        jLabel5 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        cboMesa = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton4.setText("Salir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del proveedor"));

        jLabel8.setText("Seleccione el cliente");

        cboCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(31, 31, 31)
                .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnQuitar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnQuitar.setText("Quitar artículo");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton5.setText("Grabar la compra");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAgregar.setText("Agregar artículo");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Artículos registrados de la compra"));

        tblCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCompra.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblCompraPropertyChange(evt);
            }
        });
        tblCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblCompraKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblCompra);
        /*parte 3*/
        tblCompra.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                field.setText("");
                field.requestFocus();
            }
        });

        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode()==10){
                    if (field.getText().isEmpty()){
                        evt.consume();
                    }
                }
            }
        });
        /*parte 3*/

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Total");

        lblTotal.setEditable(false);
        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 51, 51));
        lblTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lblTotal.setText("0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("N° Prod:");

        jTextField1.setEditable(false);

        cboMesa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Mesa");

        jLabel6.setText("Fecha Venta");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(35, 35, 35)
                .addComponent(jButton4)
                .addGap(331, 331, 331))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAgregar)
                        .addGap(18, 18, 18)
                        .addComponent(btnQuitar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(cboMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(30, 30, 30)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnQuitar)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        
        frmProductoBuscar obj = new frmProductoBuscar(null, true);
        obj.setVisible(true);

        if (obj.valorRetorno > 0) {
            int filasel = obj.tblListado.getSelectedRow();
            
            int codigoProducto = Integer.parseInt(obj.tblListado.getValueAt(filasel, 0).toString());
            String nombreProducto = obj.tblListado.getValueAt(filasel, 1).toString();
            int cantidad = 0;
            double precio = Double.parseDouble(obj.tblListado.getValueAt(filasel, 2).toString());
            double importe = 0;

            DefaultTableModel modelo = (DefaultTableModel) this.tblCompra.getModel();

            Object filaDatos[] = new Object[5];
            filaDatos[0] = codigoProducto;
            filaDatos[1] = nombreProducto;
            filaDatos[2] = cantidad;
            filaDatos[3] = precio;
            filaDatos[4] = importe;

            modelo.addRow(filaDatos);

            tblCompra.setModel(modelo);
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tblCompraPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblCompraPropertyChange
        if (evt.getPropertyName().equalsIgnoreCase("tableCellEditor")) {
            //System.out.println("Editando datos sobre la tabla");
            int columnaEditar = this.tblCompra.getEditingColumn();
            int filaEditar = this.tblCompra.getEditingRow();

            if (columnaEditar == 2) {
                
                int cantidadProducto = Integer.parseInt(this.tblCompra.getValueAt(filaEditar, 2).toString());
                double precioProducto = Double.parseDouble(this.tblCompra.getValueAt(filaEditar, 3).toString().replace(",",""));

                
                
                this.tblCompra.setValueAt(Funciones.formatearNumero(cantidadProducto*precioProducto), filaEditar, 4);
                calcularTotales();
            }
        }
    }//GEN-LAST:event_tblCompraPropertyChange

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        DefaultTableModel tablaDetalle
                = (DefaultTableModel) this.tblCompra.getModel();

        int fila = this.tblCompra.getSelectedRow();

        if (fila < 0) {
            Funciones.mensajeError("Debe seleccionar una fila", "Verifique");
            return;
        }

        String articulo = tblCompra.getValueAt(fila, 1).toString();
        int respuesta = Funciones.mensajeConfirmacion(
                "Esta seguro de quitar el artículo: " + articulo,
                "Confirme"
        );

        if (respuesta != 0) {
            return;
        }

        tablaDetalle.removeRow(fila);
        this.tblCompra.setModel(tablaDetalle);

    }//GEN-LAST:event_btnQuitarActionPerformed

    private void tblCompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCompraKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                btnQuitar.doClick();
                break;

            case KeyEvent.VK_INSERT:
                btnAgregar.doClick();
                break;
        }
    }//GEN-LAST:event_tblCompraKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    }//GEN-LAST:event_formKeyPressed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (validarDatos() == false) {
            return;
        }

        int respuesta = Funciones.mensajeConfirmacion("Desea grabar la compra", "comfirme");
        if (respuesta == 1) {
            return;
        }
        grabarCompra();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void grabarCompra() {
        int item=1;
        try {
            int mesaCod = mesa.listaMesa.get(cboMesa.getSelectedIndex()).getCodigo_mesa();            
            int clienteCod = cliente.listaCliente.get(cboCliente.getSelectedIndex()).getCodigo_cliente();            
            java.sql.Date fecha_compra = new java.sql.Date(this.txtFecha.getDate().getTime());
            int codUsuario = 1;
            double total = Double.parseDouble(lblTotal.getText().replace(",", ""));
            
            comanda objCompra = new comanda();
            objCompra.setCodigo_cliente(clienteCod);
            objCompra.setCodigo_mesa(mesaCod);
            objCompra.setFecha(fecha_compra);
            objCompra.setCodigo_usuario(codUsuario);
            objCompra.setTotal(total);
            
            
            ArrayList<comandaDetalle> comandaDetalle = new ArrayList<comandaDetalle>();
            
            for (int i = 0; i < tblCompra.getRowCount(); i++) {
                int codigo_producto = Integer.parseInt(tblCompra.getValueAt(i, 0).toString());
                int cantidad_sacos = Integer.parseInt(tblCompra.getValueAt(i, 2).toString());
                double precio_pilado = Double.parseDouble(tblCompra.getValueAt(i, 3).toString());
                double importe_pilado = Double.parseDouble(tblCompra.getValueAt(i, 4).toString().replace(",", ""));
                
                comandaDetalle objFila = new comandaDetalle();
                objFila.setCodigo_producto(codigo_producto);
                objFila.setCantidad(cantidad_sacos);
                objFila.setPrecio(precio_pilado);
                objFila.setImporte(importe_pilado);
                objFila.setItem(item);             

                comandaDetalle.add(objFila);
                item++;
            }
            objCompra.setComandaDetalle(comandaDetalle);

            if (objCompra.grabarCompra()) {
                Funciones.mensajeInformacion("Grabado correctamente", "Exito");
                this.valorRetorno = 1;
                this.dispose();
            }

        } catch (Exception e) {
            Funciones.mensajeError(e.getMessage(), "Error");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JComboBox cboCliente;
    private javax.swing.JComboBox cboMesa;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField lblTotal;
    private javax.swing.JTable tblCompra;
    private com.toedter.calendar.JDateChooser txtFecha;
    // End of variables declaration//GEN-END:variables
}
