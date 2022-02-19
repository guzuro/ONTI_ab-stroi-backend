package backend.Routes;

import backend.Services.ProjectService.ProjectService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ProjectRoutes {
	
	final ProjectService projectService;

	public ProjectRoutes(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	public Router setProjectRoutes(Vertx vertx) {
		Router projectRouter = Router.router(vertx);

		projectRouter.post("/add").handler(rc -> projectService.addProject(rc));
		projectRouter.post("/get").handler(rc -> projectService.getProject(rc));
		projectRouter.post("/update").handler(rc -> projectService.updateProject(rc));
		projectRouter.post("/doPayment").handler(rc -> projectService.doPrePayment(rc));
		
		projectRouter.post("/addstep").handler(rc -> projectService.addStep(rc));
		projectRouter.post("/doStepPayment").handler(rc -> projectService.doStepPayment(rc));

		projectRouter.post("/uploadphoto").handler(rc -> projectService.uploadStepPhoto(rc));
		projectRouter.post("/removephoto").handler(rc -> projectService.removeStepPhoto(rc));

		return projectRouter;
	}	
}
