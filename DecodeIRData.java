import java.awt.Robot;
import java.awt.event.KeyEvent;

public class DecodeIRData extends Thread{
	private String str;
	private static int customerHead = 0x8890;

	public DecodeIRData(String str){
		this.str = str;
	}

	@Override
    public void run() {
        //System.out.println("thread start!");
        decodeIRData(new StringBuffer(str));
    }

    public static String decodeIRData(StringBuffer sBuffer){
        String str = "error";
        String strTmp = sBuffer.reverse().toString();
        
        String tmp1 = strTmp.substring(0,8);
        //System.out.println(tmp1);
        String tmp2 = strTmp.substring(8,16);
       // System.out.println(tmp2);
        String tmp3 = strTmp.substring(16,24);
       // System.out.println(tmp3);
        String tmp4 = strTmp.substring(24);
       // System.out.println(tmp4);
        
        strTmp = tmp4 + tmp3 + tmp2 + tmp1;
        long tmp = Long.parseLong(strTmp,2);
      //  System.out.println("tmp:  "+tmp);

        if((tmp>>16) != customerHead){
            //System.out.println("customerhead error");
            return "error";
        } else {
           // System.out.println("customerhead right");
        }
        
        long command = (tmp & 0x0000FF00)>>8;
        long commandAnti = tmp & 0x000000FF;

        if((command & commandAnti) != 0){
           // System.out.println("command  error");
            return "error";
        } else {
           // System.out.println("command right");
        }

        switch((int)command){
            case IrKeyCode.KEY_UP:
                //System.out.println("ir send up");
                try {
                    Robot r = new Robot();//创建自动化工具对象
                    r.keyPress(KeyEvent.VK_UP);
                    r.keyRelease(KeyEvent.VK_UP);
                } catch (Exception e2) {
                    return "error";
                }
                break;
            case IrKeyCode.KEY_DOWN:
                //System.out.println("ir send down");
                try {
                    Robot r = new Robot();//创建自动化工具对象
                    r.keyPress(KeyEvent.VK_DOWN);
                    r.keyRelease(KeyEvent.VK_DOWN);
                } catch (Exception e2) {
                    return "error";
                }
                break;
            case IrKeyCode.KEY_LEFT:
                //System.out.println("ir send left");
                try {
                    Robot r = new Robot();//创建自动化工具对象
                    r.keyPress(KeyEvent.VK_LEFT);
                    r.keyRelease(KeyEvent.VK_LEFT);
                } catch (Exception e2) {
                    return "error";
                }
                break;
            case IrKeyCode.KEY_RIGHT:
                //System.out.println("ir send right");
                try {
                    Robot r = new Robot();//创建自动化工具对象
                    r.keyPress(KeyEvent.VK_RIGHT);
                    r.keyRelease(KeyEvent.VK_RIGHT);
                } catch (Exception e2) {
                    return "error";
                }
                break;
            case IrKeyCode.KEY_MENU:
               //System.out.println("ir send menu");
                break;
            case IrKeyCode.KEY_BACK:
                //System.out.println("ir send back");
                try {
                    Robot r = new Robot();//创建自动化工具对象
                    r.keyPress(KeyEvent.VK_Q);
                    r.keyRelease(KeyEvent.VK_Q);
                } catch (Exception e2) {
                    return "error";
                }
                break;
            default :
                break;
        }

        return str;
    }
}