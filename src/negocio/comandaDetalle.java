/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import datos.Conexion;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class comandaDetalle extends Conexion {
    
  private int nro_comanda;
  private int codigo_producto;
  private int item;
  private int cantidad;
  private double precio;
  private double importe;

    public int getNro_comanda() {
        return nro_comanda;
    }

    public void setNro_comanda(int nro_comanda) {
        this.nro_comanda = nro_comanda;
    }

    public int getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(int codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    
    public ResultSet configurarTablaDetalleCompra() throws Exception {
        String sql = "select * from( select 0 as codigo ,''::character varying(100) producto, 0 as cantidad,0::numeric as precio,0::numeric as importe) as tb_temporal where tb_temporal.codigo <> 0";
        ResultSet resultado = this.ejecutarSQLSelect(sql);
        return resultado;
    }

    

}
