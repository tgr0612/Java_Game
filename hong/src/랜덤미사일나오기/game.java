package �����̻��ϳ�����;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
public class game
{
    public static void main(String[] ar)
    {
        game_Frame fms = new game_Frame();
    }
}
class Bullet
{
    private int nSize = 6;//�Ѿ��� ũ��
    private int nHalfSize = 3;//�Ѿ��� ��ũ��
    private Point pStart;//���� ��ġ
    private Point pPos;//�Ѿ��� ��ġ
    private Point pVel;//�Ѿ� ������                 ��
    public Bullet(int x, int y)
    {
        pStart = new Point(x, y);//������ġ
        pPos = new Point(x, y);//�Ѿ� ��ġ
        pVel = new Point();//�Ѿ� �����ϰ�
    }
    //���� ���ϱ�
    public float TanGetAngle(Point pt1, Point pt2)
    {
        float fX, fY;
        float fAngle;
        fX = pt2.x - pt1.x;
        fY = pt2.y - pt1.y;
      
        fAngle = (float) Math.atan( -fY / fX );
        if (fX < 0)
        {
            fAngle += Math.PI;
        }
        if (pt2.x >= pt1.x && pt2.y >= pt1.y)
        {
            fAngle += 2 * Math.PI;
        }
        return fAngle;
    }
    //�����ϰ� ���
    public void setMoveValue(int player_x, int player_y)
    {
        Point player = new Point(player_x, player_y);//�÷��̾� ��ġ
        float angle = TanGetAngle(pPos, player);//���� ���
        int speed = (int)(Math.random() * 3) + 3;//�Ѿ˼ӵ� �����ϰ�
       
        pVel.x = (int)(Math.cos(angle) * speed);
        pVel.y = (int)(-Math.sin(angle) * speed);//������ �°� ������ �� ���
    }
    public boolean moveBullet(int width, int hight)
    {
        pPos.x += pVel.x;
        pPos.y += pVel.y;//�Ѿ� �����̱�
        if (( pPos.x + nHalfSize ) < 0 || ( pPos.x - nHalfSize ) > width)//ȭ���� �����
        {
            pPos.x = pStart.x;
            pPos.y = pStart.y;//������ġ��
            return false;//ȭ��ۿ� �������� �˸�
        } else if (( pPos.y + nHalfSize < 0 ) || ( pPos.y - nHalfSize ) > hight)//ȭ���� �����
        {
            pPos.x = pStart.x;
            pPos.y = pStart.y;//������ġ��
            return false;//ȭ��ۿ� �������� �˸�
        }
       
        return true;
    }
    public boolean checkCollision(int player_x, int player_y, int player_size)
    {
        return false;
    }
    //�Ѿ� �׸���
    public void Draw(Graphics g)
    {
        g.setColor(Color.red);
        g.drawOval(pPos.x - nHalfSize, pPos.y - nHalfSize, nSize, nSize);
    }
}
class game_Frame extends JFrame implements KeyListener, Runnable
{
    int f_width = 800;
    int f_height = 600;
    int x, y, bx, by;
    boolean KeyUp = false;
    boolean KeyDown = false;
    boolean KeyLeft = false;
    boolean KeyRight = false;
    Thread th;
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image me_img = tk.getImage("f15k.png");
    Image background_img; //���ȭ�� �̹���
    Image buffImage; //���� ���۸���
    Graphics buffg; //���� ���۸���
   
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();//�Ѿ� ����
    game_Frame()
    {
        init();
        start();
        setTitle("���� ���� �����");
        setSize(f_width, f_height);
        Dimension screen = tk.getScreenSize();
        int f_xpos = (int) ( screen.getWidth() / 2 - f_width / 2 );
        int f_ypos = (int) ( screen.getHeight() / 2 - f_height / 2 );
 

        setLocation(f_xpos, f_ypos);
        setResizable(false);
        setVisible(true);
    }

    public void init()
    {
        background_img = new ImageIcon("background.png").getImage();
        bx = -25;
        by = 15;
        x = 275;
        y = 260;
        f_width = 700;
        f_height = 550;
        Random rnd = new Random(1234);
        int tx, ty, tvx, tvy;
        Bullet b;
        double angle;
        for (int i = 0; i < 10; i++)
        {
            //���� �Ѿ� ����
            tx = rnd.nextInt(f_width);
            ty = 0;
               
            b = new Bullet(tx, ty);//�Ѿ� ����
            b.setMoveValue(x, y);//������ �� ���
           
            bullets.add(b);//�Ѿ� ����
           
            //������ �Ѿ� ����
            tx = f_width;
            ty = rnd.nextInt(f_height);
               
            b = new Bullet(tx, ty);
            b.setMoveValue(x, y);
           
            bullets.add(b);
           
            //�Ʒ��� �Ѿ� ����
            tx = rnd.nextInt(f_width);
            ty = f_height;
               
            b = new Bullet(tx, ty);
            b.setMoveValue(x, y);
           
            bullets.add(b);
           
            //���� �Ѿ� ����
            tx = 0;
            ty = rnd.nextInt(f_height);
               
            b = new Bullet(tx, ty);
            b.setMoveValue(x, y);
           
            bullets.add(b);
        }
    }
    public void start()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        th = new Thread(this);
        th.start();
    }
    public void moveBullets()
    {
        //����� �Ѿ˵��� �����̱�
        for (Bullet b : bullets)
        {
            if(b.moveBullet(f_width, f_height) == false)//ȭ�� ������ ������
            {
                b.setMoveValue(x, y);//������ �� �ٽ� ���
            }
        }
    }
    public void DrawBullets()
    {
        //�Ѿ� �׷��ֱ�
        for (Bullet b : bullets)
        {
            b.Draw(buffg);
        }
    }
    public void run()
    {
        try
        {
            while (true)
            {
                KeyProcess();
                moveBullets();//�Ѿ��� �����̱�
                repaint();
                Thread.sleep(20);
            }
        }
        catch (Exception e)
        {
        }
    }
    public void paint(Graphics g)
    {
        buffImage = createImage(f_width, f_height);
        //������۸� ���� ũ�⸦ ȭ�� ũ��� ���� ����
        buffg = buffImage.getGraphics(); //������ �׷��� ��ü�� ���
        update(g);
    }
    public void update(Graphics g)
    {
        Draw_Background(); //��� �̹��� �׸��� �޼ҵ� ����
        DrawBullets();//�Ѿ� �׷��ֱ�
        Draw_Char();// ������ �׷��� �׸��� �����´�
        g.drawImage(buffImage, 0, 0, this);
    }
    public void Draw_Background()
    {
        buffg.clearRect(0, 0, f_width, f_height);
        buffg.drawImage(background_img, bx, by, this);
    }
    public void Draw_Char()
    { // ������ �׸����� �׸� �κ�
        buffg.drawImage(me_img, x, y, this);
    }
    public void keyPressed(KeyEvent e)
    {
 
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                KeyUp = true;
                break;
            case KeyEvent.VK_DOWN:
                KeyDown = true;
                break;
            case KeyEvent.VK_LEFT:
                KeyLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                KeyRight = true;
                break;
        }
    }
    public void keyReleased(KeyEvent e)
    {

        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                KeyUp = false;
                break;
            case KeyEvent.VK_DOWN:
                KeyDown = false;
                break;
            case KeyEvent.VK_LEFT:
                KeyLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                KeyRight = false;
                break;
        }
    }
    public void keyTyped(KeyEvent e)
    {
    }
    public void KeyProcess()
    {
 
        if (KeyUp == true)
        {
            y -= 5;
        }
        if (KeyDown == true)
        {
            y += 5;
        }
        if (KeyLeft == true)
        {
            x -= 5;
        }
        if (KeyRight == true)
        {
            x += 5;
        }

    }
}