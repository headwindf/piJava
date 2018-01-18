/**
* MenuItem类，每一个条目都是一个MenuItem对象
* date：2018.1.15
* @author headwind
* @version V0.0
*/
public class MenuItem {
	private String id;
	private String itemName;
	private String type;
	private String value;
	private static int MaxValue = 9;
	private static int MinValue = 0;
	
	public MenuItem() {
		this.id = "00";
		this.itemName = "null";
		this.type = "null";
		this.value = "null";
	}
	
	public MenuItem(String id,String itemName,String type,String value) {
		this.id = id;
		this.itemName = itemName;
		this.type = type;
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getMaxValue() {
		return MaxValue;
	}
	
	public int getMinValue() {
		return MinValue;
	}
}
