package backend.model.Order;

public class Contract {

	private Number id;
	private String contract_main;
	private String contract_signed;
	private Boolean contract_approved;

	public Contract() {
	}

	public Contract(String contract_main) {
		this.contract_main = contract_main;
	}

	public Contract(String contract_main, String contract_signed) {
//		this.id = id;
		this.contract_main = contract_main;
		this.contract_signed = contract_signed;
	}

	public Contract(String contract_main, String contract_signed, Boolean contract_approved) {
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

}