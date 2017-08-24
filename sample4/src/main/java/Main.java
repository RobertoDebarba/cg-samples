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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Ponto4D pto1 = new Ponto4D(  0.0,   0.0, 0.0, 1.0);
	private Ponto4D pto2 = new Ponto4D(100.0, 100.0, 0.0, 1.0);
	private double angulo = 45;
	private double tamanho = 100;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(0.82f, 0.82f, 0.82f, 1.0f);
	}

	//exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-400.0f, 400.0f, -400.0f, 400.0f);

		SRU();

		pto2.atribuirX(RetornaX(angulo, tamanho) + pto1.obterX());
		pto2.atribuirY(RetornaY(angulo, tamanho) + pto1.obterY());

		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glLineWidth(2.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(pto1.obterX(),pto1.obterY());
		gl.glVertex2d(pto2.obterX(),pto2.obterY());
		gl.glEnd();

		gl.glPointSize(9.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2d(pto1.obterX(),pto1.obterY());
		gl.glVertex2d(pto2.obterX(),pto2.obterY());
		gl.glEnd();

		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(" --- keyPressed ---");
		switch (e.getKeyChar()) {
			case 'Q':
			case 'q':
				pto1.atribuirX(pto1.obterX() - 1);
				glDrawable.display();
				break;
			case 'W':
			case 'w':
				pto1.atribuirX(pto1.obterX() + 1);
				glDrawable.display();
				break;
			case 'A':
			case 'a':
				tamanho--;
				glDrawable.display();
				break;
			case 'S':
			case 's':
				tamanho++;
				glDrawable.display();
				break;
			case 'Z':
			case 'z':
				angulo--;
				glDrawable.display();
				break;
			case 'X':
			case 'x':
				angulo++;
				glDrawable.display();
				break;
		}
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

	public double RetornaX(double angulo, double raio) {
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}
	public double RetornaY(double angulo, double raio) {
		return (raio * Math.sin(Math.PI * angulo / 180.0));
	}

}
