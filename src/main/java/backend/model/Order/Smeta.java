package backend.model.Order;

public class Smeta {
	private String item_name;
	private String unit;
	private Number quantity; 
	private Number price;
	private Number item_total;
	
	public Smeta(String item_name, String unit, Number quantity, Number price, Number item_total) {
		this.item_name = item_name;
		this.unit = unit;
		this.quantity = quantity;
		this.price = price;
		this.item_total = item_total;
	}
	
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public Number getPrice() {
		return price;
	}
	public void setPrice(Number price) {
		this.price = price;
	}
	public Number getItem_total() {
		return item_total;
	}
	public void setItem_total(Number item_total) {
		this.item_total = item_total;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Number getQuantity() {
		return quantity;
	}

	public void setQuantity(Number quantity) {
		this.quantity = quantity;
	}
}
