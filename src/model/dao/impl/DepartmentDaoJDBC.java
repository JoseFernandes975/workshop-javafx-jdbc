package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.DepartmentDao;
import db.DB;
import db.DbException;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO department "
					+"(Name) "
					+"VALUES "	
					+"(?)",Statement.RETURN_GENERATED_KEYS);
					
					
					st.setString(1, department.getName());
					
					int rowsAffected = st.executeUpdate();
					
					if(rowsAffected>0) {
						ResultSet rs = st.getGeneratedKeys();
					if(rs.next()) {
						int id = rs.getInt(1);
						department.setId(id);
					 }
					DB.closeResultSet(rs);
					}	
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			
		}
	}

	@Override
	public void update(Department department) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			"UPDATE department "
		   + "SET Name = ? " 
		   + "WHERE Id = ?");
			
			st.setString(1, "Food");
			st.setInt(2, 1);
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			"DELETE FROM department "
			+ "WHERE Id = ?");		
					
			st.setInt(1, 6);
			
		    st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM department WHERE Id = ?");
					
				   st.setInt(1, id);
				   
				   rs = st.executeQuery();
				   if(rs.next()) {
					   Department obj = new Department();
					   obj.setId(rs.getInt("Id"));
					   obj.setName(rs.getString("Name"));
					   return obj;
				   }
				   return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM department ORDER BY Name");
			rs = st.executeQuery();
			
		    List<Department> list = new ArrayList<>();
			
			while(rs.next()) {
				Department dp = new Department();
				dp.setId(rs.getInt("Id"));
				dp.setName(rs.getString("Name"));
				list.add(dp);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

}
