package com.logistica.controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.logistica.entidad.Bienes;
import com.logistica.interfaces.BienesDAO;
import com.logistica.utils.MySqlConexion;
import java.sql.CallableStatement;

import lib.Mensajes;

public class MySqlBienesDAO implements BienesDAO {

	@Override
	public int Ingresar(Bienes bean) {
		boolean repetido = false;
		int salida = -1;
		Connection cn = null;
		PreparedStatement pstm = null;
		String sql = "insert into tb_bienes values (?,?,?,?,?,?,CURDATE(),?)";
		ArrayList<Bienes> listaBien = this.ListarTodo();
		for(int i=0; i < listaBien.size();i++) {
			if(bean.getCodBien().equals(listaBien.get(i).getCodBien())) {
				repetido = true;
			}
		}
		if(repetido == false) {
			try {
				cn = MySqlConexion.getConexion();
				pstm = cn.prepareStatement(sql);
				pstm.setString(1, bean.getCodBien());
				pstm.setString(2, bean.getDescBien());
				pstm.setString(3, bean.getUniMed());
				pstm.setDouble(4, bean.getPrecUni());
				pstm.setString(5, bean.getCategoria());
				pstm.setInt(6, bean.getStockDisponible());
				pstm.setInt(7, bean.getStockAlmacen());
				salida = pstm.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (cn != null) cn.close();
					if (pstm != null) pstm.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		}else {
			Mensajes.dialogo("No puede insertar un Codigo de Bien ya existente");
		}
		return salida;
	}

	@Override
	public int Actualizar(Bienes bean) {
		int salida = -1;
		Connection cn = null;
		PreparedStatement pstm = null;
		String sql = "update tb_bienes set descripcion= ?, unidMed=?, precUni=?, stockDisponible=? where codBien=?";
		
		try {
			cn = MySqlConexion.getConexion();
			pstm = cn.prepareStatement(sql);
			pstm.setString(1, bean.getDescBien());
			pstm.setString(2, bean.getUniMed());
			pstm.setDouble(3, bean.getPrecUni());
			pstm.setInt(4, bean.getStockDisponible());
			pstm.setString(5, bean.getCodBien());
			salida = pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (cn != null) cn.close();
				if (pstm != null) pstm.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return salida;
	}

	@Override
	public int Eliminar(Bienes bean) {
		int salida = -1;
		Connection cn = null;
		PreparedStatement pstm = null;
		String sql = "delete from tb_bienes where codBien = ?";
		
		try {
			cn = MySqlConexion.getConexion();
			pstm = cn.prepareStatement(sql);
			pstm.setString(1, bean.getCodBien());
			pstm.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (cn != null) cn.close();
				if (pstm != null) pstm.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return salida;
	}

	@Override
	public ArrayList<Bienes> ListarTodo() {
		ArrayList<Bienes> lista = new ArrayList<Bienes>();
		Connection cn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql = "call sp_listarBienes()";
		
		try {
			cn = MySqlConexion.getConexion();
			pstm = cn.prepareStatement(sql);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				Bienes bie = new Bienes();
				
				bie.setCodBien(rs.getString(1));
				bie.setDescBien(rs.getString(2));
				bie.setUniMed(rs.getString(3));
				bie.setPrecUni(rs.getDouble(4));
				bie.setCategoria(rs.getString(5));
				bie.setStockAlmacen(rs.getInt(6));
				bie.setFecIngreso(rs.getString(7));
				lista.add(bie);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(">>Problemas en bienesDAO");
		}
		finally {
			try {
				if (rs != null) rs.close();
				if (cn != null) cn.close();
				if (pstm != null) pstm.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return lista;
	}

	@Override
	public String[] buscarBienPorCodigo(String codBien) {
		String cadena[] = new String[3];
		Connection cn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql = "select * from tb_bienes where codBien = ?;";
		try {
			cn = MySqlConexion.getConexion();
			pstm = cn.prepareCall(sql);
			pstm.setString(1, codBien);
			rs = pstm.executeQuery();
			if(rs.next()) {
				cadena[0] = rs.getString(1);
				cadena[1] = rs.getString(2);
				cadena[2] = rs.getString(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Hay un problema en MySqlPecosaDAO");
		} finally {
			try {
				if(cn !=null) cn.close();
				if(pstm != null)pstm.close();
				if(rs != null)rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return cadena;
	}

	@Override
	public ArrayList<Bienes> buscarBien(String descr, String comodin, String categoria) {
		ArrayList<Bienes> lista = new ArrayList<Bienes>();
		Connection cn = null;
		CallableStatement pstm = null;
		ResultSet rs = null;
		String sql = "call sp_buscarBien(?,?,?)";
		try {
			cn = MySqlConexion.getConexion();
			pstm = cn.prepareCall(sql);
			pstm.setString(1, descr);
			pstm.setString(2, comodin);
			pstm.setString(3, categoria);
			rs = pstm.executeQuery();
			while(rs.next()) {
				Bienes bien = new Bienes();
				bien.setCodBien(rs.getString(1));
				bien.setDescBien(rs.getString(2));
				bien.setStockAlmacen(rs.getInt(3));
				bien.setCategoria(rs.getString(4));
				lista.add(bien);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Hay un problema en MySqlBienes");
		} finally {
			try {
				if(cn !=null) cn.close();
				if(pstm != null)pstm.close();
				if(rs != null)rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return lista;
	}
	
}
