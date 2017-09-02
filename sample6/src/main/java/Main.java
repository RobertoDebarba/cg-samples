/// \file Exemplo_N2_Jogl_Eclipse.java
/// \brief Exemplo_N2_Jogl_Eclipse: desenha uma linha na diagonal.
/// \version $Revision: 1.0 $
/// \author Dalton Reis.
/// \date 03/05/13.
/// Obs.: variaveis globais foram usadas por questoes didaticas mas nao sao recomendas para aplicacoes reais.

import java.awt.event.*;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Ponto4D pto1 = new Ponto4D(  100.0,   -100.0, 0.0, 1.0);
	private Ponto4D pto2 = new Ponto4D(100.0, 100.0, 0.0, 1.0);
	private Ponto4D pto3 = new Ponto4D(  -100.0,   100.0, 0.0, 1.0);
	private Ponto4D pto4 = new Ponto4D(-100.0, -100.0, 0.0, 1.0);

	private double antigoX = 0.0f;
	private double antigoY = 0.0f;

	private Ponto4D ptoSelecionado = pto4;

	private double splineX = 0.0f;
	private double splineY = 0.0f;

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
		glu.gluOrtho2D(-400.0f, 400.0f, -400.0f, 400.0f);

		SRU();

		//Poliedro
		gl.glLineWidth(3.0f);
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex2d(pto1.obterX(), pto1.obterY());
		gl.glVertex2d(pto2.obterX(), pto2.obterY());
		gl.glVertex2d(pto3.obterX(), pto3.obterY());
		gl.glVertex2d(pto4.obterX(), pto4.obterY());
		gl.glEnd();

		//Ponto selecionado
		gl.glPointSize(10.0f);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2d(ptoSelecionado.obterX(), ptoSelecionado.obterY());
		gl.glEnd();

		//Spline
		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINE_STRIP);
		for(int i = 0; i <= 20; i++){
			calcularSpline(i);
			gl.glVertex2d(splineX, splineY);
		}
		gl.glEnd();

		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(" --- keyPressed ---");
		System.out.println(e.getKeyCode());
		switch (e.getKeyCode()) {
			case 49: //Tecla "1"
				ptoSelecionado = pto4;
				break;
			case 50: //Tecla "2"
				ptoSelecionado = pto3;
				break;
			case 51: //Tecla "3"
				ptoSelecionado = pto2;
				break;
			case 52: //Tecla "4"
				ptoSelecionado = pto1;
				break;
		}

		glDrawable.display();
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
//		gl.glDisable(gl.GL_TEXTURE_2D);
//		gl.glDisableClientState(gl.GL_TEXTURE_COORD_ARRAY);
//		gl.glDisable(gl.GL_LIGHTING); //TODO: [D] FixMe: check if lighting and texture is enabled

		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin( GL.GL_LINES );
		gl.glVertex2f( -200.0f, 0.0f );
		gl.glVertex2f(  200.0f, 0.0f );
		gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin( GL.GL_LINES);
		gl.glVertex2f(  0.0f, -200.0f);
		gl.glVertex2f(  0.0f, 200.0f );
		gl.glEnd();
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		antigoX = e.getX();
		antigoY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		double movToX = e.getX() - antigoX;
		double movToY = e.getY() - antigoY;

		ptoSelecionado.atribuirX(ptoSelecionado.obterX() + movToX);
		ptoSelecionado.atribuirY(ptoSelecionado.obterY() - movToY);

		System.out.println("posMouse: "+movToX+" / "+movToY);

		antigoX = e.getX();
		antigoY = e.getY();

		glDrawable.display();
	}

	public void mouseMoved(MouseEvent e) {}

	public void calcularSpline(int t){
		double P4P3X = pto4.obterX() + (pto3.obterX() - pto4.obterX()) * t * 0.05;
		double P4P3Y = pto4.obterY() + (pto3.obterY() - pto4.obterY()) * t * 0.05;

		double P3P2X = pto3.obterX() + (pto2.obterX() - pto3.obterX()) * t * 0.05;
		double P3P2Y = pto3.obterY() + (pto2.obterY() - pto3.obterY()) * t * 0.05;

		double P2P1X = pto2.obterX() + (pto1.obterX() - pto2.obterX()) * t * 0.05;
		double P2P1Y = pto2.obterY() + (pto1.obterY() - pto2.obterY()) * t * 0.05;

		double P4P3P2X = P4P3X + (P3P2X - P4P3X) * t * 0.05;
		double P4P3P2Y = P4P3Y + (P3P2Y - P4P3Y) * t * 0.05;

		double P3P2P1X = P3P2X + (P2P1X - P3P2X) * t * 0.05;
		double P3P2P1Y = P3P2Y + (P2P1Y - P3P2Y) * t * 0.05;

		splineX = P4P3P2X + (P3P2P1X - P4P3P2X) * t * 0.05;
		splineY = P4P3P2Y + (P3P2P1Y - P4P3P2Y) * t * 0.05;
	}
}
