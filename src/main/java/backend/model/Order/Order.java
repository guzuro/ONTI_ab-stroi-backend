package backend.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

	private Number id;
	private Number client_id;
	private Contract contract;

	private List<Smeta> smeta;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;

	public Order() {
	}

	public Order(Number client_id) {
		this.client_id = client_id;
	}

	public Order(Number client_id, Contract contract) {
		this.client_id = client_id;
		this.contract = contract;
	}

	public Order(Number client_id, Contract contract, List<Smeta> smeta) {
		this.client_id = client_id;
		this.smeta = smeta;
		this.contract = contract;
	}

	public Order(Number client_id, Contract contract, List<Smeta> smeta, LocalDateTime updatedAt) {
		this.client_id = client_id;
		this.smeta = smeta;
		this.contract = contract;
		this.updated_at = updatedAt;
	}

	public Order(Number id, Number client_id, Contract contract, List<Smeta> smeta, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.client_id = client_id;
		this.smeta = smeta;
		this.contract = contract;
		this.created_at = createdAt;
		this.updated_at = updatedAt;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public Number getClient_id() {
		return client_id;
	}

	public void setClient_id(Number client_id) {
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

}
