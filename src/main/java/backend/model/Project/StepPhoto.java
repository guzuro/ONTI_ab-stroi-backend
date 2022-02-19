package backend.model.Project;

public class StepPhoto {
	private Number id;
	private String name;
	private Number step_id;

	public StepPhoto() {
	}

	public StepPhoto(Number id) {
		this.id = id;
	}

	public StepPhoto(String name, Number step_id) {
		this.name = name;
		this.step_id = step_id;
	}

	public StepPhoto(Number id, String name) {
		this.id = id;
		this.name = name;
	}

	public StepPhoto(Number id, String name, Number step_id) {
		this.id = id;
		this.name = name;
		this.step_id = step_id;
	}

	public Number getStep_id() {
		return step_id;
	}

	public void setStep_id(Number step_id) {
		this.step_id = step_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}
}
