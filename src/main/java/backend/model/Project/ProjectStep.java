package backend.model.Project;

public class ProjectStep {
	private Number id;
	private Number project_id;
	private String[] photo;
	private String end_date;
	private Boolean to_delete;
	private Boolean step_payed;
	private Number step_cost;
	private Boolean is_done;
	private String title;
	private String description;

	public ProjectStep() {

	}

	public ProjectStep(Number id, Number project_id, String[] photo, String end_date, Boolean to_delete,
			Boolean step_payed, Boolean is_done, String title, String description, Number step_cost) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.project_id = project_id;
		this.photo = photo;
		this.end_date = end_date;
		this.to_delete = to_delete;
		this.step_payed = step_payed;
		this.is_done = is_done;
		this.step_cost = step_cost;
	}

	public ProjectStep(Number project_id, String[] photo, String end_date, Boolean to_delete) {
		this.project_id = project_id;
		this.photo = photo;
		this.end_date = end_date;
		this.to_delete = to_delete;
	}

	public ProjectStep(Number project_id, String[] photo) {
		this.project_id = project_id;
		this.photo = photo;
	}

	public ProjectStep(Number project_id, String end_date, String title) {
		this.project_id = project_id;
		this.end_date = end_date;
		this.title = title;
	}

	public ProjectStep(Number project_id, String end_date, String title, String description, Number step_cost) {
		this.project_id = project_id;
		this.end_date = end_date;
		this.title = title;
		this.description = description;
		this.step_cost = step_cost;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public Number getProject_id() {
		return project_id;
	}

	public void setProject_id(Number project_id) {
		this.project_id = project_id;
	}

	public String[] getPhoto() {
		return photo;
	}

	public void setPhoto(String[] photo) {
		this.photo = photo;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public Boolean getTo_delete() {
		return to_delete;
	}

	public void setTo_delete(Boolean to_delete) {
		this.to_delete = to_delete;
	}

	public Boolean getStep_payed() {
		return step_payed;
	}

	public void setStep_payed(Boolean step_payed) {
		this.step_payed = step_payed;
	}

	public Boolean getIs_done() {
		return is_done;
	}

	public void setIs_done(Boolean is_done) {
		this.is_done = is_done;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Number getStep_cost() {
		return step_cost;
	}

	public void setStep_cost(Number step_cost) {
		this.step_cost = step_cost;
	}
}
