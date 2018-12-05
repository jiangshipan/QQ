package Client;


import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;


public class QQlogin extends JFrame implements ActionListener{

    private static final long serialVersionUID = -6788045638380819221L;
    //用户名
    private JTextField ulName;
    //密码
    private JPasswordField ulPasswd;
    //小容器
    private JLabel j1;
    private JLabel j2;
    private JLabel j3;
    private JLabel j4;
    //小按钮
    private JButton b1;
    private JButton b2;
    private JButton b3;
    //复选框
    private JCheckBox c1;
    private JCheckBox c2;
    //列表框
    private JComboBox<String> cb1;
    /**
     * 初始化QQ登录页面
     * */
    public QQlogin(){
        //设置登录窗口标题
        this.setTitle("QQ登录");
        //去掉窗口的装饰(边框)
//      this.setUndecorated(true);
        //采用指定的窗口装饰风格
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        //窗体组件初始化
        init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置布局为绝对定位
        this.setLayout(null);
        this.setBounds(0, 0, 600, 400);

        //窗体大小不能改变
        this.setResizable(false);
        //居中显示
        this.setLocationRelativeTo(null);
        //窗体显示
        this.setVisible(true);

    }
    //  /**
    // * 窗体组件初始化
    // * */
    public void init(){
        //创建一个容器,其中的图片大小和setBounds第三、四个参数要基本一致(需要自己计算裁剪)
        Container container = this.getContentPane();
        j1 = new JLabel();
        //设置背景色
        //得到类路径
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(path + "/utilFile/login.jpg");
        Image img1 = new ImageIcon(path + "/utilFile/login.jpg").getImage();
        j1.setIcon(new ImageIcon(img1));
        j1.setBounds(0, 0, 600, 400);

        //用户名输入框
        ulName = new JTextField();
        ulName.setBounds(365, 120, 200, 30);
        //注册账号
        j3 = new JLabel("账号");
        j3.setFont(new Font("宋体", Font.PLAIN, 15));
        j3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        j3.setBounds(232, 125, 70, 20);
        //密码输入框
        ulPasswd = new JPasswordField();
        ulPasswd.setBounds(365, 180, 200, 30);
        //找回密码
        j4= new JLabel("密码");
        j4.setFont(new Font("宋体", Font.PLAIN, 15));
        j4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        j4.setBounds(232, 185, 70, 20);
        //记住密码
        c1 = new JCheckBox("记住密码");
        c1.setBounds(105, 155, 80, 15);
        //自动登陆
        c2 = new JCheckBox("自动登陆");
        c2.setBounds(185, 155, 80, 15);
        //用户登陆状态选择
        cb1 = new JComboBox<String>();
        cb1.addItem("在线");
        cb1.addItem("隐身");
        cb1.addItem("离开");
        cb1.setBounds(40, 150, 55, 20);
        //登陆按钮
        b1 = new JButton("登录");
        //设置字体和颜色和手形指针
        b1.setFont(new Font("宋体", Font.PLAIN, 12));
        b1.setForeground(Color.RED);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b1.setBounds(460, 300, 100, 30);

        //多账号
        b2 = new JButton("注册");
        b2.setFont(new Font("宋体", Font.PLAIN, 12));
        b2.setForeground(Color.RED);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b2.setBounds(40, 300, 100,30);
        //设置
        b3 = new JButton("取消");
        b3.setFont(new Font("宋体", Font.PLAIN, 12));
        b3.setForeground(Color.RED);
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b3.setBounds(240,300, 100, 30);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        //所有组件用容器装载
        //  j1.add(j2);
        j1.add(j3);
        j1.add(j4);
        //  j1.add(c1);
        // j1.add(c2);
        // j1.add(cb1);
        j1.add(b1);
        j1.add(b2);
        j1.add(b3);
        //  container.add(jlb1);
        container.add(j1);
        container.add(ulName);
        container.add(ulPasswd);
    }
    public static void main(String[] args) {
        QQlogin w = new QQlogin();
        w.setVisible(true);
    }
    public void actionPerformed(ActionEvent arg0) {
        if(arg0.getActionCommand().equals("登录")) {
            //发送用户名和密码到服务器端
            try{
                String username = ulName.getText();
                String userpassword = ulPasswd.getText();
                String flag = "no!";
                Socket s = new Socket("132.232.101.33",8000);
                OutputStream os = s.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter pw = new PrintWriter(osw,true);
                pw.println(username+"%"+userpassword+"%"+flag);
                //接受服务器端发送回来的确认信息
                InputStream is = s.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String yorn = br.readLine();
                //显示主窗口
                if(yorn.equals("ok")) {
                    QQMain w =new QQMain();
                    w.setSocket(s);
                    w.setVisible(true);
                    this.setVisible(false);
                }
                else {
                    JOptionPane.showMessageDialog(this, "对不起,用户名或密码错误");//告知用户某事已经发生
                }

            }catch(Exception e) {}
        }
        if(arg0.getActionCommand().equals("注册")) {
            this.setVisible(false);
            writeSQL n = new writeSQL();
            n.setVisible(true);
        }
        if(arg0.getActionCommand().equals("取消")) {
            System.exit(0);
        }

    }

}