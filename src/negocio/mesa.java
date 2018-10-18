package negocio;

import datos.Conexion;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JComboBox;

public class mesa extends Conexion{
    
  private int codigo_mesa;
  private String descripcion;
  private String estado;

    
    public static ArrayList<mesa> listaMesa = new ArrayList<mesa>();

    public int getCodigo_mesa() {
        return codigo_mesa;
    }

    public void setCodigo_mesa(int codigo_mesa) {
        this.codigo_mesa = codigo_mesa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public static ArrayList<mesa> getListaTipoComp() {
        return listaMesa;
    }

    public static void setListaTipoComp(ArrayList<mesa> listaTipoComp) {
        mesa.listaMesa = listaTipoComp;
    }

    
    
    
    private void cargarLista() throws Exception {
        String sql = "select * from mesa order by 2";
        ResultSet resultado = this.ejecutarSQLSelect(sql);

        listaMesa.clear();

        while (resultado.next()) {
            mesa obj = new mesa();
            obj.setCodigo_mesa(resultado.getInt("codigo_mesa"));
            obj.setDescripcion(resultado.getString("descripcion"));
            obj.setEstado(resultado.getString("estado"));
            listaMesa.add(obj);
        }
    }
    
    public void cargarCombo(JComboBox objCombo) throws Exception {
        cargarLista();
        objCombo.removeAllItems();
        
        for (mesa listCom : listaMesa) {
            objCombo.addItem(listCom.getDescripcion());
        }
    }
    
}
