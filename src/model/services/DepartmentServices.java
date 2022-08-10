package model.services;

import java.util.ArrayList;
import java.util.List;

import dao.DaoFactory;
import dao.DepartmentDao;
import model.entities.Department;

public class DepartmentServices {

	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
	   return dao.findAll();
	}
	
}
