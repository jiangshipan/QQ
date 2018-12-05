package Server;
import java.net.*;

import java.util.*;



import java.sql.*;
import java.io.*;
public class QQServer {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            //声明存放所有人的Socket的集合
            HashMap<String,Socket> hm = new HashMap<String,Socket>();

            ServerSocket ss = new ServerSocket(8000);
            while(true) {
                System.out.println("服务器正在8000端口监听");

                Socket s = ss.accept();

                MyService t = new MyService();
                t.setSocket(s);
                //将HashMap的引用传入服务器端线程
                t.setHashMap(hm);
                t.start();
            }


        }catch(Exception e) {e.printStackTrace();}//打印异常
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
    public static boolean findFile(String pathName) {
        File file = new File(pathName);
        if (file.isFile())
            return true;
        else
            return false;
    }
    public static class MyService extends Thread{
        private Socket s;
        //接收HashMap的引用
        private HashMap<String , Socket> hm;
        public void setHashMap(HashMap hm) {
            this.hm = hm;
        }
        public void setSocket(Socket s) {
            this.s = s;
        }
        public void run() {
            try {
                //接受用户名和密码
                InputStream is = s.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String uandp = br.readLine();

                //检验点
                System.out.println(uandp);

                //拆分用户名和密码

                String u = uandp.split("%")[0];
                String p = uandp.split("%")[1];
                String flag = uandp.split("%")[2];

                String driver = "com.mysql.jdbc.Driver";
                String url = "jdbc:mysql://127.0.0.1:3306/qq";
                String username = "root";
                String password = "3.3.0.";
                Connection con = null;
                Statement sta = null;
                if(flag.equals("yes!")) {
                    System.out.println("可注册");
                    String sql = "insert into user(username,password)"+"value('"+u+"','"+p+"')";//注意写的格式
                    Class.forName(driver);
                    con = DriverManager.getConnection(url,username,password);
                    sta = con.createStatement();
                    sta.executeUpdate(sql);
                    System.out.println("数据已进入数据库!");
                }
                OutputStream os = s.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter pw = new PrintWriter(osw,true);

                //到数据库中验证用户身份
                Class.forName("com.mysql.jdbc.Driver");//返回与带有给定字符串名的类或接口相关联的 Class 对象
                Connection cn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/qq","root","3.3.0.");
                PreparedStatement ps = cn.prepareStatement("select * from user where username=? and password=?");
                ps.setString(1, u);
                ps.setString(2, p);

                //		ResultSet rs = null;  //表示数据库结果集的数据表
                //      rs = ps.executeQuery();
                ResultSet rs = ps.executeQuery();  //把数据库响应的查询结果存放在ResultSet类对象中供我们使用

                if(rs.next()) {
                    //发送正确信息到客户端
                    pw.println("ok");

                    //将本人的名字发送给其他用户
                    for(Socket ts : hm.values()) {
                        OutputStream tos = ts.getOutputStream();
                        OutputStreamWriter tosw = new OutputStreamWriter(tos);
                        PrintWriter tpw = new PrintWriter(tosw , true);

                        tpw.println("add%"+u);
                    }
                    //将其他人的名字发送给自己
                    for(String tu :hm.keySet()) {
                        pw.println("add%"+tu);
                    }
                    //将本人的用户名和Socket存入HashMap
                    hm.put(u,s);
                    //不断地接受客户端发送过来的信息
                    while(true) {
                        String message = br.readLine();
                        System.out.println(message);
                        String mess1 = message.split("%")[0];
                        String mess2 = message.split("%")[1];

                        //		System.out.println(message);
                        if(mess2.equals("{exit}")){
                            //将该用户从HashMap中删除
                            hm.remove(u);
                            //通知所有人本用户退出
                            for(Socket ts : hm.values()) {
                                OutputStream tos = ts.getOutputStream();
                                OutputStreamWriter tosw = new OutputStreamWriter(tos);
                                PrintWriter tpw = new PrintWriter(tosw , true);

                                tpw.println("exit%"+u);
                            }
                            return ;
                        }
                        if(mess2.equals("receivefile")) {
                            //		String ao = message.split("%")[0];
                            //	Socket ts = hm.get(ao);
                            OutputStream tos = s.getOutputStream();
                            OutputStreamWriter tosw = new OutputStreamWriter(tos);
                            PrintWriter tpw = new PrintWriter(tosw , true);

                            tpw.println("send%"+" ");

                            try {
                                //       ServerSocket sss = new ServerSocket(9999);
                                //     Socket aa = sss.accept();
                                String pathName = "zhongzhuan.txt"; // 空文本，用来接收数据。
                                File file = QQServer.createFile(pathName);
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
                                //    sss.close();

                                System.out.println("数据传输完毕，大小为" + dataCounter + "字节");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String ao = message.split("%")[0];
                            Socket ts = hm.get(ao);
                            OutputStream toas = ts.getOutputStream();
                            OutputStreamWriter toasw = new OutputStreamWriter(toas);
                            PrintWriter tapw = new PrintWriter(toasw , true);
                            tapw.println("receive%"+" ");
                        }
                        if(mess2.equals("ready")) {
                            String to = message.split("%")[0];
                            String mess =message.split("%")[1];

                            System.out.println("i will send it");
                            Socket ts = hm.get(to);

                            try {
                                String pathName = "zhongzhuan.txt";
                                File file = new File(pathName);
                                //     byte[] IP = new byte[] {(byte) 192 ,(byte) 168 ,1,103 };
                                if (!(findFile(pathName))) {
                                    return;
                                }
                                System.out.println("正在连接到目标主机……");
                                //     InetAddress addr = InetAddress.getByAddress();
                                InetAddress addr = ts.getInetAddress();
                                //     Socket s = new Socket(addr, 9999);
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
                            System.out.println("发送完成");

                        }
                        //转发信息
                        String to = message.split("%")[0];
                        String mess =message.split("%")[1];
                        Socket ts = hm.get(to);

                        OutputStream tos = ts.getOutputStream();
                        OutputStreamWriter tosw = new OutputStreamWriter(tos);
                        PrintWriter tpw = new PrintWriter(tosw , true);

                        tpw.println("mess%"+mess);
                        //	System.out.println(message);
                    }
                }
                else {
                    //发送错误信息到客户端
                    pw.println("err");
                }
            }catch(Exception e) {}
        }
    }

}
