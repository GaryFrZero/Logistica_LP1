package com.logistica.controlador;

import java.sql.*;
import java.util.ArrayList;

import com.logistica.entidad.Trabajador;
import com.logistica.interfaces.TrabajadorDAO;
import com.logistica.utils.MySqlConexion;

public class MySqlTrabajadorDAO implements TrabajadorDAO{

	@Override
	public int Ingresar(Trabajador bean) {
		int salida = -1;
		Connection cn = null;
		PreparedStatement pstm = null;
		String sql = "insert into TB_Trabajadores values (?,?,?,?,?)";
		try {
			cn = MySqlConexion.getConexion();
			pstm = cn.prepareStatement(sql);
			pstm.setInt(1, bean.getDni());
			pstm.setString(2, bean.getNomApe());
			pstm.setString(3, bean.getCargo());
			pstm.setString(4, bean.getFecNac());
			pstm.setDouble(5, bean.getSueldo());
			salida = pstm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error en MySqlTrabajadorDAO");
			e.printStackTrace();
		} finally {
			try {
				if(cn != null) cn.close();
				if(pstm != null)pstm.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return salida;
	}

	@Override
	public int Actualizar(Trabajador bean) {
		int salida = -1;
		
		return salida;
	}

	@Override
	public int Eliminar(Trabajador bean) {
		int salida = -1;
		return salida;
	}

	@Override
	public ArrayList<Trabajador> ListarTodo() {
		ArrayList<Trabajador> lista = new ArrayList<Trabajador>();
		Connection cn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql = "select * from TB_Trabajadores";
		try {
			cn = MySqlConexion.getConexion();
			pstm = cn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				Trabajador tra = new Trabajador();
				tra.setDni(rs.getInt(1));
				tra.setNomApe(rs.getString(2));
				tra.setCargo(rs.getString(3));
				tra.setFecNac(rs.getString(4));
				tra.setSueldo(rs.getDouble(5));
				lista.add(tra);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Problemas en MySqlTrabajadorDao");
		} finally {
			try {
				if(cn!= null) cn.close();
				if(pstm != null)pstm.close();
				if(rs != null)rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return lista;
	}

}
