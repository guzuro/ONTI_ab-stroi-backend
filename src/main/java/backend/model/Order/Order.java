package backend.model.Order;

import java.time.LocalDateTime;
import java.util.List;

import backend.model.User.Client;

public class Order {

	private Number id;
	private Client client_id;
	private Contract contract;

	private List<Smeta> smeta;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;

	private Boolean smeta_approved;

	private String order_doc_main;
	private String order_doc_signed;
	private Boolean order_approved;	
	
	public Order() {
	}

	public Order(Client client_id) {
		this.client_id = client_id;
	}

	public Order(Client client_id, Contract contract) {
		this.client_id = client_id;
		this.contract = contract;
	}

	public Order(Client client_id, Contract contract, List<Smeta> smeta) {
		this.client_id = client_id;
		this.smeta = smeta;
		this.contract = contract;
	}

	public Order(Client client_id, Contract contract, List<Smeta> smeta, LocalDateTime updatedAt) {
		this.client_id = client_id;
		this.smeta = smeta;
		this.contract = contract;
		this.updated_at = updatedAt;
	}

	public Order(Number id, Client client_id, Contract contract, List<Smeta> smeta, Boolean smeta_approved,
			String order_doc_main, String order_doc_signed, Boolean order_approved) {
		this.id = id;
		this.client_id = client_id;
		this.smeta = smeta;
		this.contract = contract;
		this.smeta_approved = smeta_approved;
		this.order_doc_main = order_doc_main;
		this.order_doc_signed = order_doc_signed;
		this.order_approved = order_approved;
	}

	public Order(Number id, Client client_id, Contract contract, List<Smeta> smeta, LocalDateTime createdAt,
			LocalDateTime updatedAt, Boolean smeta_approved, String order_doc_main, String order_doc_signed, Boolean order_approved) {
		this.id = id;
		this.client_id = client_id;
		this.smeta = smeta;
		this.contract = contract;
		this.created_at = createdAt;
		this.updated_at = updatedAt;
		this.smeta_approved = smeta_approved;
		this.order_doc_main = order_doc_main;
		this.order_doc_signed = order_doc_signed;
		this.order_approved = order_approved;
	}

	public Order(Number id, String order_doc_main, String order_doc_signed) {
		this.id = id;
		this.order_doc_main = order_doc_main;
		this.order_doc_signed = order_doc_signed;
	}

	public Order(Number id, String order_doc_main) {
		this.id = id;
		this.order_doc_main = order_doc_main;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public Client getClient_id() {
		return client_id;
	}

	public void setClient_id(Client client_id) {
		this.client_id = client_id;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public List<Smeta> getSmeta() {
		return smeta;
	}

	public void setSmeta(List<Smeta> smeta) {
		this.smeta = smeta;
	}

	public Boolean getSmeta_approved() {
		return smeta_approved;
	}

	public void setSmeta_approved(Boolean smeta_approved) {
		this.smeta_approved = smeta_approved;
	}

	public String getOrder_doc_main() {
		return order_doc_main;
	}

	public void setOrder_doc_main(String order_doc_main) {
		this.order_doc_main = order_doc_main;
	}

	public String getOrder_doc_signed() {
		return order_doc_signed;
	}

	public void setOrder_doc_signed(String order_doc_signed) {
		this.order_doc_signed = order_doc_signed;
	}

	public Boolean getOrder_approved() {
		return order_approved;
	}

	public void setOrder_approved(Boolean order_approved) {
		this.order_approved = order_approved;
	}

}
