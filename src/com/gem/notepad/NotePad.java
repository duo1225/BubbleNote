package com.gem.notepad;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class NotePad extends JFrame {

	/**
	 * autor:Chation Usefor:¼ÇÊÂ±¾
	 */
	private static final long serialVersionUID = 1L;
	private TextDAO textDAO;
	private JMenu fileMenu;
	private JMenuItem menuOpen; // open
	private JMenuItem menuSave; //save
	private JMenuItem menuSaveAs; //save as
	private JMenuItem menuClose; // close

	private JMenu editMenu;
	private JMenuItem menuCut; // cut
	private JMenuItem menuCopy; // copy
	private JMenuItem menuPast; // coller

	private JMenu aboutMenu;
	private JMenuItem menuAbout; // about

	private JMenuBar menuBar;

	private JTextArea textArea;
	private JLabel stateBar;

	private JPopupMenu popupMenu; // popup menu

	private JFileChooser fileChooser;// fichier selector

	public NotePad(TextDAO textDAO) {
		this();
		this.textDAO = textDAO;
	}

	public NotePad() {
		// initialiser
		initComponent();
		// initialiser listener
		initEventListener();
	}

	private void initEventListener() {
		// ?????????????????X????
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		// ??????
		initAccelerator();

		// ???????
		initMenuListener();

		// ???????
		textArea.addKeyListener(new KeyAdapter() {

			public void keyTyped(java.awt.event.KeyEvent e) {
				stateBar.setText("Already modified");
			}
		});

		// ???????
		textArea.addMouseListener(new MouseAdapter() {
			/**
			 * ????????????????????
			 */
			public void mouseClicked(MouseEvent e) {
				// BUTTON1,??????
				if (e.getButton() == MouseEvent.BUTTON1) {
					popupMenu.setVisible(false);
				}
			}

			/**
			 * ????????????????????????
			 */
			public void mouseReleased(MouseEvent e) {
				// BUTTON3,??????
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(editMenu, e.getX(), e.getY());
				}
			}

		});
	}

	private void initMenuListener() {
		/**
		 * ?????????????????????actionPerformed()???
		 */
		menuOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ????
				openFile();
			}
		});

		menuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ????
				saveFile();
			}
		});

		menuSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ????
				saveAsFile();
			}
		});

		menuClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ????
				close();
			}
		});

		menuCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ????
				cut();
			}
		});

		menuCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ????
				copy(e);
			}
		});

		menuPast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ????
				paste(e);
			}
		});

		menuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ???????
				JOptionPane.showOptionDialog(null, "BubbleNote is a new note....", "BubbleNote",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			}
		});
	}

	protected void paste(ActionEvent e) {
		textArea.paste();
		//???
		stateBar.setText("already modified");
		// ????
		popupMenu.setVisible(false);

	}

	protected void copy(ActionEvent e) {
		textArea.copy();
		// ????
		popupMenu.setVisible(false);

	}

	protected void cut() {
		textArea.cut();
		stateBar.setText("already modified");
		// ????
		popupMenu.setVisible(false);
	}

	protected void close() {
		if (isEditFileSaved()) {
			// ??????,??????
			dispose();
		} else {
			int option = JOptionPane.showConfirmDialog(null, "Already modified, do you want to save?", "Close document?", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null);
			switch (option) {
			case JOptionPane.YES_OPTION:
				saveFile();
				break;
			case JOptionPane.NO_OPTION:
				break;
			}
			dispose();
		}

	}

	protected void saveAsFile() {
		int option = fileChooser.showSaveDialog(null);
		// ????????
		if (option == JFileChooser.APPROVE_OPTION) {
			// ?????
			setTitle(fileChooser.getSelectedFile().toString());
			// ????
			textDAO.create(fileChooser.getSelectedFile().toString());
			// ???????????
			saveFile(fileChooser.getSelectedFile().toString());
		}
	}

	protected void saveFile(String path) { // save fichier
		String text = textArea.getText();
		textDAO.save(path, text);
		// ?????????
		stateBar.setText("unmodified");
	}

	protected void openFile() {
		if (isEditFileSaved()) {
			// ?????????
			showFileDialog();
		} else {
			// ???????
			int option = JOptionPane.showConfirmDialog(null, "The document has been modified, is it saved?",
					"´save ficher?", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null);

			switch (option) {
			case JOptionPane.YES_OPTION: // Yes
				saveFile();
				break;
			case JOptionPane.NO_OPTION: // cancel
				showFileDialog();
				break;
			}
		}
	}

	private void saveFile() {
		// ?????????????
		File file = new File(getTitle());
		if (!file.exists()) {
			// ?????
			saveAsFile();
		} else {
			// ???????
			saveFile(getTitle());
		}
	}

	private void showFileDialog() {
		int option = fileChooser.showOpenDialog(null);// ?????????
		// ???????
		if (option == JFileChooser.APPROVE_OPTION) {
			// ?????????
			setTitle(fileChooser.getSelectedFile().toString());
			textArea.setText("");// ???????
			stateBar.setText("Unmodified");// ?????

			String text = textDAO.read(fileChooser.getSelectedFile().toString());
			// ??????????
			textArea.setText(text);
		}
	}

	private boolean isEditFileSaved() {
		if (stateBar.getText().equals("Unmodified")) {
			return true; // ????????
		} else {
			return false;
		}
	}

	private void initAccelerator() {
		// ?????? ctrl+O
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		// ?????? ctrl+S
		menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		// ?????? ctrl+shift+S
		menuSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		// ???? ctrl+Q
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

		// ???? ctrl+X
		menuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		// ???? ctrl+C
		menuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		// ???? ctrl+V
		menuPast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
	}

	private void initComponent() {
		// ??????
		setTitle("Text document");
		// ??????
		setSize(800, 600);

		// ?????????
		initFileMenu();
		// ?????????
		initEditMenu();
		// ?????????
		initAboutMenu();

		// ??????
		initMenuBar();
		// ????????
		initTextArea();
		// ??????
		initStateBar();
		// ????
		popupMenu = editMenu.getPopupMenu();
		// ?????????
		fileChooser = new JFileChooser();
	}

	private void initStateBar() {
		stateBar = new JLabel("???");
		// ???????LEFT?? CENTER?? RIGHT??
		stateBar.setHorizontalAlignment(SwingConstants.LEFT);
		// ????????
		stateBar.setBorder(BorderFactory.createEtchedBorder());
		// ????????????
		getContentPane().add(stateBar, BorderLayout.SOUTH);
	}

	private void initTextArea() {
		// ??????
		textArea = new JTextArea();
		// ???????????:???
		textArea.setFont(new Font("Detailed body", Font.PLAIN, 16));
		// ??????
		textArea.setLineWrap(true);
		// ??????????
		JScrollPane panel = new JScrollPane(textArea, // ??????
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, // ???????????
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // ???????????
		// ?????????????
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();

		// ??????????
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);

		// ?????
		setJMenuBar(menuBar);
	}

	private void initAboutMenu() {
		aboutMenu = new JMenu("About");

		menuAbout = new JMenuItem("About BubbleNote");
		// ??????????
		aboutMenu.add(menuAbout);
	}

	private void initEditMenu() {
		editMenu = new JMenu("Edit");

		menuCut = new JMenuItem("cut");
		menuCopy = new JMenuItem("copy");
		menuPast = new JMenuItem("paste");
		// ??????????
		editMenu.add(menuCut);
		editMenu.add(menuCopy);
		editMenu.add(menuPast);
	}

	private void initFileMenu() {
		// ????????
		fileMenu = new JMenu("Document");

		menuOpen = new JMenuItem("Open document");
		menuSave = new JMenuItem("Save document");
		menuSaveAs = new JMenuItem("Save as new document");
		menuClose = new JMenuItem("Close document");

		// ??????????
		fileMenu.add(menuOpen);
		// ?????
		fileMenu.addSeparator();
		fileMenu.add(menuSave);
		fileMenu.add(menuSaveAs);
		// ?????
		fileMenu.addSeparator();
		fileMenu.add(menuClose);
	}

}