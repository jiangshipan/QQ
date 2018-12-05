package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class QQMain extends JFrame implements ActionListener,Runnable,WindowListener{

    private Socket s;
    public void setSocket(Socket value) {
        s = value;
        //启动接受线程
        Thread t = new Thread(this);
        t.start();
    }
    JTextField txtMess = new JTextField();

    JComboBox cmbUser = new JComboBox();//下拉框
    JTextArea txtContent = new JTextArea();//文本内容,显示纯文本的多行区域
    QQMain(){
        this.setSize(500,600);
        //居中显示
        this.setLocationRelativeTo(null);
        //窗体显示
        this.setVisible(true);
        //窗体大小不能改变
        this.setResizable(false);
        //new组件
        JButton btnSend = new JButton("发送");
        JButton btnExit = new JButton("退出");
        JButton btnSendFile = new JButton("文件");

        JScrollPane spContent = new JScrollPane(txtContent);//JScrollPane 管理视口、可选的垂直和水平滚动条以及可选的行和列标题视口.滚动条

        //注册事件监听
        btnSend.addActionListener(this);
        btnExit.addActionListener(this);
        btnSendFile.addActionListener(this);
        //布置小面板
        JPanel panSmall = new JPanel();
        panSmall.setLayout(new GridLayout(1,2));

        panSmall.add(cmbUser);
        panSmall.add(btnSend);
        panSmall.add(btnExit);
        panSmall.add(btnSendFile);
        //布置大面板
        JPanel panBig = new JPanel();
        panBig.setLayout(new GridLayout(2,1));

        panBig.add(txtMess);
        panBig.add(panSmall);
        //布置窗体
        this.setLayout(new BorderLayout());

        this.add(panBig,BorderLayout.SOUTH);
        this.add(spContent, BorderLayout.CENTER);

        //读聊天记录
	/*	try {
			File f = new File("c:/work/聊天记录.qq");

			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			while(br.ready()) {
				txtContent.append(br.readLine()+"\n");
			}
		}catch(Exception e) {}*/
    }
    public void actionPerformed(ActionEvent arg0) {
        if(arg0.getActionCommand().equals("退出")) {

            try {
                OutputStream os = s.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter pw = new PrintWriter(osw,true);
                pw.println(cmbUser.getSelectedItem()+"%"+"{exit}");
            }catch(Exception e) {}
            System.exit(0);
        }
        if(arg0.getActionCommand().equals("发送")) {
            //将txtMess->txtContent   \n 这样每次输入都会产生新的一行，同事清空txtMess
            txtContent.append("我:  "+txtMess.getText()+"\n");//append将给定文本追加到文档结尾.getText()返回此 TextComponent 中包含的文本。
            //将txtMess的内容存入聊天记录文件
            try {
                File f = new File("/utilFile/history.qq");

                FileWriter fw = new FileWriter(f,true);//写入字符文件,true说明成追加模式
                PrintWriter pw = new PrintWriter(fw);//字符输出流，带缓冲区
                pw.println(txtMess.getText());
                pw.close();
            }catch(Exception e) {}

            //发送信息到服务器端
            try {
                OutputStream os = s.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter pw = new PrintWriter(osw,true);
                pw.println(cmbUser.getSelectedItem()+"%"+txtMess.getText());
            }catch(Exception e) {}
            //清除txtMess中的内容
            txtMess.setText("");//将此 TextComponent 文本设置为指定文本。如果该文本为 null 或空，则具有只删除旧文本的效果。
        }

        if(arg0.getActionCommand().equals("文件")) {
            try {
                try {
                    OutputStream os = s.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    PrintWriter pw = new PrintWriter(osw,true);
                    pw.println(cmbUser.getSelectedItem()+"%"+"receivefile");
                }catch(Exception e) {}
			/*
				File outfile = null;
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				    outfile = chooser.getSelectedFile();
				}*/
		/*		FileInputStream fis = new FileInputStream(outfile);
				byte[] b = new byte[1024];
				int len = fis.available()/1024+1;
				for(int i = 0 ; i < len ; i++){
					fis.read(b);
				}

				fis.close();


*/
		/*	  DatagramSocket ds = new DatagramSocket(5555, InetAddress.getByName("192.168.1.104"));
			  InputStream in = new FileInputStream(new File("E:\\java\\a.txt"));
			  byte[] b = new byte[1024];
			  int c = in.read(b);
			  in.close();
			  DatagramPacket dp = new DatagramPacket(b, c);
			  ds.send(dp);
			*/
				/*
				DatagramPacket packet = null;
	        	DatagramSocket socket = new DatagramSocket();
	            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("E:\\Java\\a.txt")));
	            byte[] buffer = new byte[1024];
	            int len;
	            while ((len = bis.read(buffer)) != -1) {
	            	packet = new DatagramPacket(buffer, buffer.length,InetAddress.getByName("127.0.0.1"),10000);
		            while (true) {
	                socket.send(packet);
	                System.out.println(new String(packet.getData()));
	                break;
	    	        }
	            }
	            socket.send(packet);
	            socket.close(); */



            }catch(Exception e) {}
        }
    }
    public static boolean findFile(String pathName) {
        File file = new File(pathName);
        if (file.isFile())
            return true;
        else
            return false;
    }
    public static File createFile(String pathName) {
        File file = new File(pathName);
        if (file.exists() && file.isFile()) {
            System.out.println("使用已存在的文件");
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    //接收线程
    public void run() {
        try {
            InputStream is = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while(true) {
                String message = br.readLine();
                System.out.println(message);

                //		String to = message.split("%")[0];
                String type = message.split("%")[0];
                String mess = message.split("%")[1];
                if(type.equals("add")) {
                    cmbUser.addItem(mess);
                }
                if(type.equals("exit")) {
                    cmbUser.removeItem(mess);
                }
                if(type.equals("mess")) {
                    txtContent.append(cmbUser.getSelectedItem() + ":   "+mess+"\n");
                }
                if(type.equals("send")) {
                    //	String pathName = "E:\\Java\\111.jpg";
                    //  File file = new File(pathName);

                    File file = null;
                    JFileChooser chooser = new JFileChooser();
                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        file = chooser.getSelectedFile();
                    }
                    byte[] IP = new byte[] {(byte) 133 ,(byte) 232 ,101,(byte) 33 };
			      /*  if (!(findFile(pathName))) {
			            return;
			        }*/
                    System.out.println("正在连接到目标主机……");
                    try {
                        System.out.println("正在连接到目标主机...");
                        InetAddress addr = InetAddress.getByAddress(IP);

                        //       Socket s = new Socket(addr, 9999);
                        OutputStream output = s.getOutputStream();
                        FileInputStream fis = new FileInputStream(file);
                        System.out.println("连接成功，正在传输数据...");
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        int dataCounter = 0;
                        while ((len = fis.read(buffer)) != -1) {
                            output.write(buffer, 0, len);
                            dataCounter += len;
                        }
                        fis.close();
                        output.close();
                        s.close();
                        System.out.println("数据传送完毕，共传送" + dataCounter + "字节。");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(type.equals("receive")) {
                    JOptionPane.showMessageDialog(this, "您将要接收这个文件");

                    //	 File infile = null;


					    /*        InputStream in = s.getInputStream();
					            byte[] tmp = new byte[1024];

					            JFileChooser chooser = new JFileChooser();
					            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					                infile = chooser.getSelectedFile();
					            }
					            FileOutputStream fos = new FileOutputStream(infile);
					            int length = in.available()/1024+1;
					            for(int i = 0 ; i < length ; i++){
									fos.write(tmp);
								}
					            fos.close();*/

					/*
				            InputStream in = s.getInputStream();
				            byte[] buf = new byte[1024];
				            int len = 0;
				            JFileChooser chooser = new JFileChooser();
				            if (chooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
				                infile = chooser.getSelectedFile();
				            }
				            OutputStream writer1 = new FileOutputStream(infile);
				            while ((len = in.read(buf)) > 0) {
				                writer1.write(buf, 0, len);
				                writer1.flush();
				            }
				            writer1.close();
				            this.dispose();
				            */
					/*	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("E:\\Java\\b.txt")));
				    		DatagramSocket socket = new DatagramSocket(10000);

				            byte[] buf = new byte[1024];

				    		DatagramPacket packet = new DatagramPacket(buf, buf.length);

				            System.out.println("等待接收");



				            socket.receive(packet);
				            System.out.println(packet.getData());

				            bos.write(packet.getData(), 0, packet.getLength());
				            bos.flush();

				            bos.close();
				            socket.close();
						*/
                    try {
                        OutputStream os = s.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        PrintWriter pw = new PrintWriter(osw,true);
                        //	pw.println("ready%");
                        pw.println(cmbUser.getSelectedItem()+"%"+"ready");
                        System.out.println("i am ready");
                    }catch(Exception e) {}
                    try {
                        //      ServerSocket sss = new ServerSocket(9999);
                        //     Socket aa = sss.accept();
                        //  String pathName = "E:\\Java\\444.jpg"; // 空文本，用来接收数据。
                        //  File file = QQMain.createFile(pathName);
                        File file = null;
                        JFileChooser chooser = new JFileChooser();
                        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                            file = chooser.getSelectedFile();
                        }
                        InputStream in = s.getInputStream();
                        FileOutputStream fos = new FileOutputStream(file);
                        System.out.println("开始接数据...");
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        int dataCounter = 0;
                        while ((len = in.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            dataCounter += len;
                        }
                        fos.close();
                        in.close();
                        s.close();
                        //    ss.close();
                        System.out.println("数据传输完毕，大小为" + dataCounter + "字节");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                }
            }
        }catch(Exception e) {}
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void windowClosing(WindowEvent e) {
        // TODO Auto-generated method stub
        try{	OutputStream os = s.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            PrintWriter pw = new PrintWriter(osw , true);

            pw.println("{exit}");
            //正常退出
            System.exit(0);
        }catch(Exception e1) {}
    }
    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

}
