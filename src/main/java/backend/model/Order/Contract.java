package backend.model.Order;

import io.vertx.ext.web.FileUpload;

public class Contract {
	
	private FileUpload contract_main;
	private FileUpload contract_signed;
	private Boolean contract_approved;
	
	public Contract(FileUpload contract_main, FileUpload contract_signed, Boolean contract_approved) {
		this.contract_main = contract_main;
		this.contract_signed = contract_signed;
		this.contract_approved = contract_approved;
	}

	public Boolean getContract_approved() {
		return contract_approved;
	}

	public void setContract_approved(Boolean contract_approved) {
		this.contract_approved = contract_approved;
	}

	public FileUpload getContract_signed() {
		return contract_signed;
	}

	public void setContract_signed(FileUpload contract_signed) {
		this.contract_signed = contract_signed;
	}

	public FileUpload getContract_main() {
		return contract_main;
	}

	public void setContract_main(FileUpload contract_main) {
		this.contract_main = contract_main;
	}

}