package backend.model.Project;

public class ProjectStep {
	private Number id;
	private Number project_id;
	private String[] photo;
	private String end_date;

	public ProjectStep() {

	}

	public ProjectStep(Number id, Number project_id, String[] photo, String end_date) {
		this.id = id;
		this.project_id = project_id;
		this.photo = photo;
		this.end_date = end_date;
	}

	public ProjectStep(Number project_id, String[] photo, String end_date) {
		this.project_id = project_id;
		this.photo = photo;
		this.end_date = end_date;
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
}
