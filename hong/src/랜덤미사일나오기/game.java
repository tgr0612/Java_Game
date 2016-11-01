package 랜덤미사일나오기;

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
    private int nSize = 6;//총알의 크기
    private int nHalfSize = 3;//총알의 반크기
    private Point pStart;//시작 위치
    private Point pPos;//총알의 위치
    private Point pVel;//총알 움직일                 값
    public Bullet(int x, int y)
    {
        pStart = new Point(x, y);//시작위치
        pPos = new Point(x, y);//총알 위치
        pVel = new Point();//총알 움직일값
    }
    //각도 구하기
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
    //움직일값 계산
    public void setMoveValue(int player_x, int player_y)
    {
        Point player = new Point(player_x, player_y);//플레이어 위치
        float angle = TanGetAngle(pPos, player);//각도 계산
        int speed = (int)(Math.random() * 3) + 3;//총알속도 랜덤하게
       
        pVel.x = (int)(Math.cos(angle) * speed);
        pVel.y = (int)(-Math.sin(angle) * speed);//각도에 맞게 움직일 값 계산
    }
    public boolean moveBullet(int width, int hight)
    {
        pPos.x += pVel.x;
        pPos.y += pVel.y;//총알 움직이기
        if (( pPos.x + nHalfSize ) < 0 || ( pPos.x - nHalfSize ) > width)//화면을 벗어나면
        {
            pPos.x = pStart.x;
            pPos.y = pStart.y;//시작위치로
            return false;//화면밖에 나갔음을 알림
        } else if (( pPos.y + nHalfSize < 0 ) || ( pPos.y - nHalfSize ) > hight)//화면을 벗어나면
        {
            pPos.x = pStart.x;
            pPos.y = pStart.y;//시작위치로
            return false;//화면밖에 나갔음을 알림
        }
       
        return true;
    }
    public boolean checkCollision(int player_x, int player_y, int player_size)
    {
        return false;
    }
    //총알 그리기
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
    Image background_img; //배경화면 이미지
    Image buffImage; //더블 버퍼링용
    Graphics buffg; //더블 버퍼링용
   
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();//총알 저장
    game_Frame()
    {
        init();
        start();
        setTitle("슈팅 게임 만들기");
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
            //위쪽 총알 생성
            tx = rnd.nextInt(f_width);
            ty = 0;
               
            b = new Bullet(tx, ty);//총알 생성
            b.setMoveValue(x, y);//움직일 값 계산
           
            bullets.add(b);//총알 저장
           
            //오른쪽 총알 생성
            tx = f_width;
            ty = rnd.nextInt(f_height);
               
            b = new Bullet(tx, ty);
            b.setMoveValue(x, y);
           
            bullets.add(b);
           
            //아래쪽 총알 생성
            tx = rnd.nextInt(f_width);
            ty = f_height;
               
            b = new Bullet(tx, ty);
            b.setMoveValue(x, y);
           
            bullets.add(b);
           
            //왼쪽 총알 생성
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
        //저장된 총알들을 움직이기
        for (Bullet b : bullets)
        {
            if(b.moveBullet(f_width, f_height) == false)//화면 밖으로 나가면
            {
                b.setMoveValue(x, y);//움직일 값 다시 계산
            }
        }
    }
    public void DrawBullets()
    {
        //총알 그려주기
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
                moveBullets();//총알을 움직이기
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
        //더블버퍼링 버퍼 크기를 화면 크기와 같게 설정
        buffg = buffImage.getGraphics(); //버퍼의 그래픽 객체를 얻기
        update(g);
    }
    public void update(Graphics g)
    {
        Draw_Background(); //배경 이미지 그리기 메소드 실행
        DrawBullets();//총알 그려주기
        Draw_Char();// 실제로 그려진 그림을 가져온다
        g.drawImage(buffImage, 0, 0, this);
    }
    public void Draw_Background()
    {
        buffg.clearRect(0, 0, f_width, f_height);
        buffg.drawImage(background_img, bx, by, this);
    }
    public void Draw_Char()
    { // 실제로 그림들을 그릴 부분
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