import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class MenuManager extends JFrame {
	
	private static final long serialVersionUID = 1L;
    private static int fwidth = 900;
    private static int fheigth = 490;
    private boolean isMainMenu = true;
    private int lastLocation = 0;
    
    private JScrollPane scrollPaneHead;
    private JScrollPane scrollPane;
    
    public MenuManager() {
    	
    }
    
    public void init(DefaultTableModel model,MenuJTable table,MenuHeadJtable tableHead,DefaultTableModel modelHead) {
    	int w = (Toolkit.getDefaultToolkit().getScreenSize().width - fwidth) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - fheigth) / 2;
        this.setLocation(w, h);
        System.out.println("父窗体w："+ w);
        System.out.println("父窗体h："+ h);
    	
    	setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(fwidth, fheigth);
        setVisible(true);
        
        
        addWindowFocusListener(new WindowFocusListener()
		{
			public void windowGainedFocus(WindowEvent e)
			{
				System.out.println("父窗体获得焦点");
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("父窗体失去焦点");
			}
		});

		Vector names = new Vector();
		Vector data = new Vector(); 
		Vector row = new Vector(); 
		names.add("");
		names.add("");
		names.add("");
		
		model.setDataVector(data, names); 
		Vector namesHead = new Vector();
		Vector dataHead = new Vector(); 

		namesHead.add("");
		namesHead.add("");
		namesHead.add("");
		modelHead.setDataVector(dataHead, namesHead); 

        //table.setShowGrid(false);
		table.setGridColor(Color.white);
		table.setSelectionBackground(Color.gray);
        table.setBackground(Color.white);
        table.setRowHeight(70);
        table.setFont(new Font("Menu.font", Font.PLAIN, 30));
        DefaultTableCellRenderer r   = new DefaultTableCellRenderer();   
        r.setHorizontalAlignment(JLabel.CENTER);   
        //table.setDefaultRenderer(Object.class, r);
        
        tableHead.setGridColor(Color.white);
        tableHead.setSelectionBackground(Color.white);
        tableHead.setBackground(Color.white);
        tableHead.setRowHeight(70);
        tableHead.setFont(new Font("Menu.font", Font.PLAIN, 30)); 
        tableHead.setDefaultRenderer(Object.class, r);
		
        scrollPane=new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);  
        scrollPane.getViewport().setOpaque(false); 
        scrollPane.setBackground(Color.white);
        table.setOpaque(false);
        
        scrollPane.setBounds(0,140,fwidth,fheigth-140);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        //add(new JScrollPane(table));

        scrollPaneHead=new JScrollPane(tableHead);
        scrollPaneHead.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneHead.setOpaque(false);  
        scrollPaneHead.getViewport().setOpaque(false); 
        tableHead.setOpaque(false);
        
        scrollPaneHead.setBounds(0,0,fwidth,140);
        getContentPane().add(scrollPaneHead,BorderLayout.CENTER);
       
        

	}
    
    public void removeAll(MenuArrayList mainMenuItemList,DefaultTableModel model) {
    	mainMenuItemList.remove(model);
	}
    
    public void updateItem(MenuArrayList mainMenuItemList,DefaultTableModel model) {
    	for (int i = 0; i < mainMenuItemList.size(); i++) {
			mainMenuItemList.add(mainMenuItemList.get(i), model);
		}	
	}
    
    public void addItem(String id,String itemName,String type,String value,MenuArrayList mainMenuItemList) {
		MenuItem menuItem = new MenuItem(id,itemName,type,value);
		mainMenuItemList.add(menuItem);		
	}
    
    public void removeItem(MenuArrayList mainMenuItemList,int index,DefaultTableModel model) {
    	mainMenuItemList.remove(index,model);
	}
    
    public void changeItemId(MenuArrayList mainMenuItemList,int index,String id,DefaultTableModel model) {
    	mainMenuItemList.get(index).setId(id);
    	model.setValueAt(mainMenuItemList.get(index).getId(), index, 0);
	}
    
    public void changeItemValue(MenuArrayList mainMenuItemList,int index,String value,DefaultTableModel model) {
    	mainMenuItemList.get(index).setValue(value);
    	model.setValueAt(mainMenuItemList.get(index).getValue(), index, 2);
	}
    
    public void enterSubMenu(MenuArrayList mainMenuItemList,String value,DefaultTableModel model) {
    	this.changeItemValue(mainMenuItemList,0,value,model);
		//remove(scrollPane);
		this.isMainMenu = false;
	}
    
    public void reflesh() {
		repaint();
	}
    
    public void enterMainMenu(MenuArrayList mainMenuItemList,String value,DefaultTableModel model) {
    	this.changeItemValue(mainMenuItemList,0,value,model);
    	this.isMainMenu = true;
	}
    
    public boolean getIsMainMenu() {
		return this.isMainMenu;
	}
    
    public void setLastLocation(int index) {
		this.lastLocation = index;
	}
    
    public int getLastLocation() {
		return this.lastLocation;
	}
    
    public static void main(String[] args) throws AWTException{ 
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
}



