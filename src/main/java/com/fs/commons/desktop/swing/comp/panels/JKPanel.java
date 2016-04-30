package com.fs.commons.desktop.swing.comp.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.graphics.GraphicsFactory;
import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.FSAbstractComponent;
import com.fs.commons.desktop.swing.comp.JKErrorLabel;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.exception.UIValidationException;
import com.fs.commons.locale.Lables;

/**
 */
public class JKPanel<T> extends JPanel implements UIPanel, BindingComponent<T> {
	private GradientType gradientType;
	private Color gredientColor;
	private Image backGroundImage;
	protected FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean transfer;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private DataSource manager;

	public JKPanel() {
		// super(new FlowLayout(FlowLayout.CENTER, 0, 0));
		initJKPanel();
	}

	public JKPanel(LayoutManager layout) {
		super(layout);
		initJKPanel();
	}

	public JKPanel(JComponent component) {
		this();
		add(component);
	}

	public JKPanel(GradientType gradientType) {
		this.gradientType = gradientType;
	}

	public JKPanel(JComponent container, String title) {
		this(container);
		setTitle(title);
	}

	public void setTitle(String title) {
		setBorder(SwingUtility.createTitledBorder(title));
	}

	private void initJKPanel() {
		setOpaque(false);
		// setDoubleBuffered(false);
		setLocale(SwingUtility.getDefaultLocale());
		setFocusable(false);
		setBackground(SwingUtility.getDefaultBackgroundColor());
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == e.BUTTON3) {
					setVisible(false);
				}
			}
		});
	}

	@Override
	public void requestFocus() {
		Runnable runnable = new Runnable() {
			public void run() {
				transferFocus();
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 * 
	 * @throws DaoException
	 */
	public void resetComponents() throws DaoException {
	}

	@Override
	public void setEnabled(boolean enabled) {
		SwingUtility.enableContainer(this, enabled);
	}

	// ///////////////////////////////////////////////////////////
	public void setPreferredSize(int width, int height) {
		super.setPreferredSize(new Dimension(width, height));
	}

	// ///////////////////////////////////////////////////////////
	public void paint(Graphics g) {
		if (gradientType != null) {
			Color newColor = getGredientColor(false);
			GraphicsFactory.makeGradient(this, g, getBackground(), newColor, gradientType);
		}
		super.paint(g);
	};

	/**
	 * 
	 * @return
	 */
	private Color getGredientColor(boolean recalculate) {
		if (gredientColor == null || recalculate) {
			gredientColor = GraphicsFactory.createGradientColor(getBackground());
		}
		return gredientColor;
	}

	public void setGredientColor(Color gredientColor) {
		this.gredientColor = gredientColor;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		getGredientColor(true);
	}

	/**
	 * @return the gradientType
	 */
	public GradientType getGradientType() {
		return gradientType;
	}

	/**
	 * @param gradientType
	 *            the gradientType to set
	 */
	public void setGradientType(GradientType gradientType) {
		this.gradientType = gradientType;
	}

	@Override
	public T getValue() {
		String methodName = "getValue ";
		return handleUnAllowedMethodCall(methodName);
	}

	@Override
	public void setValue(T value) {
		String methodName = "setValue ";
		handleUnAllowedMethodCall(methodName);
	}

	@Override
	public T getDefaultValue() {
		String methodName = "getDefaultValue ";
		return handleUnAllowedMethodCall(methodName);
	}

	@Override
	public void setDefaultValue(T t) {
		String methodName = "setDefaultValue ";
		handleUnAllowedMethodCall(methodName);
	}

	@Override
	public void reset() {
		String methodName = "reset ";
		handleUnAllowedMethodCall(methodName);
	}

	@Override
	public void clear() {
		String methodName = "clear ";
		handleUnAllowedMethodCall(methodName);
	}

	/**
	 * @param methodName
	 */
	private T handleUnAllowedMethodCall(String methodName) {
		// System.err.println(this.getClass().getName());
		// System.err.println("calling " + methodName +
		// "on JKPanel is not allowed");
		return null;
	}

	public void refreshComponents() {
		validate();
		repaint();
	}

	@Override
	public void filterValues(BindingComponent component) {

	}

	@Override
	public void addValidator(Validator validator) {
		fsWrapper.addValidator(validator);
	}

	@Override
	public void validateValue() throws ValidationException {
		//validateValues();
		fsWrapper.validateValue();
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	public void validateValues(JKErrorLabel fslblErrorl) throws ValidationException {
		StringBuffer buf = new StringBuffer();
		Vector<BindingComponent> components = SwingUtility.findBindingComponents(this);
		for (BindingComponent bindingComponent : components) {
			try {
				bindingComponent.validateValue();
			} catch (UIValidationException e) {
				if (e.getProblems() != null && fslblErrorl != null) {
					fslblErrorl.setText(e.getProblems().getLeadProblem().getMessage());
				}
				throw e;
			}
		}
		if (fslblErrorl != null) {
			fslblErrorl.setText("");
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	public void validateValues() throws ValidationException {
		validateValues(null);
	}

	@Override
	public void setLayout(LayoutManager mgr) {
		if (mgr instanceof FlowLayout) {
			FlowLayout flow = (FlowLayout) mgr;
//			flow.setHgap(0);
//			flow.setVgap(0);
		}
		super.setLayout(mgr);
	}

	@Override
	public void setBorder(Border border) {
		if (border instanceof TitledBorder) {
			TitledBorder b = (TitledBorder) border;
			b.setTitle(Lables.get(b.getTitle(),true));
		}
		super.setBorder(border);
	}

	public Image getBackGroundImage() {
		return backGroundImage;
	}

	public void setBackGroundImage(Image backGroundImage) {
		this.backGroundImage = backGroundImage;
	}

	public void setBackGroundImage(String imageName) {
		try {
			setBackGroundImage(SwingUtility.getImage(imageName));
			// add(new ImagePanel(SwingUtility.geti))
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (backGroundImage == null)
			super.paintComponent(g);
		else {
			Graphics2D g2d = (Graphics2D) g;

			// scale the image to fit the size of the Panel
			double mw = backGroundImage.getWidth(null);
			double mh = backGroundImage.getHeight(null);

			double sw = getWidth() / mw;
			double sh = getHeight() / mh;

			g2d.scale(sw, sh);
			g2d.drawImage(backGroundImage, 0, 0, this);
		}
	}

	@Override
	public void setDataSource(DataSource manager) {
		fsWrapper.setDataSource(manager);
	}

	@Override
	public DataSource getDataSource() {
		return fsWrapper.getDataSource();
	}

	@Override
	public void addValueChangeListener(ValueChangeListener listener) {
		fsWrapper.addValueChangeListsner(listener);
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAutoTransferFocus(boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public boolean isAutoTransferFocus() {
		return transfer;
	}	
}
