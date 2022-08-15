package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerServices;

public class SellerFormController implements Initializable {

	private Seller seller;
	private SellerServices sServices;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker txtBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label labelError;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorSalary;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(seller == null) {
			throw new IllegalStateException("Seller was null");
		}
		if(sServices == null) {
			throw new IllegalStateException("sServices was null");
		}
		try {
	    seller = getFormData();
	    sServices.saveOrUpdate(seller);
	    notifyDataChangeListeners();
	    Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
		   setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error", "DbException Save", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException exception = new ValidationException("Validation Error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addErrors("name", "Field can't be empty");
		}
		
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size()>0) {
			throw exception;
		}
		
		return obj;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	public void setSellerServices(SellerServices sServices) {
		this.sServices = sServices;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Utils.formatDatePicker(txtBirthDate, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if(seller == null) {
			throw new IllegalStateException("Seller a null");
		}
		txtId.setText(String.valueOf(seller.getId()));
		txtName.setText(seller.getName());
		txtEmail.setText(seller.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		if(seller.getBirthDate()!=null) {
		txtBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if(fields.contains("name")) {
			labelError.setText(errors.get("name"));
		}
	}
	
}
