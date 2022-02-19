package backend.Services.ProjectService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import backend.model.Project.ProjectStep;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface ProjectService {
	void getProject(RoutingContext ctx);
	void addProject(RoutingContext ctx);
	void updateProject(RoutingContext ctx);
	void doPrePayment(RoutingContext ctx);
	void addStep(RoutingContext ctx);
	void doStepPayment(RoutingContext ctx);
	void uploadStepPhoto(RoutingContext ctx);
	void removeStepPhoto(RoutingContext ctx);
	CompletableFuture<List<ProjectStep>> doLogic(RowSet<Row> set);
}