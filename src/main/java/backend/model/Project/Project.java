package backend.model.Project;

import java.util.List;

public class Project {

	private Number id;
	private Number order_id;
	private Number client_id;
	private int prepayment_sum;
	private Boolean prepayment_success;
	private String project_end_date;
	private List<ProjectStep> steps;

	public Project() {

	}

	public Project(Number order_id, Number client_id, int prepayment_sum, Boolean prepayment_success,
			List<ProjectStep> steps) {
		this.order_id = order_id;
		this.client_id = client_id;
		this.prepayment_sum = prepayment_sum;
		this.prepayment_success = prepayment_success;
		this.steps = steps;
	}

	public Project(Number order_id, Number client_id, String project_end_date) {
		this.order_id = order_id;
		this.client_id = client_id;
		this.project_end_date = project_end_date;
}

	public Project(Number id, Number order_id, Number client_id, int prepayment_sum, Boolean prepayment_success,
			List<ProjectStep> steps, String project_end_date) {
		this.id = id;
		this.order_id = order_id;
		this.client_id = client_id;
		this.prepayment_sum = prepayment_sum;
		this.prepayment_success = prepayment_success;
		this.steps = steps;
		this.project_end_date = project_end_date;
	}

	public Project(Number id, Number order_id, Number client_id, int prepayment_sum, Boolean prepayment_success,
			String project_end_date) {
		this.id = id;
		this.order_id = order_id;
		this.client_id = client_id;
		this.prepayment_sum = prepayment_sum;
		this.prepayment_success = prepayment_success;
		this.project_end_date = project_end_date;
	}

	public Project(Number id, Boolean prepayment_success) {
		this.id = id;
		this.prepayment_success = prepayment_success;
	}

	public Project(Number id, String project_end_date) {
		this.id = id;
		this.project_end_date = project_end_date;
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

	public int getPrepayment_sum() {
		return prepayment_sum;
	}

	public void setPrepayment_sum(int prepayment_sum) {
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

	public String getProject_end_date() {
		return project_end_date;
	}

	public void setProject_end_date(String project_end_date) {
		this.project_end_date = project_end_date;
	}
}
