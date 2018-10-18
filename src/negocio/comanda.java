package negocio;

import datos.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class comanda extends Conexion {

    private int nro_comanda;
    private java.sql.Date fecha;
    private int codigo_cliente;
    private int codigo_mesa;
    private int codigo_usuario;
    private double total;

    private ArrayList<comandaDetalle> comandaDetalle = new ArrayList<comandaDetalle>();

    public int getNro_comanda() {
        return nro_comanda;
    }

    public void setNro_comanda(int nro_comanda) {
        this.nro_comanda = nro_comanda;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCodigo_cliente() {
        return codigo_cliente;
    }

    public void setCodigo_cliente(int codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    public int getCodigo_mesa() {
        return codigo_mesa;
    }

    public void setCodigo_mesa(int codigo_mesa) {
        this.codigo_mesa = codigo_mesa;
    }

    public int getCodigo_usuario() {
        return codigo_usuario;
    }

    public void setCodigo_usuario(int codigo_usuario) {
        this.codigo_usuario = codigo_usuario;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<comandaDetalle> getComandaDetalle() {
        return comandaDetalle;
    }

    public void setComandaDetalle(ArrayList<comandaDetalle> comandaDetalle) {
        this.comandaDetalle = comandaDetalle;
    }

    public boolean grabarCompra() throws Exception {
        String sql = "select * from f_generar_correlativo('comanda') as numero;";
        ResultSet resultado = this.ejecutarSQLSelect(sql);

        if (resultado.next()) {
            int nuevoCodigo = resultado.getInt("numero");
            setNro_comanda(nuevoCodigo);

            Connection transaccion = abrirConexion();
            transaccion.setAutoCommit(false);

            sql = "INSERT INTO comanda("
                    + "   nro_comanda, fecha, codigo_cliente, codigo_mesa, codigo_usuario,"
                    + "   total)"
                    + "   VALUES (?, ?, ?, ?, ?, "
                    + "            ?);";
            PreparedStatement sentenciaComanda = transaccion.prepareStatement(sql);
            sentenciaComanda.setInt(1, this.getNro_comanda());
            sentenciaComanda.setDate(2, this.getFecha());
            sentenciaComanda.setInt(3, this.getCodigo_cliente());
            sentenciaComanda.setInt(4, this.getCodigo_mesa());
            sentenciaComanda.setInt(5, this.getCodigo_usuario());
            sentenciaComanda.setDouble(6, this.getTotal());
            this.ejecutarSQL(sentenciaComanda, transaccion);

            for (int i = 0; i < comandaDetalle.size(); i++) {
                comandaDetalle fila = comandaDetalle.get(i);
                sql = "INSERT INTO comanda_detalle("
                        + "            nro_comanda, codigo_producto, item, cantidad, precio, importe)"
                        + "    VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement sentenciaComandaDetalle = transaccion.prepareStatement(sql);
                sentenciaComandaDetalle.setInt(1, this.getNro_comanda());
                sentenciaComandaDetalle.setInt(2, fila.getCodigo_producto());
                sentenciaComandaDetalle.setInt(3, fila.getItem());
                sentenciaComandaDetalle.setInt(4, fila.getCantidad());
                
                String sql2= "select nombre,stock from producto where codigo_producto=?";
                PreparedStatement validar = abrirConexion().prepareStatement(sql2);
                validar.setInt(1, fila.getCodigo_producto());
                ResultSet validacion = this.ejecutarSQLSelectSP(validar);
                
                if (validacion.next()) {
                    if (validacion.getInt("stock")<fila.getCantidad()) {
                    transaccion.close();
                    throw new Exception("Tiene menos stock en el codigo de producto "+validacion.getString("nombre")+" cantidad máxima "+validacion.getInt("stock"));
                    }
                }    
                
                sentenciaComandaDetalle.setDouble(5, fila.getPrecio());
                sentenciaComandaDetalle.setDouble(6, fila.getImporte());
                this.ejecutarSQL(sentenciaComandaDetalle, transaccion);

                sql = "UPDATE producto  SET stock=stock - ? WHERE codigo_producto=?;";
                PreparedStatement sentenciaActualizarProducto = transaccion.prepareStatement(sql);
                sentenciaActualizarProducto.setInt(1, fila.getCantidad());
                sentenciaActualizarProducto.setInt(2, fila.getCodigo_producto());
                this.ejecutarSQL(sentenciaActualizarProducto, transaccion);
            }

            sql = "update correlativo set correlativo = correlativo+1 where tabla='comanda'";
            PreparedStatement actualizarCorrelativo = transaccion.prepareStatement(sql);
            this.ejecutarSQL(actualizarCorrelativo, transaccion);

            transaccion.commit();
            transaccion.close();
        } else {
            throw new Exception("No existe un correlativo para la tabla comanda");
        }
        return true;
    }

    public ResultSet listar() throws Exception {
        String sql = "SELECT "
                + "  comanda.nro_comanda,"
                + "  comanda.fecha,"
                + "  (cliente.apellidos||', '|| cliente.nombres)::varchar as cliente,"
                + "  mesa.descripcion,"
                + "  comanda.total,"
                + "  comanda.estado"
                + "  FROM"
                + "  public.comanda,"
                + "  public.mesa,"
                + "  public.cliente"
                + "  WHERE"
                + "  comanda.codigo_cliente = cliente.codigo_cliente AND"
                + "  comanda.codigo_mesa = mesa.codigo_mesa"
                + "  order by 1;";
        PreparedStatement sentencia = abrirConexion().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet resultado = ejecutarSQLSelectSP(sentencia);
        return resultado;
    }

    public boolean anular(int numeroComanda) throws Exception {
        String sql = "select * from comanda where nro_comanda=?";
        PreparedStatement sentencia = abrirConexion().prepareStatement(sql);
        sentencia.setInt(1, numeroComanda);
        ResultSet resultado = this.ejecutarSQLSelectSP(sentencia);

        if (resultado.next()) {
            if (resultado.getString("estado").equalsIgnoreCase("A")) {
                throw new Exception("La comanda que intenta anular ya ha sido anulado");
            } else {

                Connection transaccion = this.abrirConexion();
                transaccion.setAutoCommit(false);

                sql = "update comanda set estado = 'A' where nro_comanda=?";
                PreparedStatement sentenciaAnular = transaccion.prepareStatement(sql);
                sentenciaAnular.setInt(1, numeroComanda);
                ejecutarSQL(sentenciaAnular, transaccion);

                sql = "select * from comanda_detalle where nro_comanda=?";
                PreparedStatement sentenciaArticuloCompra = this.abrirConexion().prepareStatement(sql);
                sentenciaArticuloCompra.setInt(1, numeroComanda);
                ResultSet resultadoArticuloCompra = ejecutarSQLSelectSP(sentenciaArticuloCompra);

                while (resultadoArticuloCompra.next()) {

                    sql = "UPDATE producto  SET stock=stock + ? WHERE codigo_producto=?;";
                    PreparedStatement sentenciaActualizarProducto = transaccion.prepareStatement(sql);
                    sentenciaActualizarProducto.setInt(1, resultadoArticuloCompra.getInt("cantidad"));
                    sentenciaActualizarProducto.setInt(2, resultadoArticuloCompra.getInt("codigo_producto"));
                    this.ejecutarSQL(sentenciaActualizarProducto, transaccion);
                }

                transaccion.commit();
                transaccion.close();

            }
        } else {
            throw new Exception("No se ha encontrado la comanda que quiere anular");
        }
        return true;
    }
//
//    public ResultSet listarCompraDetalle(int numeroCompra) throws Exception {
//        String sql = " select tb4.nro_pro,tb3.producto,tb4.cantidad_sacos,tb4.cantidad_kilos,tb4.precio_pilado,tb4.importe_pilado,tb3.envase,tb4.cantidad_envases_utilizados,tb4.precio_envase,tb4.importe_envases,tb4.sub_total from"
//                + " (select tb1.codigo as codigo_producto,tb1.nombre as producto,tb1.peso_producto,tb1.precio_pilado,tb1.codigo_envase,tb2.nombre as envase,tb2.precio_envase"
//                + " from"
//                + " (select * from articulo where linea='P') as tb1"
//                + " inner join"
//                + " (select * from articulo where linea='E') as tb2"
//                + " on tb1.codigo_envase=tb2.codigo) as tb3"
//                + " inner join"
//                + " (select * from produccion_detalle) as tb4"
//                + " on tb3.codigo_producto=tb4.codigo_producto and tb3.codigo_envase=tb4.codigo_envase"
//                + " where tb4.nro_pro=?";
//        PreparedStatement sentencia = abrirConexion().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
//        sentencia.setInt(1, numeroCompra);
//
//        ResultSet resultado = ejecutarSQLSelectSP(sentencia);
//        return resultado;
//    }

}
