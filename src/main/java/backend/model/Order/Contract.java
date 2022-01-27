package backend.model.Order;

import io.vertx.ext.web.FileUpload;

public class Contract {

	private Number id;
	private String contract_main;
	private String contract_main_mime;
	private String contract_signed;
	private String contract_signed_mime;
	private Boolean contract_approved;

	public Contract() {
	}

	public Contract(String contract_main, String contract_main_mime) {
		this.contract_main = contract_main;
		this.contract_main_mime = contract_main_mime;
	}

	public Contract(String contract_main, String contract_main_mime, String contract_signed,
			String contract_signed_mime) {
//		this.id = id;
		this.contract_main = contract_main;
		this.contract_signed = contract_signed;
		this.contract_main_mime = contract_main_mime;
		this.contract_signed_mime = contract_signed_mime;
	}

	public Contract(String contract_main, String contract_main_mime, String contract_signed,
			String contract_signed_mime, Boolean contract_approved) {
//		this.id = id;
		this.contract_main = contract_main;
		this.contract_signed = contract_signed;
		this.contract_approved = contract_approved;
		this.contract_main_mime = contract_main_mime;
		this.contract_signed_mime = contract_signed_mime;
	}

	public Boolean getContract_approved() {
		return contract_approved;
	}

	public void setContract_approved(Boolean contract_approved) {
		this.contract_approved = contract_approved;
	}

	public String getContract_signed() {
		return contract_signed;
	}

	public void setContract_signed(String contract_signed) {
		this.contract_signed = contract_signed;
	}

	public String getContract_main() {
		return contract_main;
	}

	public void setContract_main(String contract_main) {
		this.contract_main = contract_main;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public String getContract_signed_mime() {
		return contract_signed_mime;
	}

	public void setContract_signed_mime(String contract_signed_mime) {
		this.contract_signed_mime = contract_signed_mime;
	}

	public String getContract_main_mime() {
		return contract_main_mime;
	}

	public void setContract_main_mime(String contract_main_mime) {
		this.contract_main_mime = contract_main_mime;
	}

}