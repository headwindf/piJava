
import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * This example code demonstrates how to perform serial communications using the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class SerialCtrl {
    private static SerialCtrl serialCtrl = new SerialCtrl();
    private static Serial serial;
    private static String defaultSerial = "/dev/ttyAMA0";
    //private static SerialCtrl serialCtrl = new SerialCtrl();
    //private static Serial serial;

    public Serial initSerial(){
        System.out.println("serial");
        final Serial serial = SerialFactory.createInstance();
        try {
            // create serial config object
            SerialConfig config = new SerialConfig();
            config.device(defaultSerial)
                  .baud(Baud._115200)
                  .dataBits(DataBits._8)
                  .parity(Parity.NONE)
                  .stopBits(StopBits._1)
                  .flowControl(FlowControl.NONE);
            serial.open(config);
        }
        catch(Exception ex) {
            System.out.println("err");
        }
        return serial;
    }

    public void receieveDeal(Serial serial){
        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                // NOTE! - It is extremely important to read the data received from the
                // serial port.  If it does not get read from the receive buffer, the
                // buffer will continue to grow and consume memory.

                // print out the data received to the console
                
                try {
                    System.out.println("you got "+event.getHexByteString());
                    
                    UartPackage uartPackage = new UartPackage();
                    String commandStr = uartPackage.unzipPackage(event.getHexByteString());
                    switch(commandStr){
                        case "menu":
                            System.out.println("you got menu");
                            createFrame();
                            break;
                        case "up":
                            try {
                                Robot r = new Robot();//创建自动化工具对象
                                r.keyPress(KeyEvent.VK_UP);
                                r.keyRelease(KeyEvent.VK_UP);
                            } catch (Exception e2) {
                                return;
                            }
                            System.out.println("you got up");
                            serialCtrl.sendMessage(serial,"up");
                            break;
                        case "down":
                            try {
                                Robot r = new Robot();//创建自动化工具对象
                                r.keyPress(KeyEvent.VK_DOWN);
                                r.keyRelease(KeyEvent.VK_DOWN);
                            } catch (Exception e2) {
                                return;
                            }
                            System.out.println("you got down");
                            serialCtrl.sendMessage(serial,"down");
                            break;
                        case "left":
                            try {
                                Robot r = new Robot();//创建自动化工具对象
                                r.keyPress(KeyEvent.VK_LEFT);
                                r.keyRelease(KeyEvent.VK_LEFT);
                            } catch (Exception e2) {
                                return;
                            }
                            System.out.println("you got left");
                            serialCtrl.sendMessage(serial,"left");
                            break;
                        case "right":
                            try {
                                Robot r = new Robot();//创建自动化工具对象
                                r.keyPress(KeyEvent.VK_RIGHT);
                                r.keyRelease(KeyEvent.VK_RIGHT);
                            } catch (Exception e2) {
                                return;
                            }
                            System.out.println("you got right");
                            serialCtrl.sendMessage(serial,"right");
                            break;
                        case "exit":
                            try {
                                Robot r = new Robot();//创建自动化工具对象
                                r.keyPress(KeyEvent.VK_Q);
                                r.keyRelease(KeyEvent.VK_Q);
                            } catch (Exception e2) {
                                return;
                            }
                            System.out.println("you got exit");
                            serialCtrl.sendMessage(serial,"exit");
                            break;

                        case "error":
                            System.out.println("you got error");
                            serialCtrl.sendMessage(serial,"error");
                            break;
                        default :
                            break;
                    }
                    //System.out.println("received:"+ event.getHexByteString());
                    //System.out.println("received:"+ event.getAsciiString());
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMessage(Serial serial,String str){
        try {
            UartPackage uartPackage = new UartPackage();
            byte[] sendByte = uartPackage.createPackageData(str, str.length());
            serial.write(sendByte);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void createFrame(){
        Map<String,MenuArrayList> map =new HashMap<String,MenuArrayList>();
        
        MenuArrayList mainMenuItemList = new MenuArrayList();
        MenuArrayList mainMenuHeadList = new MenuArrayList();
        MenuArrayList subMenuItemList = new MenuArrayList();
        
        map.put("mainMenuHead", mainMenuHeadList);
        map.put("mainMenuItem", mainMenuItemList);
        map.put("subMenuItem", subMenuItemList);
        
        DefaultTableModel model = new DefaultTableModel();
        DefaultTableModel modelHead = new DefaultTableModel();
        MenuJTable table = new MenuJTable(model);
        MenuHeadJtable tableHead = new MenuHeadJtable(modelHead);
        JTableHeader tableHeader = table.getTableHeader();
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(); 
        renderer.setHorizontalAlignment(JLabel.CENTER);   
        table.setDefaultRenderer(Object.class, renderer);
        tableHeader.setDefaultRenderer(new HeadRenderer(tableHeader.getDefaultRenderer()));
        tableHeader = tableHead.getTableHeader();
        tableHeader.setDefaultRenderer(new HeadRenderer(tableHeader.getDefaultRenderer()));
        
        MenuManager manager = new MenuManager();
        manager.init(model,table,tableHead,modelHead);

        manager.addItem("","","text","FACTORY MENU",mainMenuHeadList);
        manager.addItem("","","text","<1> return/exit                             <2> show menu",mainMenuHeadList);
        manager.updateItem(map.get("mainMenuHead"), modelHead);
        
        manager.addItem("* 00","LED-R","bool","FALSE",mainMenuItemList);
        manager.addItem("  01","LED-G","bool","FALSE",mainMenuItemList);
        manager.addItem("  02","LED-B","bool","FALSE",mainMenuItemList);
        manager.addItem("  03","Steering level","int","5",mainMenuItemList);
        manager.addItem("  04","SUB MENU","sub",">>>>",mainMenuItemList);
        manager.addItem("  05","SUB MENU","sub",">>>>",mainMenuItemList);
        manager.addItem("  06","SUB MENU","sub",">>>>",mainMenuItemList);
        manager.addItem("  07","SUB MENU","sub",">>>>",mainMenuItemList);
        manager.updateItem(map.get("mainMenuItem"), model);
        
        manager.addItem("* 00","sub-item1","bool","FALSE",subMenuItemList);
        manager.addItem("  01","sub-item2","bool","FALSE",subMenuItemList);
        
        //manager.removeItem(mainMenuItemList, 3, model);
    
        table.requestFocus();
        table.changeSelection(0,0,false, false);

        tableHead.setEnabled(false);
        //SelectionListener listener = new SelectionListener(table);
        //table.getSelectionModel().addListSelectionListener(listener);
        
        System.out.println(System.getProperty("user.dir"));
        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        */
        table.addKeyListener(new KeyEventDeal(table,map,manager,model,modelHead));
        //manager.setVisible(true);
    }

    public static void main(String[] args) throws AWTException{ 
        
        //createFrame();
        
        //new Thread(manager,"串口").start();
        serial = serialCtrl.initSerial();
        serialCtrl.receieveDeal(serial);
        serialCtrl.sendMessage(serial,"menu");
        while(true);
    }
}

