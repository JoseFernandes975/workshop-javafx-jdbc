package model.services;

import java.util.ArrayList;
import java.util.List;

import dao.DaoFactory;
import dao.SellerDao;
import model.entities.Seller;

public class SellerServices {

	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
	   return dao.findAll();
	}
	
	public void saveOrUpdate(Seller Seller) {
		if(Seller.getId() == null) {
			dao.insert(Seller);
		}
		else {
			dao.update(Seller);
		}
	}
	
	public void remove(Seller Seller) {
		dao.deleteById(Seller.getId());
	}
	
}
