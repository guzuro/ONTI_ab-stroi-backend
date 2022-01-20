package backend.model.Order;

import io.vertx.ext.web.FileUpload;

public class Contract {
	
	private Number id;
	private byte[] contract_main;
	private byte[] contract_signed;
	private Boolean contract_approved;
	
	
	public Contract() {
	}
	
	public Contract(byte[]  contract_main) {
		this.contract_main = contract_main;
	}
	
	public Contract(byte[]  contract_main, byte[]  contract_signed) {
//		this.id = id;
		this.contract_main = contract_main;
		this.contract_signed = contract_signed;
	}
	
	public Contract(byte[]  contract_main, byte[]  contract_signed, Boolean contract_approved) {
//		this.id = id;
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

	public byte[]  getContract_signed() {
		return contract_signed;
	}

	public void setContract_signed(byte[]  contract_signed) {
		this.contract_signed = contract_signed;
	}

	public byte[] getContract_main() {
		return contract_main;
	}

	public void setContract_main(byte[]  contract_main) {
		this.contract_main = contract_main;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

}