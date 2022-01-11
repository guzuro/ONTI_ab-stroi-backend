package backend.model.User;

import java.time.LocalDateTime;

import backend.model.UserRoleEnum;

public class BaseUser {

	private Number id;
	private String login;
	private String password;
	private String first_name;
	private String last_name;

	private UserRoleEnum role;
	private LocalDateTime created_at;

	public BaseUser(String login, String password, String first_name, String last_name, UserRoleEnum role) {
		this.login = login;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
	}

	public BaseUser(String login, String first_name, String last_name, UserRoleEnum userRoleEnum) {
		this.login = login;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = userRoleEnum;
	}

	public BaseUser(Number id, String login, String first_name, String last_name, UserRoleEnum userRoleEnum,
			LocalDateTime ldt) {
		this.id = id;
		this.login = login;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = userRoleEnum;
		this.created_at = ldt;
	}

	public BaseUser(Number id, String login, String first_name, String last_name) {
		this.id = id;
		this.login = login;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public BaseUser() {
		// TODO Auto-generated constructor stub
	}

	public UserRoleEnum getRole() {
		return role;
	}

	public void setRole(UserRoleEnum role) {
		this.role = role;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
}

//class BaseUserObjectToJson extends JsonSerializer<BaseUser> {
//	@Override
//	public void serialize(BaseUser value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
//        jsonGenerator.writeStartObject();
//        jsonGenerator.writeNumberField("id", value.getId());
//        jsonGenerator.writeStringField("login", value.getLogin());
//        jsonGenerator.writeStringField("first_name", value.getFirst_name());
//        jsonGenerator.writeStringField("last_name", value.getLast_name());
//        jsonGenerator.writeStringField("role", value.getUserRoleEnum().toString());
//        jsonGenerator.writeStringField("created_at", value.getCreatedAt().toString());
//        jsonGenerator.writeEndObject();
//        jsonGenerator.close();
//		
//	}
//}
//
//class JsonToBaseUserObject extends JsonDeserializer {
//
//	@Override
//	public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
//        System.out.print(node);
//        String login = node.get("login").textValue();
//        String first_name = node.get("first_name").textValue();
//        String last_name = node.get("last_name").textValue();
//        short id = node.get("id").shortValue();
//        UserRoleEnum userRoleEnum = UserRoleEnum.valueOf(node.get("role").textValue().toString());
//        LocalDateTime ldt = LocalDateTime.parse(node.get("created_at").textValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        return new BaseUser(id, login, first_name, last_name, userRoleEnum, ldt);
//	}
//}
