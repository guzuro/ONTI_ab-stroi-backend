package backend.model.Project;

import java.util.List;

public class Project {

	private Number id;
	private Number order_id;
	private Number client_id;
	private Double prepayment_sum;
	private Boolean prepayment_success;
	private List<ProjectStep> steps;

	public Project() {

	}

	public Project(Number order_id, Number client_id, Double prepayment_sum, Boolean prepayment_success,
			List<ProjectStep> steps) {
		this.order_id = order_id;
		this.client_id = client_id;
		this.prepayment_sum = prepayment_sum;
		this.prepayment_success = prepayment_success;
		this.steps = steps;
	}

	public Project(Number id, Number order_id, Number client_id, Double prepayment_sum, Boolean prepayment_success,
			List<ProjectStep> steps) {
		this.id = id;
		this.order_id = order_id;
		this.client_id = client_id;
		this.prepayment_sum = prepayment_sum;
		this.prepayment_success = prepayment_success;
		this.steps = steps;
	}
	
	public Project(Number id, Number order_id, Number client_id, Double prepayment_sum, Boolean prepayment_success) {
		this.id = id;
		this.order_id = order_id;
		this.client_id = client_id;
		this.prepayment_sum = prepayment_sum;
		this.prepayment_success = prepayment_success;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public Number getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Number order_id) {
		this.order_id = order_id;
	}

	public Number getClient_id() {
		return client_id;
	}

	public void setClient_id(Number client_id) {
		this.client_id = client_id;
	}

	public Double getPrepayment_sum() {
		return prepayment_sum;
	}

	public void setPrepayment_sum(Double prepayment_sum) {
		this.prepayment_sum = prepayment_sum;
	}

	public Boolean getPrepayment_success() {
		return prepayment_success;
	}

	public void setPrepayment_success(Boolean prepayment_success) {
		this.prepayment_success = prepayment_success;
	}

	public List<ProjectStep> getSteps() {
		return steps;
	}

	public void setSteps(List<ProjectStep> projectSteps) {
		this.steps = projectSteps;
	}
}
