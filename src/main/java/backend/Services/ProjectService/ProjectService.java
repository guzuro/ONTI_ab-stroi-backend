package backend.Services.ProjectService;

import io.vertx.ext.web.RoutingContext;

public interface ProjectService {
	void getProject(RoutingContext ctx);
	void addProject(RoutingContext ctx);
	void updateProject(RoutingContext ctx);
	void doPrePayment(RoutingContext ctx);
	void addStep(RoutingContext ctx);
	void doStepPayment(RoutingContext ctx);

}