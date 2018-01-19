/**
* 按键事件处理
* date：2018.1.18
* @author headwind
* @version V0.1
*/

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

	private String leftOrRight = "left";
	int loc = 0;
	int lastLoction = 0;
	String rowValue = null;
	String rowType = null;
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

	public void gpioCtrl(MenuArrayList menuItemList,MenuJTable table,MenuManager manager,DefaultTableModel model,ControlGpioExample pinCtrl,GpioPinDigitalOutput pin,String rowValue){
		if(rowValue.equals("TRUE")) {
			try{
				pinCtrl.reSetGPIO(pin);
			}catch(InterruptedException ex){

			}
			//System.out.println("now is TRUE");
			manager.changeItemValue(menuItemList, table.getSelectedRow(), "FALSE", model);
		}else if (rowValue.equals("FALSE")) {
			try{
				pinCtrl.setGPIO(pin);
			}catch(InterruptedException ex){

			}
			//System.out.println("now is FALSE");
			manager.changeItemValue(menuItemList, table.getSelectedRow(), "TRUE", model);
		}
	}
	
	public void keyLeftAndRight(int row,String leftOrRight) {
		if(row == 3) {
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
		rowType = menuList.get(row).getType();
		rowValue = menuList.get(row).getValue();
		if(rowType.equals("bool")) {
			switch(row){
				case 0:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlR,pinR,rowValue);
					break;
				case 1:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlG,pinG,rowValue);
					break;
				case 2:
					gpioCtrl(menuItemList,table,manager,model,pinCtrlB,pinB,rowValue);
					break;
				default:
					break;
			}
		}else if(rowType.equals("int")){
			int num = Integer.parseInt(rowValue);
			if(leftOrRight.equals("left")){
				num --;
				if(num<menuItem.getMinValue()) {
					num = menuItem.getMinValue();
				}	
			}else if(leftOrRight.equals("right")){
				num ++;
				if(num>menuItem.getMaxValue()) {
					num = menuItem.getMaxValue();
				}
			}
			try{
				pinPwm.setPwmPin(pinPwmOut,30+num*10);
			}catch(InterruptedException ex){

			}
			pictureFrame.updateImages(System.getProperty("user.dir")+"/images/pic"+String.valueOf(num+1)+".jpg");
			manager.changeItemValue(menuList, row, String.valueOf(num), model);
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
		String value;
		int row = table.getSelectedRow();
		if(manager.getIsMainMenu()) {
			menuList = menuItemList;
		}else {
			menuList = map.get("subMenuItem");
		}
		switch (e.getKeyCode()) {
			//上键，w键需要模拟上键按下，因为在jtable中上键具有上下移动的功能
			case KeyEvent.VK_W:
				try {
					Robot r = new Robot();//创建自动化工具对象
					r.keyPress(KeyEvent.VK_UP);
					r.keyRelease(KeyEvent.VK_UP);
				} catch (Exception e2) {
					return;
				}
				break;

			case KeyEvent.VK_UP:
				//System.out.println("up");
				if(row!=3) {
					pictureFrame.setVisible(false);
				}

				if(row!=0 || row != table.getRowCount()) {
					manager.changeItemId(menuList, lastLoction, "  "+String.format("%2d", lastLoction).replace(" ", "0"),model);
					manager.changeItemId(menuList, row, "* "+String.format("%2d", row).replace(" ", "0"),model);
					value = "*"+String.format("%2d", row).replace(" ", "0");
			    	//System.out.println(value);
			    	//System.out.println(row);
				}
				table.updateUI();
				break;
			
			//下键，同上理
			case KeyEvent.VK_S:
				try {
					Robot r = new Robot();//创建自动化工具对象
					r.keyPress(KeyEvent.VK_DOWN);
					r.keyRelease(KeyEvent.VK_DOWN);
				} catch (Exception e2) {
					return;
				}
				break;

			case KeyEvent.VK_DOWN:
				if(row!=3) {
					pictureFrame.setVisible(false);
				}
				//System.out.println("down");
				if(row!=0 || row != table.getRowCount()) {
					manager.changeItemId(menuList, lastLoction, "  "+String.format("%2d", lastLoction).replace(" ", "0"),model);
					manager.changeItemId(menuList, row, "* "+String.format("%2d", row).replace(" ", "0"),model);
					value = "*"+String.format("%2d", row).replace(" ", "0");
			    	//System.out.println(value);
			    	//System.out.println(row);
				}
				table.updateUI();
				break;
				
			//左键	
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				//System.out.println("left");
				keyLeftAndRight(row,"left");
				break;
				
			//右键
			case KeyEvent.VK_D:		
			case KeyEvent.VK_RIGHT:
				//System.out.println("right");
				keyLeftAndRight(row,"right");
				break;
			
			//进入子菜单
			case KeyEvent.VK_E:
				rowType = menuList.get(row).getType();
				if(rowType.equals("sub")) {
					loc = row;
					manager.enterSubMenu(menuHeadList,"SUB  MENU",modelHead);
					manager.reflesh();
					manager.removeAll(menuItemList,model);
					manager.updateItem(map.get("subMenuItem"), model);
					table.requestFocus();
			    	table.changeSelection(manager.getLastLocation(),0,false, false);
			    	manager.setLastLocation(loc);
				}
				break;
				
			//exit键功能，退出子菜单/程序
			case KeyEvent.VK_Q:
				if(manager.getIsMainMenu()) {
					System.exit(0);
				}else {
					loc = row;
					manager.enterMainMenu(menuHeadList,"FACTORY MENU",modelHead);
					manager.reflesh();
					manager.removeAll(map.get("subMenuItem"),model);
					manager.updateItem(menuItemList, model);
					table.requestFocus();
			    	table.changeSelection(manager.getLastLocation(),0,false, false);
			    	manager.setLastLocation(loc);
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
