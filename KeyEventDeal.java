import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.GpioPinPwmOutput;

public class KeyEventDeal implements KeyListener{

	int loc = 0;
	int lastLoction = 0;
	String lastValue = null;
	String lastType = null;
	MenuJTable table;
	MenuArrayList menuItemList;
	MenuArrayList menuList;
	MenuManager manager;
	DefaultTableModel model;
	DefaultTableModel modelHead;
	MenuArrayList menuHeadList;
	Map<String,MenuArrayList> map;
	MenuItem menuItem = new MenuItem();
	
	PictureFrame pictureFrame = new PictureFrame();
	ControlGpioExample pinCtrlR = new ControlGpioExample();
	GpioPinDigitalOutput pinR = pinCtrlR.initGPIO(RaspiPin.GPIO_29, "LED-R", PinState.LOW);
	ControlGpioExample pinCtrlG = new ControlGpioExample();
	GpioPinDigitalOutput pinG = pinCtrlG.initGPIO(RaspiPin.GPIO_28, "LED-G", PinState.LOW);
	ControlGpioExample pinCtrlB = new ControlGpioExample();
	GpioPinDigitalOutput pinB = pinCtrlB.initGPIO(RaspiPin.GPIO_27, "LED-B", PinState.LOW);
	PwmExample pinPwm = new PwmExample();
	GpioPinPwmOutput pinPwmOut;//50hz

	public KeyEventDeal(MenuJTable table,
			Map<String,MenuArrayList> map,
			MenuManager manager,
			DefaultTableModel model,
			DefaultTableModel modelHead){
		this.manager = manager;
		this.model = model;
		this.table = table;
		this.menuItemList = map.get("mainMenuItem");
		this.modelHead = modelHead;
		this.menuHeadList = map.get("mainMenuHead");
		this.map = map;
		try{
			pinPwmOut = pinPwm.initPWMPin(RaspiPin.GPIO_01,1200,320);
			pinPwm.setPwmPin(pinPwmOut,30);
		}catch(InterruptedException ex){

		}	
	}

	public void gpioCtrl(MenuArrayList menuItemList,MenuJTable table,MenuManager manager,DefaultTableModel model,ControlGpioExample pinCtrl,GpioPinDigitalOutput pin,String lastValue){
		if(lastValue.equals("TRUE")) {
			
			try{
				pinCtrl.reSetGPIO(pin);
			}catch(InterruptedException ex){

			}
			
			System.out.println("now is TRUE");
			manager.changeItemValue(menuItemList, table.getSelectedRow(), "FALSE", model);
		}else if (lastValue.equals("FALSE")) {
			
			try{
				pinCtrl.setGPIO(pin);
			}catch(InterruptedException ex){

			}
			
			System.out.println("now is FALSE");
			manager.changeItemValue(menuItemList, table.getSelectedRow(), "TRUE", model);
		}
	}
	
	public void keyLeft() {
		System.out.println("left");
		if(table.getSelectedRow()==3) {
			pictureFrame.setVisible(true);
			
			//table.requestFocus();
	    	//table.changeSelection(table.getSelectedRow(),0,false, false);
		}else {
			pictureFrame.setVisible(false);
		}
		if(manager.getIsMainMenu()) {
			menuList = menuItemList;
		}else {
			menuList = map.get("subMenuItem");
		}
		lastType = menuList.get(table.getSelectedRow()).getType();
		lastValue = menuList.get(table.getSelectedRow()).getValue();
		if(lastType.equals("bool")) {
			switch(table.getSelectedRow()){
				case 0:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlR,pinR,lastValue);
					break;
				case 1:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlG,pinG,lastValue);
					break;
				case 2:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlB,pinB,lastValue);
					break;
				default:
					break;
			}
		}else if(lastType.equals("int")){
			int num = Integer.parseInt(menuList.get(table.getSelectedRow()).getValue());
			num --;
			if(num<menuItem.getMinValue()) {
				num = menuItem.getMinValue();
			}	
			try{
				pinPwm.setPwmPin(pinPwmOut,30+num*10);
			}catch(InterruptedException ex){

			}
			pictureFrame.updateImages(System.getProperty("user.dir")+"/images/pic"+String.valueOf(num+1)+".jpg");
			manager.changeItemValue(menuList, table.getSelectedRow(), String.valueOf(num), model);
		}else {
			
		}
		table.updateUI();	
	}
	
	public void keyRight() {
		System.out.println("right");
		if(table.getSelectedRow()==3) {
			pictureFrame.setVisible(true);
			//table.requestFocus();
	    	//table.changeSelection(table.getSelectedRow(),0,false, false);
		}else {
			pictureFrame.setVisible(false);
		}
		if(manager.getIsMainMenu()) {
			menuList = menuItemList;
		}else {
			menuList = map.get("subMenuItem");
		}
		lastType = menuList.get(table.getSelectedRow()).getType();
		lastValue = menuList.get(table.getSelectedRow()).getValue();
		if(lastType.equals("bool")) {
			switch(table.getSelectedRow()){
				case 0:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlR,pinR,lastValue);
					break;
				case 1:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlG,pinG,lastValue);
					break;
				case 2:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlB,pinB,lastValue);
					break;
				default:
					break;
			}
		}else if(lastType.equals("int")){
			int num = Integer.parseInt(menuList.get(table.getSelectedRow()).getValue());
			num ++;
			if(num>menuItem.getMaxValue()) {
				num = menuItem.getMaxValue();
			}
			try{
				pinPwm.setPwmPin(pinPwmOut,30+num*10);
			}catch(InterruptedException ex){

			}
			pictureFrame.updateImages(System.getProperty("user.dir")+"/images/pic"+String.valueOf(num+1)+".jpg");
			manager.changeItemValue(menuList, table.getSelectedRow(), String.valueOf(num), model);
		}else {
			
		}
		table.updateUI();	
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		String a;
		if(manager.getIsMainMenu()) {
			menuList = menuItemList;
		}else {
			menuList = map.get("subMenuItem");
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("up");
			if(table.getSelectedRow()!=3) {
				
				pictureFrame.setVisible(false);
			}

			if(table.getSelectedRow()!=0 || table.getSelectedRow() != table.getRowCount()) {
				manager.changeItemId(menuList, lastLoction, "  "+String.format("%2d", lastLoction).replace(" ", "0"),model);
				manager.changeItemId(menuList, table.getSelectedRow(), "* "+String.format("%2d", table.getSelectedRow()).replace(" ", "0"),model);
				a = "*"+String.format("%2d", table.getSelectedRow()).replace(" ", "0");
		    	System.out.println(a);
		    	System.out.println(table.getSelectedRow());
			}
			table.updateUI();
			break;
		
		case KeyEvent.VK_DOWN:
			if(table.getSelectedRow()!=3) {
				
				pictureFrame.setVisible(false);
			}
			System.out.println("down");
			if(table.getSelectedRow()!=0 || table.getSelectedRow() != table.getRowCount()) {
				manager.changeItemId(menuList, lastLoction, "  "+String.format("%2d", lastLoction).replace(" ", "0"),model);
				manager.changeItemId(menuList, table.getSelectedRow(), "* "+String.format("%2d", table.getSelectedRow()).replace(" ", "0"),model);
				a = "*"+String.format("%2d", table.getSelectedRow()).replace(" ", "0");
		    	System.out.println(a);
		    	System.out.println(table.getSelectedRow());
			}
			table.updateUI();
			break;
			
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			keyLeft();
			break;
			
		case KeyEvent.VK_D:		
		case KeyEvent.VK_RIGHT:
			keyRight();
			break;
			
		case KeyEvent.VK_E:
			lastType = menuList.get(table.getSelectedRow()).getType();
			if(lastType.equals("sub")) {
				loc = table.getSelectedRow();
				manager.enterSubMenu(menuHeadList,"SUB  MENU",modelHead);
				manager.reflesh();
				manager.removeAll(menuItemList,model);
				manager.updateItem(map.get("subMenuItem"), model);
				table.requestFocus();
		    	table.changeSelection(manager.getLastLocation(),0,false, false);
		    	manager.setLastLocation(loc);
			}
			break;
			
		case KeyEvent.VK_Q:
			if(manager.getIsMainMenu()) {
				System.exit(0);
			}else {
				loc = table.getSelectedRow();
				manager.enterMainMenu(menuHeadList,"FACTORY MENU",modelHead);
				manager.reflesh();
				manager.removeAll(map.get("subMenuItem"),model);
				manager.updateItem(menuItemList, model);
				table.requestFocus();
		    	table.changeSelection(manager.getLastLocation(),0,false, false);
		    	manager.setLastLocation(loc);
			}			
			break;
			
		case KeyEvent.VK_S:
			try {
				Robot r = new Robot();//创建自动化工具对象
				r.keyPress(KeyEvent.VK_DOWN);
				r.keyRelease(KeyEvent.VK_DOWN);
			} catch (Exception e2) {
				return;
			}
			break;
			
		case KeyEvent.VK_W:
			try {
				Robot r = new Robot();//创建自动化工具对象
				r.keyPress(KeyEvent.VK_UP);
				r.keyRelease(KeyEvent.VK_UP);
			} catch (Exception e2) {
				return;
			}
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		lastLoction = table.getSelectedRow();
	}

}
