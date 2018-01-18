import javax.xml.bind.DatatypeConverter;

public class UartPackage {
	private static int CRCByteLen = 2;
	private static byte buffHead = 0x23;
    private byte dataLen;
    private byte[] buffCRC = new byte[CRCByteLen];
    private static byte buffFoot = 0x24;
    private byte timeOut;
    private byte[] buffData;
    private boolean startFlag = false;
    private static int buffMAX = 50;
    private String dataString;
    
    /*
    public static void main(String[] args) { 
    	UartPackage uartPackage = new UartPackage();
    	byte[] sendByte = uartPackage.createPackageData("menu", 4);
		String helloHex = DatatypeConverter.printHexBinary(sendByte);  
		System.out.printf("%s\n", helloHex);   	
		
		System.out.println(uartPackage.unzipPackage("23,09,6D,65,6E,75,34,E1,24"));
    }
    */
    public void setDataString(String dataString) {
		this.dataString = dataString;
	}
    
    public String getDataString() {
		return this.dataString;
	}
    
    public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}
    
    public boolean getStartFlag() {
		return this.startFlag;
	}
    
    public void setBuffData(byte[] buffData) {
		this.buffData = buffData;
	}
    
    public byte[] getBuffData() {
		return this.buffData;
	}
    
    public byte getDataLen() {
		return this.dataLen;
	}
    
    public void setDataLen(byte dataLen) {
		this.dataLen = dataLen;
	}
    
    public void setBuffCRC(byte[] buffCRC) {
		this.buffCRC = buffCRC;
	}
    
    public byte[] getBuffCRC() {
		return this.buffCRC;
	}
    
    public void setTimeOut(byte timeOut) {
		this.timeOut = timeOut;
	}
    
    public byte getTimeOut() {
		return this.timeOut;
	}
    
    public byte[] createPackageData(String str,int len)
    {
        byte[] temp = new byte[len+5];
        byte[] tmp = new byte[len];
        byte[] crctmp = new byte[2];
        int i;
        temp[0] = UartPackage.buffHead;
        temp[1] = (byte)(len + 5);
        for (i = 0; i < len; i++)
        {
            temp[i+2] = (byte)(str.charAt(i));
            tmp[i] = (byte)(str.charAt(i));
        }
        crctmp = crc16(tmp);
        temp[i + 2] = crctmp[0];
        temp[i + 1 + 2] = crctmp[1];
        temp[i + 2 + 2] = UartPackage.buffFoot;
        return temp;
    }

    /// <summary>
    ///CRC16校验算法,（低字节在前，高字节在后）
    /// </summary>
    /// <param name="data">要校验的数组</param>
    /// <returns>返回校验结果，低字节在前，高字节在后</returns>
    public static byte[] crc16(byte[] data)
    {
    	try {
    		if (data.length == 0)
                throw new Exception("调用CRC16校验算法,（低字节在前，高字节在后）时发生异常，异常信息：被校验的数组长度为0。");           
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
        byte[] temdata = new byte[2];
        char xda=0;
        int i;
        byte  j;

        for (i =0 ; i <data.length; i++)
        {
            xda ^= data[i];
            for (j = 0; j < 8; ++j)
            {
                if ((xda & 0x01) == 1)
                    xda = (char)((xda >> 1) ^ 0xA001);    // 0xA001 = reverse 0x8005  
                else
                    xda = (char)(xda >> 1);
            }
        }
        temdata = new byte[2] ;//{ (byte)((xda >> 8) & 0xFF), (byte)(xda & 0xFF) }
        temdata[0] = (byte)((xda >> 8) & 0xFF);
        temdata[1] = (byte)(xda & 0xFF);
        return temdata;
    }
    
    public static byte[] hexToBytes(String hexString) {   
        if (hexString == null || hexString.equals("")) {   
            return null;   
        }   

        int length = hexString.length() / 2;   
        char[] hexChars = hexString.toCharArray();   
        byte[] bytes = new byte[length];   
        String hexDigits = "0123456789ABCDEF";
        for (int i = 0; i < length; i++) {   
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if(h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);   
        }   
        return bytes;   
    }
    
    public String unzipPackage(String data) {
    	UartPackage pack = new UartPackage();
		String strTmp = data.replace(",", "");
		
		byte[] dataTmp = hexToBytes(strTmp);
		
		//帧头
		if(dataTmp[0] != UartPackage.buffHead) {
			System.err.println("帧头错误");
			return "error";
		}

		//帧尾
		if(dataTmp[dataTmp.length-1] != UartPackage.buffFoot) {
			System.err.println("帧尾错误");
			return "error";
		}
		
		pack.dataLen = dataTmp[1];
		
		if(pack.dataLen != dataTmp.length) {
			System.err.println("数据长度不一致");
			return "error";
		}
		
		pack.buffData = new byte[dataTmp.length-5];
		System.arraycopy(dataTmp,2,pack.buffData,0,pack.buffData.length);
		
		//String helloHex = DatatypeConverter.printHexBinary(pack.buffData);  
		//System.out.printf("%s\n", helloHex);  
		
		pack.buffCRC = crc16(pack.buffData);

		if(pack.buffCRC[0] != dataTmp[dataTmp.length-3] || pack.buffCRC[1] != dataTmp[dataTmp.length-2]) {
			System.err.println("CRC校验不一致");
			return "error";
		}
		
		String strRes = new String(pack.buffData);
        pack.dataString = strRes;
		
		return pack.dataString;
	}
    
}
