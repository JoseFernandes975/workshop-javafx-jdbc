package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentServices;

public class DepartmentFormController implements Initializable {

	private Department department;
	private DepartmentServices dpServices;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelError;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(department == null) {
			throw new IllegalStateException("Department was null");
		}
		if(dpServices == null) {
			throw new IllegalStateException("dpServices was null");
		}
		try {
	    department = getFormData();
	    dpServices.saveOrUpdate(department);
	    Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error", "DbException Save", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Department getFormData() {
		Department obj = new Department();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		return obj;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public void setDepartmentServices(DepartmentServices dpServices) {
		this.dpServices = dpServices;
	}
	
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() {
		if(department == null) {
			throw new IllegalStateException("Department a null");
		}
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());
	}

}
