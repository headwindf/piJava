import com.pi4j.wiringpi.GpioInterruptCallback;

public class InterruptCallback implements GpioInterruptCallback {

    private String direction = "UKNOWN";
    private static long lastTime = 0L;
    private static long nowTime = 0L;
    private static boolean startFlag = false;
    private static int count = 0;

    private static long highTimeMin = 1800000L;
    private static long highTimeMax = 8000000L;
    private static long lowTimeMin = 150000L;
    private static long lowTimeMax = 1800000L;
    private static long headTimeMin = 10000000L;
    private static long headTimeMax = 14000000L;

    private static long dataRec = 0L;

    public static StringBuffer sBuffer = new StringBuffer("");

    public static boolean isReceieve = false;

    public InterruptCallback(String direction){
        this.direction = direction;
    }



    @Override
    public void callback(int pin) {
        //System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED <" + direction.toUpperCase() + ">");      

        nowTime = System.nanoTime();   //获取开始时间 
        long timeTmp = nowTime - lastTime;
        //System.out.println("timeTmp:      "+timeTmp);
        
        if(startFlag){
            if(timeTmp>highTimeMin && timeTmp<highTimeMax){//1
                count ++;
                dataRec |= (1<<(32-count));
                //System.out.println("high or low:  1");
                sBuffer.append("1");
            } else if(timeTmp>lowTimeMin && timeTmp<lowTimeMax){//0
                count ++;
                //System.out.println("high or low:  0");
                sBuffer.append("0");
            } else{//错误，结束
                sBuffer.setLength(0);
                count = 0;
                startFlag = false;
                //System.out.println("err!  end!");
            }

            if (count == 32) {
                count = 0;
              // System.out.println("sBuffer:  "+sBuffer);
                //System.out.println("data end!");
                //decodeIRData(sBuffer);

                DecodeIRData decodeIRData = new DecodeIRData(sBuffer.toString());
                decodeIRData.start();
                //System.out.println(this.sBuffer.toString());
                isReceieve = true;
                sBuffer.setLength(0);
            }
        }

        if(timeTmp>headTimeMin && timeTmp<headTimeMax){
            startFlag = true;
           // System.out.println("start!");
        } 
        //System.out.println("count:  "+count);
       // System.out.println("dataRec:  "+dataRec);
        
        
        //System.out.println("high or low:  "+(((timeTmp) > 1500000L)?1:0));
        lastTime = nowTime;
    }


}