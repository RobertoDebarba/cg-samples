/// \file Exemplo_N2_Jogl_Eclipse.java
/// \brief Exemplo_N2_Jogl_Eclipse: desenha uma linha na diagonal.
/// \version $Revision: 1.0 $
/// \author Dalton Reis.
/// \date 03/05/13.
/// Obs.: variaveis globais foram usadas por questoes didaticas mas nao sao recomendas para aplicacoes reais.

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.event.*;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
    private static final int DENTRO_BBOX = 1;
    private static final int FORA_BBOX = 2;
    private static final int NOCIRCULO = 3;
    private static final int raiomaior = 125;
    private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
    private static final Ponto4D ptoRef = new Ponto4D(  150.0,   150.0, 0.0, 1.0);
    private static final Ponto4D ptoAntigo = new Ponto4D(  ptoRef.obterX(),   ptoRef.obterY(), 0.0, 1.0);
    private static final Ponto4D ptoAbsoluto = new Ponto4D(  ptoRef.obterX(),   ptoRef.obterY(), 0.0, 1.0);
    private static final double x45 = RetornaX(45, raiomaior) + ptoAbsoluto.obterX();
    private static final double y45 = RetornaY(45, raiomaior) + ptoAbsoluto.obterY();
    private static final double x225 = RetornaX(225, raiomaior) + ptoAbsoluto.obterX();
    private static final double y225 = RetornaY(225, raiomaior) + ptoAbsoluto.obterY();
    private static final Ponto4D ptInicial = new Ponto4D(  x225,   y45, 0.0, 1.0);
    private static final Ponto4D ptFinal = new Ponto4D(  x45,   y225, 0.0, 1.0);
    private static final double distanciafixa = distanciaPontos(ptInicial, ptoAbsoluto);
    private static boolean fora = false;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	//exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-50.0f, 350.0f, -50.0f, 350.0f);

		SRU();

		desenharCirculo(ptoAbsoluto.obterX(), ptoAbsoluto.obterY(), raiomaior);
        int situacao = 1;
        if(fora){
            situacao = 3;
        }else{
            if(ptoRef.obterX() > ptInicial.obterX() && ptoRef.obterX() < ptFinal.obterX() &&
                    ptoRef.obterY() < ptInicial.obterY() && ptoRef.obterY() > ptFinal.obterY())
                situacao = 1;
            else if(distanciaPontos(ptInicial, ptoAbsoluto) > distanciaPontos(ptoRef, ptoAbsoluto)){
                situacao = 2;
            }else{
                situacao = 3;
            }
        }
		desenharQuadrado(ptInicial,ptFinal, situacao);

        desenharCirculo(ptoRef.obterX(), ptoRef.obterY(), 50, 1);
        desenharPonto(ptoRef);

		gl.glFlush();
	}

    private static double distanciaPontos(Ponto4D pt1, Ponto4D pt2) {
        return Math.pow(pt1.obterX() - pt2.obterX(), 2)+ Math.pow(pt1.obterY() - pt2.obterY(), 2);
    }

    private void desenharPonto(Ponto4D ponto) {
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glPointSize(5);
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex2d(ponto.obterX(), ponto.obterY());
        gl.glEnd();
    }

    private void desenharQuadrado(Ponto4D ptInicial, Ponto4D ptFinal, int situacao) {
        if(situacao == DENTRO_BBOX)
		    gl.glColor3f(1.0f, 0.0f, 1.0f);
        else if(situacao == FORA_BBOX)
            gl.glColor3f(1.0f, 1.0f, 0.0f);
        else
            gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(ptInicial.obterX(), ptInicial.obterY());
		gl.glVertex2d(ptFinal.obterX(), ptInicial.obterY());
		gl.glVertex2d(ptFinal.obterX(), ptFinal.obterY());
		gl.glVertex2d(ptInicial.obterX(), ptFinal.obterY());
		gl.glEnd();
	}

    private void desenharCirculo(double x, double y, double raio) {
        desenharCirculo(x, y, raio, 2);
    }
    private void desenharCirculo(double x, double y, double raio, float tamanho) {

		// CIRCULO
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glLineWidth(tamanho);
		gl.glBegin(GL.GL_LINE_LOOP);
		for (int angulo = 1; angulo <= 360; angulo++)
			gl.glVertex2d(RetornaX(angulo,raio) + x, RetornaY(angulo,raio) + y);
		gl.glEnd();
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(" --- keyPressed ---");
		System.out.println(e.getKeyCode());
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(" --- reshape ---");
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		System.out.println(" --- displayChanged ---");
	}

	public void keyReleased(KeyEvent arg0) {
		System.out.println(" --- keyReleased ---");
	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println(" --- keyTyped ---");
	}

	public void SRU() {

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin( GL.GL_LINES );
		gl.glVertex2f( -200.0f, 0.0f );
		gl.glVertex2f(  200.0f, 0.0f );
		gl.glEnd();

		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin( GL.GL_LINES);
		gl.glVertex2f(  0.0f, -200.0f);
		gl.glVertex2f(  0.0f, 200.0f );
		gl.glEnd();
	}

	public static double RetornaX(double angulo, double raio) {
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}
	public static double RetornaY(double angulo, double raio) {
		return (raio * Math.sin(Math.PI * angulo / 180.0));
	}

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ptoAntigo.atribuirX(e.getX());
        ptoAntigo.atribuirY(e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ptoRef.atribuirX(ptoAbsoluto.obterX());
        ptoRef.atribuirY(ptoAbsoluto.obterY());
        fora = false;
        glDrawable.display();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double movToX = e.getX() - ptoAntigo.obterX();
        double movToY = e.getY() - ptoAntigo.obterY();

        double resultX = ptoRef.obterX() + movToX;
        double resultY = ptoRef.obterY() - movToY;
        fora = false;
        if(resultX > ptInicial.obterX() && resultX < ptFinal.obterX() &&
                resultY < ptInicial.obterY() && resultY > ptFinal.obterY()) {  }
        else{
            double distanciaflexiva = distanciaPontos(new Ponto4D(resultX, resultY,0,1), ptoAbsoluto);
            if( distanciafixa <  distanciaflexiva){
                resultX = ptoRef.obterX();
                resultY = ptoRef.obterY();
                fora = true;
            }
        }

        ptoRef.atribuirX(resultX);
        ptoRef.atribuirY(resultY);

        System.out.println("posMouse: "+movToX+" / "+movToY);

        ptoAntigo.atribuirX(e.getX());
        ptoAntigo.atribuirY(e.getY());

        glDrawable.display();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
