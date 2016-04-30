//// AnimDemo3.java
//
//import com.jgoodies.animation.*;
//
//import com.jgoodies.animation.swing.animations.BasicTextAnimation;
//import com.jgoodies.animation.swing.animations.FanAnimation;
//import com.jgoodies.animation.swing.components.BasicTextLabel;
//import com.jgoodies.animation.swing.components.FanComponent;
//
//import java.awt.*;
//import javax.swing.*;
//
//public class AnimDemo3 extends JWindow {
//	final static int DURATION = 30000;
//	final static int FRAME_RATE = 30;
//
//	int duration;
//
//	BasicTextLabel label;
//
//	FanComponent fan;
//
//	public void showSplashScreen() {
//		JPanel content = (JPanel) getContentPane();
//		content.setLayout(new GridLayout(2, 1));
//		content.setBackground(Color.white);
//		content.setBorder(BorderFactory.createLineBorder(Color.blue, 10));
//
//		int width = 500;
//		int height = 200;
//		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
//		int x = (screen.width - width) / 2;
//		int y = (screen.height - height) / 2;
//		setBounds(x, y, width, height);
//
//		label = new BasicTextLabel("");
//		label.setFont(new Font("Tahoma", Font.BOLD, 18));
//		content.add(label);
//
//		fan = new FanComponent(10, Color.green);
//		content.add(fan);
//
//		setVisible(true);
//
//		Animation animation = createAnimation();
//
//		AnimationListener al;
//		al = new AnimationListener() {
//			public void animationStarted(AnimationEvent e) {
//			}
//
//			public void animationStopped(AnimationEvent e) {
//				synchronized ("A") {
//					"A".notify();
//				}
//			}
//		};
//		animation.addAnimationListener(al);
//
//		Animator animator = new Animator(animation, 30);
//		animator.start();
//
//		synchronized ("A") {
//			try {
//				"A".wait();
//			} catch (InterruptedException e) {
//			}
//		}
//	}
//
//	private Animation createAnimation() {
//		Animation a1 = BasicTextAnimation.defaultFade(label, 10000, "Welcome To", Color.orange);
//
//		Animation a2 = BasicTextAnimation.defaultScale(label, 10000, "The JGoodies", Color.cyan);
//
//		Animation a3 = BasicTextAnimation.defaultSpace(label, 10000, "Animation Demonstration!", Color.magenta);
//
//		Animation allSeq = Animations.sequential(new Animation[] { Animations.pause(1000), a1, Animations.pause(1000), a2, Animations.pause(1000),
//				a3, Animations.pause(1000), });
//
//		Animation a4 = FanAnimation.defaultFan(fan, 33000);
//
//		return Animations.parallel(allSeq, a4);
//	}
//
//	public static void main(String[] args) {
//		new AnimDemo3().showSplashScreen();
//		System.exit(0);
//	}
//}