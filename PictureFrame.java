import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PictureFrame extends JFrame{
	private static int fwidth = 500;
    private static int fheigth = 288;
    private JLabel pic;
    int w = (Toolkit.getDefaultToolkit().getScreenSize().width - fwidth) / 2;
    int h = (Toolkit.getDefaultToolkit().getScreenSize().height - fheigth) / 2;
    
	public PictureFrame() {
		
        
		pic = new JLabel();
        pic.setIcon(new ImageIcon(System.getProperty("user.dir")+"/images/pic1.jpg"));
        pic.setBounds(0, 0, fwidth, fheigth);
        
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(fwidth, fheigth);
        setVisible(false);
        add(pic);
        setAlwaysOnTop(true);
        //setEnabled(false);
        setFocusableWindowState(false);
        
        addWindowFocusListener(new WindowFocusListener()
		{
			public void windowGainedFocus(WindowEvent e)
			{
				System.out.println("子窗体获得焦点");
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("子窗体失去焦点");
			}
		});
        this.setLocation(w, h);
        System.out.println("子窗体w："+ w);
        System.out.println("子窗体h："+ h);
        	
	}
	
	public void updateImages(String path) {
		remove(pic);
		pic = new JLabel();
        pic.setIcon(new ImageIcon(path));
        pic.setBounds(0, 0, fwidth, fheigth);
        add(pic);
        repaint();
        this.setLocation(w, h);
	}
	
}
