package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class writeSQL extends JFrame implements ActionListener {

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
    public writeSQL(){
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
        this.setBounds(0, 0, 500, 300);

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
        System.out.println(path + "utilFile/register.jpg");
        Image img1 = new ImageIcon(path + "/utilFile/register.jpg").getImage();
        j1.setIcon(new ImageIcon(img1));
        j1.setBounds(0, 0, 600, 400);

        //用户名输入框
        ulName = new JTextField();
        ulName.setBounds(256, 95, 150, 20);
        //注册账号
        j3 = new JLabel("账号");
        j3.setFont(new Font("宋体", Font.PLAIN, 15));
        j3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        j3.setBounds(202, 95, 70, 20);
        //密码输入框
        ulPasswd = new JPasswordField();
        ulPasswd.setBounds(256, 135, 150, 20);
        //找回密码
        j4= new JLabel("密码");
        j4.setFont(new Font("宋体", Font.PLAIN, 15));
        j4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        j4.setBounds(202, 135, 70, 20);


        //登陆按钮
        b1 = new JButton("返回");
        //设置字体和颜色和手形指针
        b1.setFont(new Font("宋体", Font.PLAIN, 12));
        b1.setForeground(Color.RED);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b1.setBounds(315, 235, 85, 20);

        //多账号
        b2 = new JButton("确定");
        b2.setFont(new Font("宋体", Font.PLAIN, 12));
        b2.setForeground(Color.RED);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b2.setBounds(20, 235, 85,20);
        //设置
        b3 = new JButton("取消");
        b3.setFont(new Font("宋体", Font.PLAIN, 12));
        b3.setForeground(Color.RED);
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b3.setBounds(170,235, 85, 20);
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
 /*   public static void main(String[] args) {
        QQlogin w = new QQlogin();
        w.setVisible(true);
    }*/
    public void actionPerformed(ActionEvent arg0) {
        if(arg0.getActionCommand().equals("确定")) {
            try{
                String username = ulName.getText();
                String userpassword = ulPasswd.getText();
                Socket s = new Socket("132.232.101.33",8000);

                OutputStream os = s.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter pw = new PrintWriter(osw,true);
                pw.println(username+"%"+userpassword+"%"+"yes!");

                //		String driver = "com.mysql.jdbc.Driver";
                //		String url = "jdbc:mysql://127.0.0.1:3306/qq";
                //		String username = "program";
                //		String password = "19980502";
	/*		Connection cn = null;
			Statement st = null;

			String sql = "insert into user(username,password)"+"value('"+inuser+"','"+inpass+"')";//注意写的格式
			Class.forName(driver);
			cn = DriverManager.getConnection(url,username,password);
			st = cn.createStatement();
			st.executeUpdate(sql);
			System.out.println("数据已进入数据库!");*/
                JOptionPane.showMessageDialog(this, "注册成功!");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        if(arg0.getActionCommand().equals("取消")) {
            this.setVisible(false);
            QQlogin q = new QQlogin();
            q.setVisible(true);
        }
        if(arg0.getActionCommand().equals("返回")) {
            this.setVisible(false);
            QQlogin q = new QQlogin();
            q.setVisible(true);
        }


    }

}
