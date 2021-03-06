import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.event.*;
import javax.swing.text.*;

import static java.nio.file.StandardCopyOption.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import ca.updater.constants.GUI;

public class Updater implements ActionListener {
	private final Map<String, ImageIcon> StockIconMap = new HashMap<>();
	private final Map<String, ImageIcon> PortIconMap = new HashMap<>();
	/* Preference Keys */
	private static final String LEFT1_CHAR="L1_CHAR";
	private static final String LEFT2_CHAR="L2_CHAR";
	private static final String RIGHT1_CHAR="R1_CHAR";
	private static final String RIGHT2_CHAR="R2_CHAR";
	private static final String LEFT1_CHAR_IMAGE="L1_CHAR_IMAGE";
	private static final String LEFT2_CHAR_IMAGE="L2_CHAR_IMAGE";
	private static final String RIGHT1_CHAR_IMAGE="R1_CHAR_IMAGE";
	private static final String RIGHT2_CHAR_IMAGE="R2_CHAR_IMAGE";
	private static final String LEFT1_PORT_FILE_PATH="L1_PORT_FILE_PATH";
	private static final String LEFT2_PORT_FILE_PATH="L2_PORT_FILE_PATH";
	private static final String RIGHT1_PORT_FILE_PATH="R1_PORT_FILE_PATH";
	private static final String RIGHT2_PORT_FILE_PATH="R2_PORT_FILE_PATH";
	private static final String FILE_NAMES="F_NAMES";
	private static final String LEFT1_NAME_FP="LEFT1_NAME_FP";
	private static final String RIGHT1_NAME_FP="RIGHT1_NAME_FP";
	private static final String LEFT2_NAME_FP="LEFT2_NAME_FP";
	private static final String RIGHT2_NAME_FP="RIGHT2_NAME_FP";
	private static final String LEFT_SCORE_FP="LEFT_SCORE_FP";
	private static final String RIGHT_SCORE_FP="RIGHT_SCORE_FP";
	private static final String BRACKET_POSITION_FP="BRACKET_POSITION_FP";
	private static final String ROUND_FORMAT_FP="ROUND_FORMAT_FP";
	private static final String LEFT_COMMENTATOR_FP="LEFT_COMMENTATOR_FP";
	private static final String RIGHT_COMMENTATOR_FP="RIGHT_COMMENTATOR_FP";
	private static final String LEFT1_STOCK_ICON_FP="LEFT1_STOCK_ICON_FP";
	private static final String RIGHT1_STOCK_ICON_FP="RIGHT1_STOCK_ICON_FP";
	private static final String LEFT2_STOCK_ICON_FP="LEFT2_STOCK_ICON_FP";
	private static final String RIGHT2_STOCK_ICON_FP="RIGHT2_STOCK_ICON_FP";
	private static final String FILE_STOCK_ICONS="F_STOCK_ICONS";
	private static final String FILE_PORTS="F_PORTS";
	/* Preference Defaults */
	private static String DEFAULT_LEFT1_CHAR="null";
	private static String DEFAULT_LEFT2_CHAR="null";
	private static String DEFAULT_LEFT1_CHAR_IMAGE="null";
	private static String DEFAULT_LEFT2_CHAR_IMAGE="null";
	private static String DEFAULT_RIGHT1_CHAR="null";
	private static String DEFAULT_RIGHT2_CHAR="null";
	private static String DEFAULT_RIGHT1_CHAR_IMAGE="null";
	private static String DEFAULT_RIGHT2_CHAR_IMAGE="null";
	private static String DEFAULT_LEFT1_PORT_FILE_PATH="null";
	private static String DEFAULT_LEFT2_PORT_FILE_PATH="null";
	private static String DEFAULT_RIGHT1_PORT_FILE_PATH="null";
	private static String DEFAULT_RIGHT2_PORT_FILE_PATH="null";
	private static String DEFAULT_FILE_NAMES="CHANGETHIS";
	private static String DEFAULT_FILE_STOCK_ICONS="CHANGETHIS";
	private static String DEFAULT_FILE_PORTS="CHANGETHIS";
	/* Files for program */
	private File namesFile;
	private File leftNameFile;
	private File left2NameFile;
	private File rightNameFile;
	private File right2NameFile;
	private File leftCommentatorNameFile;
	private File rightCommentatorNameFile;
	private File leftScoreFile;
	private File rightScoreFile;
	private File bracketPositionFile;
	private File roundFormatFile;
	private File leftStockIconFile;
	private File left2StockIconFile;
	private File rightStockIconFile;
	private File right2StockIconFile;
	private File stockIconDir;
	private File portsDir;
	// TODO: remove? unused?
	//private File leftPortFile;
	//private File left2PortFile;
	//private File rightPortFile;
	//private File right2PortFile;
	/* Local variables for score etc */
	private ArrayList<String> namesList = new ArrayList<>();
	private ArrayList<String> iconsList = new ArrayList<>();
	private ArrayList<String> portsList = new ArrayList<>();
	private String leftCommentatorNameValue = "unnamed";
	private String rightCommentatorNameValue = "unnamed";
	private int leftScoreValue = 0;
	private int rightScoreValue = 0;
	/* GUI variables */
	private int scoreY;
	private boolean show2 = false;
	private boolean flatLayout = false;
	private boolean showCommentators = false;

	/* Aliased GUI classes {{{ */
	public class JAliasedTextField extends JTextField {
		public JAliasedTextField(String s) {
			super(s);
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}

	public class JAliasedButton extends JButton {
		public JAliasedButton(String s) {
			super(s);
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}

	public class JAliasedComboBox extends JComboBox {
		public JAliasedComboBox() {
			super();
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}

	public class JAliasedLabel extends JLabel {
		public JAliasedLabel(String s) {
			super(s);
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}
	/* }}} */

	/* Updating elements */
	private GUIPlayerSection left1PS;
	private GUIPlayerSection left2PS;
	private GUIPlayerSection right1PS;
	private GUIPlayerSection right2PS;
	private JAliasedTextField leftScore;
	private JAliasedTextField rightScore;
	private JAliasedButton flatLayoutButton;
	private JAliasedButton show2Button;
	private JAliasedButton showCommentatorsButton;
	private JAliasedButton leftScoreInc;
	private JAliasedButton leftScoreDec;
	private JAliasedButton rightScoreInc;
	private JAliasedButton rightScoreDec;
	private JAliasedComboBox leftCommentatorName;
	private JAliasedComboBox rightCommentatorName;
	private JAliasedTextField bracketPosition;
	private JAliasedTextField roundFormat;
	private JAliasedButton switchStockIcons1;
	private JAliasedButton switchStockIcons2;
	private JAliasedButton switchNames1;
	private JAliasedButton switchNames2;
	private JAliasedButton switchPorts1;
	private JAliasedButton switchPorts2;
	private JAliasedButton switchScore;
	private JAliasedButton switchCommentatorNames;
	private JAliasedButton reloadFilesButton;
	private JAliasedLabel leftScoreLabel;
	private JAliasedLabel rightScoreLabel;
	private JAliasedLabel bracketPositionLabel;
	private JAliasedLabel roundFormatLabel;
	private JAliasedLabel leftCommentatorLabel;
	private JAliasedLabel rightCommentatorLabel;
	/* Settings elements */
	private JAliasedLabel namesLabel;
	private JAliasedTextField namesText;
	private JAliasedLabel namesFieldsLabel;
	private JAliasedTextField leftNameText;
	private JAliasedTextField left2NameText;
	private JAliasedTextField rightNameText;
	private JAliasedTextField right2NameText;
	private JAliasedLabel scoresFieldsLabel;
	private JAliasedTextField leftScoreText;
	private JAliasedTextField rightScoreText;
	private JAliasedLabel setInfoFieldsLabel;
	private JAliasedTextField bracketPositionText;
	private JAliasedTextField roundFormatText;
	private JAliasedLabel commentatorFieldsLabel;
	private JAliasedTextField leftCommentatorNameText;
	private JAliasedTextField rightCommentatorNameText;
	private JAliasedLabel stockIconFieldsLabel;
	private JAliasedTextField leftStockIconText;
	private JAliasedTextField left2StockIconText;
	private JAliasedTextField rightStockIconText;
	private JAliasedTextField right2StockIconText;
	// TODO: remove? unused?
	//private JAliasedTextField leftPortText;
	//private JAliasedTextField left2PortText;
	//private JAliasedTextField rightPortText;
	//private JAliasedTextField right2PortText;
	private JAliasedLabel StockIconDirLabel;
	private JAliasedLabel PortsDirLabel;
	private JAliasedTextField StockIconDirText;
	private JAliasedTextField PortsDirText;
	private JAliasedButton NamesBrowseButton;
	private JAliasedButton LeftNameBrowseButton;
	private JAliasedButton Left2NameBrowseButton;
	private JAliasedButton RightNameBrowseButton;
	private JAliasedButton Right2NameBrowseButton;
	private JAliasedButton LeftScoreBrowseButton;
	private JAliasedButton RightScoreBrowseButton;
	private JAliasedButton BracketPositionBrowseButton;
	private JAliasedButton RoundFormatBrowseButton;
	private JAliasedButton LeftCommentatorBrowseButton;
	private JAliasedButton RightCommentatorBrowseButton;
	private JAliasedButton LeftStockIconBrowseButton;
	private JAliasedButton Left2StockIconBrowseButton;
	private JAliasedButton RightStockIconBrowseButton;
	private JAliasedButton Right2StockIconBrowseButton;
	private JAliasedButton StockIconDirBrowseButton;
	private JAliasedButton PortsDirBrowseButton;
	private JScrollPane settingsPaneScroller;

	/* Custom Cell Renderers {{{ */
	public class StockIconListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object obj, int index,
			boolean isSelected, boolean cellHasFocus) {

			JLabel item = (JLabel) super.getListCellRendererComponent(list, obj, index, isSelected,
				cellHasFocus);
			item.setIcon(StockIconMap.get((String) obj));
			item.setText("");
			return item;
		}
	}

	public class PortIconListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object obj, int index,
			boolean isSelected, boolean cellHasFocus) {

			JLabel item = (JLabel) super.getListCellRendererComponent(list, obj, index, isSelected,
				cellHasFocus);
			item.setIcon(PortIconMap.get((String) obj));
			item.setText("");
			return item;
		}
	}
	/* }}} */

	private void setFilePaths() {
		namesFile = new File(namesText.getText());
		leftNameFile = new File(leftNameText.getText());
		left2NameFile = new File(left2NameText.getText());
		rightNameFile = new File(rightNameText.getText());
		right2NameFile = new File(right2NameText.getText());
		leftCommentatorNameFile = new File(leftCommentatorNameText.getText());
		rightCommentatorNameFile = new File(rightCommentatorNameText.getText());
		leftScoreFile = new File(leftScoreText.getText());
		rightScoreFile = new File(rightScoreText.getText());
		bracketPositionFile = new File(bracketPositionText.getText());
		roundFormatFile = new File(roundFormatText.getText());
		leftStockIconFile = new File(leftStockIconText.getText());
		left2StockIconFile = new File(left2StockIconText.getText());
		rightStockIconFile = new File(rightStockIconText.getText());
		right2StockIconFile = new File(right2StockIconText.getText());
		stockIconDir = new File(StockIconDirText.getText());
		portsDir = new File(PortsDirText.getText());
		// TODO: remove? unused?
		//leftPortFile = new File(leftPortText.getText());
		//left2PortFile = new File(left2PortText.getText());
		//rightPortFile = new File(rightPortText.getText());
		//right2PortFile = new File(right2PortText.getText());
	}

	private void readNames() {
		try {
			namesList.clear();
			BufferedReader reader = new BufferedReader(new FileReader(namesFile));
			String line;
			while ((line = reader.readLine()) != null) {
				namesList.add(line);
			}
		} catch (Exception e) {
			System.err.println("Error: Failed to read from File");
		}
	}

	private void readFromFile(File f, JTextComponent component) {
		String read;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			if ((read = reader.readLine()) != null) {
				component.setText("" + read);
			} else {
				System.err.println("Error: could not read from File");
			}
		} catch (Exception e) {
			System.err.println("Error: could not read from File");
		}
	}

	private String readFromFile(File f) {
		String read;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			if ((read = reader.readLine()) != null) {
				return read;
			} else {
				System.err.println("Error: could not read from File");
				return "";
			}
		} catch (Exception e) {
			System.err.println("Error: could not read from File");
			return "";
		}
	}

	private String stringFromFile(File f) {
		String ret = "";
		String read;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			while ((read = reader.readLine()) != null) {
				ret += read;
			}

			return ret;
		} catch (Exception e) {
			System.err.println("Error: could not read from File");
			return "";
		}
	}

	public void writeToFile(String toWrite, File f) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(toWrite);
			writer.close();
		} catch (Exception e) {
			System.err.println("Error: Failed to write to File");
		}
	}

	private void loadStockIcons() {
		try {
			File[] icons = stockIconDir.listFiles();
			iconsList.clear();

			/* Perform a null-check in case the path does not denote a directory */
			if (icons != null) {
				for (int i = 0; i < icons.length; i++) {
					iconsList.add(icons[i].getName());
				}
				Collections.sort(iconsList);
			} else {
				System.err.println("Warning: stockIconDir is not a directory");
			}
		} catch (Exception e) {
			System.err.println("Error: could not load stock icon directories");
		}
	}

	private void loadPorts() {
		try {
			File[] ports = portsDir.listFiles();
			portsList.clear();

			/* Perform a null-check in case the path does not denote a directory */
			if (ports != null) {
				for (int i = 0; i < ports.length; i++) {
					portsList.add(ports[i].getName());
				}
				Collections.sort(portsList);
			} else {
				System.err.println("Warning: portsDir is not a directory");
			}
		} catch (Exception e) {
			System.err.println("Error: could not load ports directories");
		}
	}

	private void updateElements() {
		Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

		String leftComName = (String) readFromFile(leftCommentatorNameFile);
		String rightComName = (String) readFromFile(rightCommentatorNameFile);

		String left1Name = (String) readFromFile(leftNameFile);
		String right1Name = (String) readFromFile(rightNameFile);
		String left2Name = (String) readFromFile(left2NameFile);
		String right2Name = (String) readFromFile(right2NameFile);

		/* Update stock icon path for each player section */
		left1PS.setNamesPath(namesText.getText());
		left2PS.setNamesPath(namesText.getText());
		right1PS.setNamesPath(namesText.getText());
		right2PS.setNamesPath(namesText.getText());
		left1PS.setStockDirPath(StockIconDirText.getText());
		left2PS.setStockDirPath(StockIconDirText.getText());
		right1PS.setStockDirPath(StockIconDirText.getText());
		right2PS.setStockDirPath(StockIconDirText.getText());
		left1PS.setPortsDirPath(PortsDirText.getText());
		left2PS.setPortsDirPath(PortsDirText.getText());
		right1PS.setPortsDirPath(PortsDirText.getText());
		right2PS.setPortsDirPath(PortsDirText.getText());

		readNames();
		loadStockIcons();
		loadPorts();

		left1PS.updateElements(namesList, iconsList, portsList);
		right1PS.updateElements(namesList, iconsList, portsList);
		left2PS.updateElements(namesList, iconsList, portsList);
		right2PS.updateElements(namesList, iconsList, portsList);

		/* Set the names to be what is currently written in their corresponding files */
		left1PS.setName(left1Name);
		right1PS.setName(right1Name);
		left2PS.setName(left2Name);
		right2PS.setName(right2Name);

		/* Set characters to remembered characters */
		left1PS.setCharacter(prefs.get(LEFT1_CHAR, DEFAULT_LEFT1_CHAR));
		right1PS.setCharacter(prefs.get(RIGHT1_CHAR, DEFAULT_RIGHT1_CHAR));
		left2PS.setCharacter(prefs.get(LEFT2_CHAR, DEFAULT_LEFT2_CHAR));
		right2PS.setCharacter(prefs.get(RIGHT2_CHAR, DEFAULT_RIGHT2_CHAR));
		/* Set image used for character to remembered image */
		left1PS.setChosenImageFileName(prefs.get(LEFT1_CHAR_IMAGE, DEFAULT_LEFT1_CHAR_IMAGE));
		right1PS.setChosenImageFileName(prefs.get(RIGHT1_CHAR_IMAGE, DEFAULT_RIGHT1_CHAR_IMAGE));
		left2PS.setChosenImageFileName(prefs.get(LEFT2_CHAR_IMAGE, DEFAULT_LEFT2_CHAR_IMAGE));
		right2PS.setChosenImageFileName(prefs.get(RIGHT2_CHAR_IMAGE, DEFAULT_RIGHT2_CHAR_IMAGE));

		/* Set port image to remembered image */
		left1PS.setPortsFileName(prefs.get(LEFT1_PORT_FILE_PATH, DEFAULT_LEFT1_PORT_FILE_PATH));
		right1PS.setPortsFileName(prefs.get(RIGHT1_PORT_FILE_PATH, DEFAULT_RIGHT1_PORT_FILE_PATH));
		left2PS.setPortsFileName(prefs.get(LEFT2_PORT_FILE_PATH, DEFAULT_LEFT2_PORT_FILE_PATH));
		right2PS.setPortsFileName(prefs.get(RIGHT2_PORT_FILE_PATH, DEFAULT_RIGHT2_PORT_FILE_PATH));

		readFromFile(leftScoreFile, (JTextComponent) leftScore);
		// leftScoreValue = Integer.parseInt(leftScore.getText());
		readFromFile(rightScoreFile, (JTextComponent) rightScore);
		// rightScoreValue = Integer.parseInt(rightScore.getText());
		readFromFile(bracketPositionFile, (JTextComponent) bracketPosition);
		readFromFile(roundFormatFile, (JTextComponent) roundFormat);

		leftCommentatorName.removeAllItems();
		rightCommentatorName.removeAllItems();
		for (int i = 0; i < namesList.size(); i++) {
			leftCommentatorName.addItem(namesList.get(i));
			rightCommentatorName.addItem(namesList.get(i));
		}
		leftCommentatorName.setSelectedItem(leftComName);
		rightCommentatorName.setSelectedItem(rightComName);
	}

	private void setElementPositionsStandardLayout() {
		left1PS.setLocation(5, 5);
		right1PS.setLocation(
			left1PS.getX() + left1PS.getWidth() + (2 * GUI.small_gap_width) + GUI.button_width,
			left1PS.getY());
		left2PS.setLocation(
			left1PS.getX(),
			left1PS.getY() + left1PS.getHeight() + GUI.small_gap_width);
		right2PS.setLocation(
			right1PS.getX(),
			right1PS.getY() + right1PS.getHeight() + GUI.small_gap_width);

		switchStockIcons1.setBounds(
			left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			left1PS.getCharacterBoxY(),
			GUI.button_width,
			GUI.element_height + GUI.small_gap_width + (int) (GUI.element_height * 1.5));
		switchStockIcons2.setBounds(
			left2PS.getX() + left2PS.getWidth() + GUI.small_gap_width,
			left2PS.getCharacterBoxY(),
			GUI.button_width,
			GUI.element_height + GUI.small_gap_width + (int) (GUI.element_height * 1.5));
		switchNames1.setBounds(
			left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			left1PS.getNameBoxY(),
			GUI.button_width,
			GUI.element_height);
		switchNames2.setBounds(
			left2PS.getX() + left2PS.getWidth() + GUI.small_gap_width,
			left2PS.getNameBoxY(),
			GUI.button_width,
			GUI.element_height);
		switchPorts1.setBounds(
			left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			left1PS.getPortsListY(),
			GUI.button_width,
			(int) (GUI.element_height * 1.5));
		switchPorts2.setBounds(
			left2PS.getX() + left2PS.getWidth() + GUI.small_gap_width,
			left2PS.getPortsListY(),
			GUI.button_width,
			(int) (GUI.element_height * 1.5));

		setScorePositions();
	}

	private void setElementPositionsFlatLayout() {
		leftScoreLabel.setLocation(
			5,
			5);
		leftScoreDec.setLocation(
			leftScoreLabel.getX(),
			leftScoreLabel.getY() + leftScoreLabel.getHeight() + GUI.small_gap_width);
		leftScore.setLocation(
			leftScoreDec.getX() + leftScoreDec.getWidth() + 5,
			leftScoreDec.getY());
		leftScoreInc.setLocation(
			leftScore.getX() + leftScore.getWidth() + 5,
			leftScoreDec.getY());

		left1PS.setLocation(leftScoreLabel.getX() + leftScoreLabel.getWidth() + GUI.small_gap_width,
			leftScoreLabel.getY());

		switchScore.setBounds(
			left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			left1PS.getY(),
			GUI.button_width,
			GUI.element_height);
		switchStockIcons1.setLocation(left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			switchScore.getY() + switchScore.getHeight() + GUI.small_gap_width);
		switchPorts1.setLocation(
			left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			switchStockIcons1.getY() + switchStockIcons1.getHeight() + GUI.small_gap_width);
		switchNames1.setLocation(
			left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			switchPorts1.getY() + switchPorts1.getHeight() + GUI.small_gap_width);
		switchCommentatorNames.setLocation(
			switchNames1.getX(),
			switchNames1.getY() + switchNames1.getHeight() + GUI.small_gap_width);

		right1PS.setLocation(switchScore.getX() + switchScore.getWidth() + GUI.small_gap_width,
			left1PS.getY());

		rightScoreLabel.setLocation(
			right1PS.getX() + right1PS.getWidth() + GUI.small_gap_width,
			right1PS.getY());
		rightScoreDec.setLocation(
			rightScoreLabel.getX(),
			rightScoreLabel.getY() + rightScoreLabel.getHeight() + GUI.small_gap_width);
		rightScore.setLocation(
			rightScoreDec.getX() + rightScoreDec.getWidth() + 5,
			rightScoreDec.getY());
		rightScoreInc.setLocation(
			rightScore.getX() + rightScore.getWidth() + 5,
			rightScoreDec.getY());

		bracketPositionLabel.setBounds(
			leftScoreDec.getX(),
			leftScoreDec.getY() + leftScoreDec.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		roundFormatLabel.setBounds(
			rightScoreDec.getX(),
			bracketPositionLabel.getY(),
			GUI.combo_box_width,
			GUI.element_height);
		bracketPosition.setBounds(
			bracketPositionLabel.getX(),
			bracketPositionLabel.getY() + bracketPositionLabel.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		roundFormat.setBounds(
			rightScoreDec.getX(),
			bracketPosition.getY(),
			GUI.combo_box_width,
			GUI.element_height);

		showCommentatorsButton.setBounds(bracketPosition.getX(),
			left1PS.getY() + left1PS.getHeight() - GUI.element_height,
			GUI.element_height,
			GUI.element_height);
		leftCommentatorLabel.setBounds(
			showCommentatorsButton.getX(),
			showCommentatorsButton.getY(),
			GUI.combo_box_width,
			GUI.element_height);
		rightCommentatorLabel.setBounds(
			roundFormat.getX(),
			showCommentatorsButton.getY(),
			GUI.combo_box_width,
			GUI.element_height);
		leftCommentatorName.setLocation(
			leftCommentatorLabel.getX(),
			leftCommentatorLabel.getY() + leftCommentatorLabel.getHeight() + GUI.small_gap_width);
		rightCommentatorName.setLocation(
			rightCommentatorLabel.getX(),
			rightCommentatorLabel.getY() + rightCommentatorLabel.getHeight() + GUI.small_gap_width);
	}
	private void setScorePositions() {
		leftScoreLabel.setBounds(
			left1PS.getX(),
			scoreY,
			GUI.combo_box_width,
			GUI.element_height);
		rightScoreLabel.setBounds(
			right1PS.getX(),
			scoreY,
			GUI.combo_box_width,
			GUI.element_height);
		leftScoreDec.setBounds(
			leftScoreLabel.getX(),
			leftScoreLabel.getY() + leftScoreLabel.getHeight() + GUI.small_gap_width,
			(GUI.combo_box_width - (2 * GUI.small_gap_width)) / 3,
			(int) (GUI.element_height * 1.5));
		leftScore.setBounds(
			leftScoreDec.getX() + leftScoreDec.getWidth() + 5,
			leftScoreDec.getY(),
			(GUI.combo_box_width - (2 * GUI.small_gap_width)) / 3,
			(int) (GUI.element_height * 1.5));
		leftScoreInc.setBounds(
			leftScore.getX() + leftScore.getWidth() + 5,
			leftScoreDec.getY(),
			(GUI.combo_box_width - (2 * GUI.small_gap_width)) / 3,
			(int) (GUI.element_height * 1.5));
		switchScore.setBounds(left1PS.getX() + left1PS.getWidth() + GUI.small_gap_width,
			leftScoreInc.getY(), GUI.button_width, (int) (GUI.element_height * 1.5));
		rightScoreDec.setBounds(
			right1PS.getX(),
			leftScoreDec.getY(),
			(GUI.combo_box_width - (2 * GUI.small_gap_width)) / 3,
			(int) (GUI.element_height * 1.5));
		rightScore.setBounds(
			rightScoreDec.getX() + rightScoreDec.getWidth() + 5,
			leftScoreDec.getY(),
			(GUI.combo_box_width - (2 * GUI.small_gap_width)) / 3,
			(int) (GUI.element_height * 1.5));
		rightScoreInc.setBounds(
			rightScore.getX() + rightScore.getWidth() + 5,
			leftScoreDec.getY(),
			(GUI.combo_box_width - (2 * GUI.small_gap_width)) / 3,
			(int) (GUI.element_height * 1.5));
		bracketPositionLabel.setBounds(
			leftScoreDec.getX(),
			leftScoreDec.getY() + leftScoreDec.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		roundFormatLabel.setBounds(
			rightScoreDec.getX(),
			bracketPositionLabel.getY(),
			GUI.combo_box_width,
			GUI.element_height);
		bracketPosition.setBounds(
			bracketPositionLabel.getX(),
			bracketPositionLabel.getY() + bracketPositionLabel.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		roundFormat.setBounds(
			rightScoreDec.getX(),
			bracketPosition.getY(),
			GUI.combo_box_width,
			GUI.element_height);
		leftCommentatorLabel.setBounds(
			bracketPosition.getX(),
			bracketPosition.getY() + bracketPosition.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		rightCommentatorLabel.setBounds(
			roundFormat.getX(),
			roundFormat.getY() + roundFormat.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		showCommentatorsButton.setBounds(left1PS.getX(),
			bracketPosition.getY() + bracketPosition.getHeight() + GUI.small_gap_width,
			GUI.element_height,
			GUI.element_height);
		leftCommentatorName.setBounds(
			leftCommentatorLabel.getX(),
			leftCommentatorLabel.getY() + leftCommentatorLabel.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		switchCommentatorNames.setBounds(
			leftCommentatorName.getX() + leftCommentatorName.getWidth() + GUI.small_gap_width,
			leftCommentatorName.getY(),
			GUI.button_width,
			GUI.element_height);
		rightCommentatorName.setBounds(
			switchCommentatorNames.getX() + switchCommentatorNames.getWidth() + GUI.small_gap_width,
			leftCommentatorName.getY(),
			GUI.combo_box_width,
			GUI.element_height);
	}

	private void set2ElementsVisibility(boolean visibility) {
		left2PS.setVisible(visibility);
		switchStockIcons2.setVisible(visibility);
		switchNames2.setVisible(visibility);
		switchPorts2.setVisible(visibility);
		right2PS.setVisible(visibility);
	}

	private void setCommentatorsElementsVisibility(boolean visibility) {
		leftCommentatorLabel.setVisible(visibility);
		rightCommentatorLabel.setVisible(visibility);
		leftCommentatorName.setVisible(visibility);
		switchCommentatorNames.setVisible(visibility);
		rightCommentatorName.setVisible(visibility);
	}

	private void setScoreY() {
		if (show2) {
			scoreY = left2PS.getY() + left2PS.getHeight() + GUI.small_gap_width;
		} else {
			scoreY = left1PS.getY() + left1PS.getHeight() + GUI.small_gap_width;
		}
	}

	private void toggle2() {
		setScoreY();
		setScorePositions();
		if (show2) {
			show2Button.setText("-");
		} else {
			show2Button.setText("+");
		}
		set2ElementsVisibility(show2);
	}

	private void toggleFlatLayout() {
		if (flatLayout) {
			show2Button.setVisible(false);
			flatLayoutButton.setText("=");
			setElementPositionsFlatLayout();
			set2ElementsVisibility(false);
		} else {
			show2Button.setVisible(true);
			flatLayoutButton.setText("_");
			setElementPositionsStandardLayout();
			set2ElementsVisibility(show2);
		}
	}

	private void toggleCommentators() {
		if (showCommentators) {
			showCommentatorsButton.setText("-");
		} else {
			showCommentatorsButton.setText("+");
		}
		setCommentatorsElementsVisibility(showCommentators);
	}

	public Updater() {
		Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

		/* TODO: IN-PROGRESS: replace this with more robust system */
		final String InitialBaseDirText = System.getProperty("user.home") + "/_obs";
		final String InitialNamesText = prefs.get(FILE_NAMES, DEFAULT_FILE_NAMES);

		final String InitialLeftNameText = prefs.get(LEFT1_NAME_FP, "CHANGETHIS");
		final String InitialLeft2NameText = prefs.get(LEFT2_NAME_FP, "CHANGETHIS");
		final String InitialRightNameText = prefs.get(RIGHT1_NAME_FP, "CHANGETHIS");
		final String InitialRight2NameText = prefs.get(RIGHT2_NAME_FP, "CHANGETHIS");

		final String InitialLeftCommentatorNameText = prefs.get(LEFT_COMMENTATOR_FP, "CHANGETHIS");
		final String InitialRightCommentatorNameText = prefs.get(RIGHT_COMMENTATOR_FP, "CHANGETHIS");

		final String InitialLeftScoreText = prefs.get(LEFT_SCORE_FP, "CHANGETHIS");
		final String InitialRightScoreText = prefs.get(RIGHT_SCORE_FP, "CHANGETHIS");

		final String InitialBracketPositionText = prefs.get(BRACKET_POSITION_FP, "CHANGETHIS");
		final String InitialRoundFormatText = prefs.get(ROUND_FORMAT_FP, "CHANGETHIS");
		final String InitialLeftStockIconText = prefs.get(LEFT1_STOCK_ICON_FP, "CHANGETHIS");
		final String InitialLeft2StockIconText = prefs.get(LEFT2_STOCK_ICON_FP, "CHANGETHIS");
		final String InitialRightStockIconText = prefs.get(RIGHT1_STOCK_ICON_FP, "CHANGETHIS");
		final String InitialRight2StockIconText = prefs.get(RIGHT2_STOCK_ICON_FP, "CHANGETHIS");

		final String InitialStockIconText =
			prefs.get(FILE_STOCK_ICONS, DEFAULT_FILE_STOCK_ICONS);
		final String InitialPortsText =
			prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS);

		// TODO: don't think these are needed
		//final String InitialLeftPortText = InitialBaseDirText + "/left_port.png";
		//final String InitialLeft2PortText = InitialBaseDirText + "/left_port2.png";
		//final String InitialRightPortText = InitialBaseDirText + "/right_port.png";
		//final String InitialRight2PortText = InitialBaseDirText + "/right_port2.png";
		/* Sets GUI style/theme */
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("GTK+".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e2) {
				System.err.println("Error: could not find system look and feel. Exiting...");
			}
		}
		/* Settings elements instantiation */
		namesLabel = new JAliasedLabel("Names File File Path:");
		namesText = new JAliasedTextField(InitialNamesText);

		namesFieldsLabel = new JAliasedLabel("Names Fields File Paths:");
		leftNameText = new JAliasedTextField(InitialLeftNameText);
		left2NameText = new JAliasedTextField(InitialLeft2NameText);
		rightNameText = new JAliasedTextField(InitialRightNameText);
		right2NameText = new JAliasedTextField(InitialRight2NameText);

		leftNameText.setToolTipText("file path for the left name file");
		left2NameText.setToolTipText("file path for the left2 name file");
		rightNameText.setToolTipText("file path for the right name file");
		right2NameText.setToolTipText("file path for the right2 name file");

		scoresFieldsLabel = new JAliasedLabel("Scores Fields File Paths:");
		leftScoreText = new JAliasedTextField(InitialLeftScoreText);
		rightScoreText = new JAliasedTextField(InitialRightScoreText);

		leftScoreText.setToolTipText("file path for the left score file");
		rightScoreText.setToolTipText("file path for the right score file");

		setInfoFieldsLabel = new JAliasedLabel("Set Info Fields File Paths:");
		bracketPositionText = new JAliasedTextField(InitialBracketPositionText);
		roundFormatText = new JAliasedTextField(InitialRoundFormatText);

		bracketPositionText.setToolTipText("file path for the bracket position file");
		roundFormatText.setToolTipText("file path for the round format file");

		commentatorFieldsLabel = new JAliasedLabel("Commentator Names Fields File Paths:");
		leftCommentatorNameText = new JAliasedTextField(InitialLeftCommentatorNameText);
		rightCommentatorNameText = new JAliasedTextField(InitialRightCommentatorNameText);

		leftCommentatorNameText.setToolTipText("file path for the left commentator name file");
		rightCommentatorNameText.setToolTipText("file path for the right commentator name file");

		stockIconFieldsLabel = new JAliasedLabel("Stock Icon File Paths:");
		leftStockIconText = new JAliasedTextField(InitialLeftStockIconText);
		left2StockIconText = new JAliasedTextField(InitialLeft2StockIconText);
		rightStockIconText = new JAliasedTextField(InitialRightStockIconText);
		right2StockIconText = new JAliasedTextField(InitialRight2StockIconText);

		leftStockIconText.setToolTipText("file path for the left stock icon file");
		left2StockIconText.setToolTipText("file path for the left2 stock icon file");
		rightStockIconText.setToolTipText("file path for the right stock icon file");
		right2StockIconText.setToolTipText("file path for the right2 stock icon file");
		//leftPortText = new JAliasedTextField(InitialLeftPortText);
		//left2PortText = new JAliasedTextField(InitialLeft2PortText);
		//rightPortText = new JAliasedTextField(InitialRightPortText);
		//right2PortText = new JAliasedTextField(InitialRight2PortText);
		StockIconDirLabel = new JAliasedLabel("Stock Icon Directory Path:");
		PortsDirLabel = new JAliasedLabel("Ports Directory Path:");
		StockIconDirText = new JAliasedTextField(InitialStockIconText);
		PortsDirText = new JAliasedTextField(InitialPortsText);

		/* Reset the 2 score files to initial score */
		setFilePaths();
		JTabbedPane tabbedPane = new JTabbedPane() {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D graphics2d = (Graphics2D) g;
				graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent(g);
			}
		};
		// TODO change init, not OS neutral
		left1PS = new GUIPlayerSection(5, 5, "Left",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/left_name.txt",
			InitialBaseDirText + "/left_stock_icon.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/left_port.png");
		right1PS = new GUIPlayerSection(
			left1PS.getX() + left1PS.getWidth() + (2 * GUI.small_gap_width) + GUI.button_width, 5, "Right",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/right_name.txt",
			InitialBaseDirText + "/right_stock_icon.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/right_port.png");
		left2PS = new GUIPlayerSection(
			left1PS.getX(), left1PS.getY() + left1PS.getHeight() + GUI.small_gap_width,
			"Left 2",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/left_name2.txt",
			InitialBaseDirText + "/left_stock_icon2.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/left_port2.png");
		right2PS = new GUIPlayerSection(
			right1PS.getX(), right1PS.getY() + right1PS.getHeight() + GUI.small_gap_width,
			"Right 2",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/right_name2.txt",
			InitialBaseDirText + "/right_stock_icon2.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/right_port2.png");
		leftScore = new JAliasedTextField("");
		rightScore = new JAliasedTextField("");
		flatLayoutButton = new JAliasedButton("t");
		show2Button = new JAliasedButton("t");
		showCommentatorsButton = new JAliasedButton("t");
		leftScoreInc = new JAliasedButton("+");
		leftScoreDec = new JAliasedButton("-");
		rightScoreInc = new JAliasedButton("+");
		rightScoreDec = new JAliasedButton("-");
		leftCommentatorName = new JAliasedComboBox();
		rightCommentatorName = new JAliasedComboBox();
		bracketPosition = new JAliasedTextField("WR1");
		roundFormat = new JAliasedTextField("Best of 5");
		switchStockIcons1 = new JAliasedButton("switch");
		switchStockIcons2 = new JAliasedButton("switch");
		switchNames1 = new JAliasedButton("switch");
		switchNames2 = new JAliasedButton("switch");
		switchPorts1 = new JAliasedButton("switch");
		switchPorts2 = new JAliasedButton("switch");
		switchScore = new JAliasedButton("switch");
		switchCommentatorNames = new JAliasedButton("switch");
		reloadFilesButton = new JAliasedButton("save & reload");
		leftScoreLabel = new JAliasedLabel("Left Score");
		rightScoreLabel = new JAliasedLabel("Right Score");
		bracketPositionLabel = new JAliasedLabel("Bracket Position");
		roundFormatLabel = new JAliasedLabel("Round Format");
		leftCommentatorLabel = new JAliasedLabel("Left Commentator");
		rightCommentatorLabel = new JAliasedLabel("Right Commentator");

		NamesBrowseButton = new JAliasedButton("Browse...");
		LeftNameBrowseButton = new JAliasedButton("Browse...");
		Left2NameBrowseButton = new JAliasedButton("Browse...");
		RightNameBrowseButton = new JAliasedButton("Browse...");
		Right2NameBrowseButton = new JAliasedButton("Browse...");
		LeftScoreBrowseButton = new JAliasedButton("Browse...");
		RightScoreBrowseButton = new JAliasedButton("Browse...");
		BracketPositionBrowseButton = new JAliasedButton("Browse...");
		RoundFormatBrowseButton = new JAliasedButton("Browse...");
		LeftCommentatorBrowseButton = new JAliasedButton("Browse...");
		RightCommentatorBrowseButton = new JAliasedButton("Browse...");
		LeftStockIconBrowseButton = new JAliasedButton("Browse...");
		Left2StockIconBrowseButton = new JAliasedButton("Browse...");
		RightStockIconBrowseButton = new JAliasedButton("Browse...");
		Right2StockIconBrowseButton = new JAliasedButton("Browse...");
		StockIconDirBrowseButton = new JAliasedButton("Browse...");
		PortsDirBrowseButton = new JAliasedButton("Browse...");


		JPanel paneUpdating = new JPanel(null);
		JPanel paneSettings = new JPanel(null);
		paneUpdating.setPreferredSize(paneUpdating.getPreferredSize());
		//paneSettings.setPreferredSize(paneSettings.getPreferredSize());
		paneSettings.setPreferredSize(new Dimension(5 + (GUI.dir_text_field_width * 2) + GUI.small_gap_width,
			1000));
		settingsPaneScroller = new JScrollPane(paneSettings);
		paneSettings.setAutoscrolls(true);
		//settingsPaneScroller.setPreferredSize(new Dimension(800, 300));
		settingsPaneScroller.getVerticalScrollBar().setUnitIncrement(16);
		//settingsPaneScroller.setPreferredSize(new Dimension(paneSettings.getWidth(), paneSettings.getHeight()));
		paneUpdating.validate();
		paneSettings.validate();
		JFrame frame = new JFrame("Updater");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(GUI.window_width, GUI.window_height);
		//frame.setLocation(10, 40);
		frame.setVisible(true);
		// frame.setIconImage(new ImageIcon(imgURL).getImage());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		/* Create a mouse adapter for the JLabels to allow for easy zero-ing of the scores */
		MouseAdapter zeroLabels = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				leftScoreValue = 0;
				rightScoreValue = 0;
				writeToFile("" + leftScoreValue, leftScoreFile);
				writeToFile("" + rightScoreValue, rightScoreFile);
				leftScore.setText("" + leftScoreValue);
				rightScore.setText("" + rightScoreValue);
			}
		};
		flatLayoutButton.setBounds(left1PS.getX(),
			left1PS.getY(),
			GUI.element_height,
			GUI.element_height);
		flatLayoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				flatLayout = !flatLayout;
				toggleFlatLayout();
			}
		});
		// Second set of names
		show2Button.setBounds(left1PS.getX(),
			left1PS.getY() + left1PS.getHeight() + GUI.small_gap_width,
			GUI.element_height,
			GUI.element_height);
		show2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				show2 = !show2;
				toggle2();
			}
		});
		showCommentatorsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCommentators = !showCommentators;
				toggleCommentators();
			}
		});

		/* Set the location of the elements */
		setElementPositionsStandardLayout();

		switchStockIcons1.setMargin(new Insets(0, 0, 0, 0));
		switchStockIcons1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right1PS.getChosenImageFileName() != null) {
					prefs.put(LEFT1_CHAR_IMAGE, (String) right1PS.getChosenImageFileName());
				}
				if (left1PS.getChosenImageFileName() != null) {
					prefs.put(RIGHT1_CHAR_IMAGE, (String) left1PS.getChosenImageFileName());
				}

				String tempLeft = left1PS.getCharacter();
				int tempLeftIndex = left1PS.getChosenImageIndex();
				int tempRightIndex = right1PS.getChosenImageIndex();

				left1PS.setCharacter(right1PS.getCharacter());
				right1PS.setCharacter(tempLeft);
				left1PS.setChosenImageIndex(tempRightIndex);
				right1PS.setChosenImageIndex(tempLeftIndex);

				prefs.put(LEFT1_CHAR, left1PS.getCharacter());
				prefs.put(RIGHT1_CHAR, right1PS.getCharacter());
			}
		});
		switchStockIcons2.setMargin(new Insets(0, 0, 0, 0));
		switchStockIcons2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right2PS.getChosenImageFileName() != null) {
					prefs.put(LEFT2_CHAR_IMAGE, (String) right2PS.getChosenImageFileName());
				}
				if (left2PS.getChosenImageFileName() != null) {
					prefs.put(RIGHT2_CHAR_IMAGE, (String) left2PS.getChosenImageFileName());
				}

				String tempLeft = left2PS.getCharacter();
				int tempLeftIndex = left2PS.getChosenImageIndex();
				int tempRightIndex = right2PS.getChosenImageIndex();

				left2PS.setCharacter(right2PS.getCharacter());
				right2PS.setCharacter(tempLeft);
				left2PS.setChosenImageIndex(tempRightIndex);
				right2PS.setChosenImageIndex(tempLeftIndex);

				prefs.put(LEFT2_CHAR, left2PS.getCharacter());
				prefs.put(RIGHT2_CHAR, right2PS.getCharacter());
			}
		});

		switchNames1.setMargin(new Insets(0, 0, 0, 0));
		switchNames1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftName = left1PS.getName();
				left1PS.setName(right1PS.getName());
				right1PS.setName(tempLeftName);
			}
		});
		switchNames2.setMargin(new Insets(0, 0, 0, 0));
		switchNames2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftName = left2PS.getName();
				left2PS.setName(right2PS.getName());
				right2PS.setName(tempLeftName);
			}
		});

		switchPorts1.setMargin(new Insets(0, 0, 0, 0));
		switchPorts1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeft = (String) left1PS.getPortsFileName();
				left1PS.setPortsFileName(right1PS.getPortsFileName());
				right1PS.setPortsFileName(tempLeft);

				prefs.put(LEFT1_PORT_FILE_PATH, (String) left1PS.getPortsFileName());
				prefs.put(RIGHT1_PORT_FILE_PATH, (String) right1PS.getPortsFileName());
			}
		});
		switchPorts2.setMargin(new Insets(0, 0, 0, 0));
		switchPorts2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeft = (String) left2PS.getPortsFileName();
				left2PS.setPortsFileName(right2PS.getPortsFileName());
				right2PS.setPortsFileName(tempLeft);

				prefs.put(LEFT2_PORT_FILE_PATH, (String) left2PS.getPortsFileName());
				prefs.put(RIGHT2_PORT_FILE_PATH, (String) right2PS.getPortsFileName());
			}
		});


		/* Left Score Elements */
		leftScoreLabel.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		leftScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rightScoreLabel.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		rightScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		leftScoreDec.setFont(new Font("Arial", Font.BOLD, GUI.score_font_size));
		leftScoreDec.setMargin(new Insets(0, 0, 0, 0));
		leftScore.setFont(new Font("Arial", Font.BOLD, GUI.score_font_size));
		leftScore.setHorizontalAlignment(SwingConstants.CENTER);
		leftScore.setEditable(false);
		leftScore.addMouseListener(zeroLabels);
		leftScoreInc.setFont(new Font("Arial", Font.BOLD, GUI.score_font_size));
		leftScoreInc.setMargin(new Insets(0, 0, 0, 0));
		/* Switch Score Element */
		switchScore.setMargin(new Insets(0, 0, 0, 0));
		/* Right Score Elements */
		rightScoreDec.setFont(new Font("Arial", Font.BOLD, GUI.score_font_size));
		rightScoreDec.setMargin(new Insets(0, 0, 0, 0));
		rightScore.setFont(new Font("Arial", Font.BOLD, GUI.score_font_size));
		rightScore.setHorizontalAlignment(SwingConstants.CENTER);
		rightScore.setEditable(false);
		rightScore.addMouseListener(zeroLabels);
		rightScoreInc.setFont(new Font("Arial", Font.BOLD, GUI.score_font_size));
		rightScoreInc.setMargin(new Insets(0, 0, 0, 0));
		/* Bracket Position Elements */
		bracketPositionLabel.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		bracketPositionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		roundFormatLabel.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		roundFormatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bracketPosition.setFont(new Font("Arial", Font.BOLD, GUI.text_field_font_size));
		bracketPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeToFile("" + bracketPosition.getText(), bracketPositionFile);
			}
		});
		roundFormat.setFont(new Font("Arial", Font.BOLD, GUI.text_field_font_size));
		roundFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeToFile("" + roundFormat.getText(), roundFormatFile);
			}
		});
		leftCommentatorLabel.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		leftCommentatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rightCommentatorLabel.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		rightCommentatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		leftCommentatorName.setMaximumRowCount(GUI.names_visible_rows);
		leftCommentatorName.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		leftCommentatorName.setEditable(true);
		// The combobox must be set to editable before this method call will work
		leftCommentatorName.setSelectedItem(readFromFile(leftCommentatorNameFile));
		leftCommentatorName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leftCommentatorNameValue = (String) leftCommentatorName.getSelectedItem();
				writeToFile(leftCommentatorNameValue, leftCommentatorNameFile);
			}
		});

		rightCommentatorName.setMaximumRowCount(GUI.names_visible_rows);
		rightCommentatorName.setFont(new Font("Arial", Font.BOLD, GUI.name_font_size));
		rightCommentatorName.setEditable(true);
		// The combobox must be set to editable before this method call will work
		rightCommentatorName.setSelectedItem(readFromFile(rightCommentatorNameFile));
		rightCommentatorName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightCommentatorNameValue = (String) rightCommentatorName.getSelectedItem();
				writeToFile(rightCommentatorNameValue, rightCommentatorNameFile);
			}
		});

		switchCommentatorNames.setMargin(new Insets(0, 0, 0, 0));
		switchCommentatorNames.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftCommentatorName = leftCommentatorNameValue;
				leftCommentatorNameValue = rightCommentatorNameValue;
				leftCommentatorName.setSelectedItem("" + rightCommentatorNameValue);
				rightCommentatorNameValue = tempLeftCommentatorName;
				rightCommentatorName.setSelectedItem("" + tempLeftCommentatorName);
			}
		});
		/* Initialize the score elements and below to the right position */
		show2 = false;
		showCommentators = false;
		flatLayout = false;
		toggle2();
		toggleCommentators();
		toggleFlatLayout();

		/* Settings elements */
		namesLabel.setBounds(5, 5, GUI.dir_text_field_width * 2 + GUI.small_gap_width, GUI.dir_label_element_height);
		namesLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		namesText.setBounds(
			namesLabel.getX(),
			namesLabel.getY() + namesLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		namesText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		NamesBrowseButton.setBounds(
			namesText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			namesText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		NamesBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		NamesBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Names file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						namesText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(FILE_NAMES, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		namesFieldsLabel.setBounds(
			namesText.getX(),
			namesText.getY() + namesText.getHeight() + GUI.small_gap_width,
			GUI.dir_text_field_width,
			GUI.dir_text_element_height);
		namesFieldsLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		leftNameText.setBounds(
			namesText.getX(),
			namesFieldsLabel.getY() + namesFieldsLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		leftNameText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftNameBrowseButton.setBounds(
			leftNameText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			leftNameText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		LeftNameBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftNameBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Left Name file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						leftNameText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(LEFT1_NAME_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		left2NameText.setBounds(
			leftNameText.getX(),
			leftNameText.getY() + leftNameText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		left2NameText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Left2NameBrowseButton.setBounds(
			left2NameText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			left2NameText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		Left2NameBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Left2NameBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Left2 Name file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						left2NameText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(LEFT2_NAME_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		rightNameText.setBounds(
			left2NameText.getX(),
			left2NameText.getY() + left2NameText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		rightNameText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightNameBrowseButton.setBounds(
			rightNameText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			rightNameText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		RightNameBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightNameBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Right Name file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						rightNameText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(RIGHT1_NAME_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		right2NameText.setBounds(
			rightNameText.getX(),
			rightNameText.getY() + rightNameText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		right2NameText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Right2NameBrowseButton.setBounds(
			right2NameText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			right2NameText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		Right2NameBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Right2NameBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Right2 Name file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						right2NameText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(RIGHT2_NAME_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		scoresFieldsLabel.setBounds(
			right2NameText.getX(),
			right2NameText.getY() + right2NameText.getHeight() + GUI.small_gap_width,
			GUI.dir_text_field_width,
			GUI.dir_text_element_height);
		scoresFieldsLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		leftScoreText.setBounds(
			scoresFieldsLabel.getX(),
			scoresFieldsLabel.getY() + scoresFieldsLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		leftScoreText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftScoreBrowseButton.setBounds(
			leftScoreText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			leftScoreText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		LeftScoreBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftScoreBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Left Score file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						leftScoreText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(LEFT_SCORE_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		rightScoreText.setBounds(
			leftScoreText.getX(),
			leftScoreText.getY() + leftScoreText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		rightScoreText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightScoreBrowseButton.setBounds(
			rightScoreText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			rightScoreText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		RightScoreBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightScoreBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Right Score file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						rightScoreText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(RIGHT_SCORE_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		setInfoFieldsLabel.setBounds(
			leftScoreText.getX(),
			rightScoreText.getY() + rightScoreText.getHeight() + GUI.small_gap_width,
			GUI.dir_text_field_width * 2,
			GUI.dir_text_element_height);
		setInfoFieldsLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		bracketPositionText.setBounds(
			setInfoFieldsLabel.getX(),
			setInfoFieldsLabel.getY() + setInfoFieldsLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		bracketPositionText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		BracketPositionBrowseButton.setBounds(
			bracketPositionText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			bracketPositionText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		BracketPositionBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		BracketPositionBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Bracket Position file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						bracketPositionText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(BRACKET_POSITION_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		roundFormatText.setBounds(
			bracketPositionText.getX(),
			bracketPositionText.getY() + bracketPositionText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		roundFormatText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RoundFormatBrowseButton.setBounds(
			roundFormatText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			roundFormatText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		RoundFormatBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RoundFormatBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Round Format file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						roundFormatText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(ROUND_FORMAT_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		commentatorFieldsLabel.setBounds(
			roundFormatText.getX(),
			roundFormatText.getY() + roundFormatText.getHeight() + GUI.small_gap_width,
			GUI.dir_text_field_width * 2,
			GUI.dir_text_element_height);
		commentatorFieldsLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		leftCommentatorNameText.setBounds(
			commentatorFieldsLabel.getX(),
			commentatorFieldsLabel.getY() + commentatorFieldsLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		leftCommentatorNameText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftCommentatorBrowseButton.setBounds(
			leftCommentatorNameText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			leftCommentatorNameText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		LeftCommentatorBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftCommentatorBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Left Commentator Name file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						leftCommentatorNameText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(LEFT_COMMENTATOR_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		rightCommentatorNameText.setBounds(
			leftCommentatorNameText.getX(),
			leftCommentatorNameText.getY() + leftCommentatorNameText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		rightCommentatorNameText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightCommentatorBrowseButton.setBounds(
			rightCommentatorNameText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			rightCommentatorNameText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		RightCommentatorBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightCommentatorBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Right Commentator Name file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						rightCommentatorNameText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(RIGHT_COMMENTATOR_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		stockIconFieldsLabel.setBounds(
			rightCommentatorNameText.getX(),
			rightCommentatorNameText.getY() + rightCommentatorNameText.getHeight() + GUI.small_gap_width,
			GUI.dir_text_field_width * 2,
			GUI.dir_text_element_height);
		stockIconFieldsLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		leftStockIconText.setBounds(
			stockIconFieldsLabel.getX(),
			stockIconFieldsLabel.getY() + stockIconFieldsLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		leftStockIconText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftStockIconBrowseButton.setBounds(
			leftStockIconText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			leftStockIconText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		LeftStockIconBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		LeftStockIconBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Left Stock Icon file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						leftStockIconText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(LEFT1_STOCK_ICON_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		left2StockIconText.setBounds(
			leftStockIconText.getX(),
			leftStockIconText.getY() + leftStockIconText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		left2StockIconText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Left2StockIconBrowseButton.setBounds(
			left2StockIconText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			left2StockIconText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		Left2StockIconBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Left2StockIconBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Left2 Stock Icon file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						left2StockIconText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(LEFT2_STOCK_ICON_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		rightStockIconText.setBounds(
			left2StockIconText.getX(),
			left2StockIconText.getY() + left2StockIconText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		rightStockIconText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightStockIconBrowseButton.setBounds(
			rightStockIconText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			rightStockIconText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		RightStockIconBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		RightStockIconBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Right Stock Icon file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						rightStockIconText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(RIGHT1_STOCK_ICON_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		right2StockIconText.setBounds(
			rightStockIconText.getX(),
			rightStockIconText.getY() + rightStockIconText.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		right2StockIconText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Right2StockIconBrowseButton.setBounds(
			right2StockIconText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			right2StockIconText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		Right2StockIconBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		Right2StockIconBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// TODO: this should maybe change to users home directory?
				//jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Right2 Stock Icon file...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						right2StockIconText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(RIGHT2_STOCK_ICON_FP, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		StockIconDirLabel.setBounds(
			right2StockIconText.getX(),
			right2StockIconText.getY() + right2StockIconText.getHeight() + GUI.small_gap_width,
			GUI.dir_text_field_width * 2 + GUI.small_gap_width,
			GUI.dir_label_element_height);
		StockIconDirLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		StockIconDirText.setBounds(
			StockIconDirLabel.getX(),
			StockIconDirLabel.getY() + StockIconDirLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		StockIconDirText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		StockIconDirBrowseButton.setBounds(
			StockIconDirText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			StockIconDirText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		StockIconDirBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		StockIconDirBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(prefs.get(FILE_STOCK_ICONS, DEFAULT_FILE_STOCK_ICONS)));
				jfc.setDialogTitle("Select Stock Icon Directory...");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						StockIconDirText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(FILE_STOCK_ICONS, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		PortsDirLabel.setBounds(
			StockIconDirText.getX(),
			StockIconDirText.getY() + StockIconDirText.getHeight() + GUI.small_gap_width,
			GUI.dir_text_field_width * 2 + GUI.small_gap_width,
			GUI.dir_label_element_height);
		PortsDirLabel.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		PortsDirText.setBounds(
			PortsDirLabel.getX(),
			PortsDirLabel.getY() + PortsDirLabel.getHeight() + GUI.small_gap_width,
			(GUI.dir_text_field_width * 2) - GUI.button_width,
			GUI.dir_text_element_height);
		PortsDirText.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		PortsDirBrowseButton.setBounds(
			PortsDirText.getX() + GUI.dir_text_field_width * 2 + GUI.small_gap_width - GUI.button_width,
			PortsDirText.getY(),
			GUI.browse_button_width,
			GUI.dir_text_element_height);
		PortsDirBrowseButton.setFont(new Font("Arial", Font.BOLD, GUI.dir_text_field_font_size));
		PortsDirBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Ports Directory...");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						PortsDirText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(FILE_PORTS, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});


		/* TODO: check these bound-settings for the non-settings elements */
		bracketPositionLabel.setBounds(
			leftScoreDec.getX(),
			leftScoreDec.getY() + leftScoreDec.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		bracketPosition.setBounds(
			bracketPositionLabel.getX(),
			bracketPositionLabel.getY() + bracketPositionLabel.getHeight() + GUI.small_gap_width,
			GUI.combo_box_width,
			GUI.element_height);
		bracketPosition.setFont(new Font("Arial", Font.BOLD, GUI.text_field_font_size));
		showCommentatorsButton.setBounds(bracketPosition.getX(),
			bracketPosition.getY() + bracketPosition.getHeight() + GUI.small_gap_width,
			GUI.element_height,
			GUI.element_height);
		reloadFilesButton.setBounds(
			PortsDirText.getX(),
			PortsDirText.getY() + PortsDirText.getHeight() + GUI.small_gap_width,
			GUI.button_width * 2,
			GUI.element_height);
		reloadFilesButton.setMargin(new Insets(0, 0, 0, 0));
		reloadFilesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				namesFile = new File(namesText.getText());
				stockIconDir = new File(StockIconDirText.getText());
				portsDir = new File(PortsDirText.getText());

				/* Update file paths of elements to use saved settings */
				leftNameFile = new File(leftNameText.getText());
				rightNameFile = new File(rightNameText.getText());
				left2NameFile = new File(left2NameText.getText());
				right2NameFile = new File(right2NameText.getText());

				leftScoreFile = new File(leftScoreText.getText());
				rightScoreFile = new File(rightScoreText.getText());

				bracketPositionFile = new File(bracketPositionText.getText());
				roundFormatFile = new File(roundFormatText.getText());

				leftCommentatorNameFile = new File(leftCommentatorNameText.getText());
				rightCommentatorNameFile = new File(rightCommentatorNameText.getText());

				leftStockIconFile = new File(leftStockIconText.getText());
				rightStockIconFile = new File(rightStockIconText.getText());
				left2StockIconFile = new File(left2StockIconText.getText());
				right2StockIconFile = new File(right2StockIconText.getText());

				/* Save file paths program remembers */
				prefs.put(FILE_NAMES, namesText.getText());

				prefs.put(LEFT1_NAME_FP, leftNameText.getText());
				prefs.put(RIGHT1_NAME_FP, rightNameText.getText());
				prefs.put(LEFT2_NAME_FP, left2NameText.getText());
				prefs.put(RIGHT2_NAME_FP, right2NameText.getText());

				prefs.put(LEFT_SCORE_FP, leftScoreText.getText());
				prefs.put(RIGHT_SCORE_FP, rightScoreText.getText());

				prefs.put(BRACKET_POSITION_FP, bracketPositionText.getText());
				prefs.put(ROUND_FORMAT_FP, roundFormatText.getText());

				prefs.put(LEFT_COMMENTATOR_FP, leftCommentatorNameText.getText());
				prefs.put(RIGHT_COMMENTATOR_FP, rightCommentatorNameText.getText());

				prefs.put(LEFT1_STOCK_ICON_FP, leftStockIconText.getText());
				prefs.put(RIGHT1_STOCK_ICON_FP, rightStockIconText.getText());
				prefs.put(LEFT2_STOCK_ICON_FP, left2StockIconText.getText());
				prefs.put(RIGHT2_STOCK_ICON_FP, right2StockIconText.getText());

				prefs.put(FILE_STOCK_ICONS, StockIconDirText.getText());
				prefs.put(FILE_PORTS, PortsDirText.getText());
				updateElements();
			}
		});
		/* Add button functionality */
		leftScoreDec.addActionListener(this);
		leftScoreInc.addActionListener(this);
		rightScoreDec.addActionListener(this);
		rightScoreInc.addActionListener(this);
		switchScore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int tempLeftScore = leftScoreValue;
				leftScoreValue = rightScoreValue;
				leftScore.setText("" + rightScoreValue);
				rightScoreValue = tempLeftScore;
				rightScore.setText("" + tempLeftScore);
				writeToFile("" + leftScoreValue, leftScoreFile);
				writeToFile("" + rightScoreValue, rightScoreFile);
			}
		});
		/* Read files to set text elements */
		updateElements();
		/* These action listeners must be added after 'updateElements()'
		   so as not to be triggered by method calls in 'updateElements()' */
		left1PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (left1PS.getCharacter() != null) {
					prefs.put(LEFT1_CHAR, left1PS.getCharacter());
				}
			}
		});
		right1PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right1PS.getCharacter() != null) {
					prefs.put(RIGHT1_CHAR, right1PS.getCharacter());
				}
			}
		});
		left2PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (left2PS.getCharacter() != null) {
					prefs.put(LEFT2_CHAR, left2PS.getCharacter());
				}
			}
		});
		right2PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right2PS.getCharacter() != null) {
					prefs.put(RIGHT2_CHAR, right2PS.getCharacter());
				}
			}
		});

		left1PS.addStockListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (left1PS.getChosenImageFileName() != null) {
					prefs.put(LEFT1_CHAR_IMAGE, (String) left1PS.getChosenImageFileName());
				}
			}
		});
		right1PS.addStockListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (right1PS.getChosenImageFileName() != null) {
					prefs.put(RIGHT1_CHAR_IMAGE, (String) right1PS.getChosenImageFileName());
				}
			}
		});
		left2PS.addStockListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (left2PS.getChosenImageFileName() != null) {
					prefs.put(LEFT2_CHAR_IMAGE, (String) left2PS.getChosenImageFileName());
				}
			}
		});
		right2PS.addStockListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (right2PS.getChosenImageFileName() != null) {
					prefs.put(RIGHT2_CHAR_IMAGE, (String) right2PS.getChosenImageFileName());
				}
			}
		});

		left1PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (left1PS.getPortsFileName() != null) {
					prefs.put(LEFT1_PORT_FILE_PATH, (String) left1PS.getPortsFileName());
				}
			}
		});
		right1PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (right1PS.getPortsFileName() != null) {
					prefs.put(RIGHT1_PORT_FILE_PATH, (String) right1PS.getPortsFileName());
				}
			}
		});
		left2PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (left2PS.getPortsFileName() != null) {
					prefs.put(LEFT2_PORT_FILE_PATH, (String) left2PS.getPortsFileName());
				}
			}
		});
		right2PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (right2PS.getPortsFileName() != null) {
					prefs.put(RIGHT2_PORT_FILE_PATH, (String) right2PS.getPortsFileName());
				}
			}
		});
		/* Add elements to the window */
		left1PS.addToPanel(paneUpdating);
		right1PS.addToPanel(paneUpdating);
		left2PS.addToPanel(paneUpdating);
		right2PS.addToPanel(paneUpdating);
		paneUpdating.add(switchStockIcons1);
		paneUpdating.add(switchStockIcons2);
		paneUpdating.add(switchNames1);
		paneUpdating.add(switchNames2);
		paneUpdating.add(switchPorts1);
		paneUpdating.add(switchPorts2);

		paneUpdating.add(flatLayoutButton);
		paneUpdating.add(show2Button);
		paneUpdating.add(showCommentatorsButton);

		paneUpdating.add(leftScoreLabel);
		paneUpdating.add(rightScoreLabel);
		paneUpdating.add(leftScoreDec);
		paneUpdating.add(leftScore);
		paneUpdating.add(leftScoreInc);
		paneUpdating.add(switchScore);
		paneUpdating.add(rightScoreDec);
		paneUpdating.add(rightScore);
		paneUpdating.add(rightScoreInc);

		paneUpdating.add(bracketPositionLabel);
		paneUpdating.add(roundFormatLabel);
		paneUpdating.add(bracketPosition);
		paneUpdating.add(roundFormat);

		paneUpdating.add(leftCommentatorLabel);
		paneUpdating.add(rightCommentatorLabel);
		paneUpdating.add(leftCommentatorName);
		paneUpdating.add(switchCommentatorNames);
		paneUpdating.add(rightCommentatorName);

		paneSettings.add(namesLabel);
		paneSettings.add(namesText);
		paneSettings.add(NamesBrowseButton);

		paneSettings.add(namesFieldsLabel);
		paneSettings.add(leftNameText);
		paneSettings.add(left2NameText);
		paneSettings.add(rightNameText);
		paneSettings.add(right2NameText);
		paneSettings.add(LeftNameBrowseButton);
		paneSettings.add(Left2NameBrowseButton);
		paneSettings.add(RightNameBrowseButton);
		paneSettings.add(Right2NameBrowseButton);

		paneSettings.add(scoresFieldsLabel);
		paneSettings.add(leftScoreText);
		paneSettings.add(rightScoreText);
		paneSettings.add(LeftScoreBrowseButton);
		paneSettings.add(RightScoreBrowseButton);

		paneSettings.add(setInfoFieldsLabel);
		paneSettings.add(bracketPositionText);
		paneSettings.add(roundFormatText);
		paneSettings.add(BracketPositionBrowseButton);
		paneSettings.add(RoundFormatBrowseButton);

		paneSettings.add(commentatorFieldsLabel);
		paneSettings.add(leftCommentatorNameText);
		paneSettings.add(rightCommentatorNameText);
		paneSettings.add(LeftCommentatorBrowseButton);
		paneSettings.add(RightCommentatorBrowseButton);

		paneSettings.add(stockIconFieldsLabel);
		paneSettings.add(leftStockIconText);
		paneSettings.add(left2StockIconText);
		paneSettings.add(rightStockIconText);
		paneSettings.add(right2StockIconText);
		paneSettings.add(LeftStockIconBrowseButton);
		paneSettings.add(Left2StockIconBrowseButton);
		paneSettings.add(RightStockIconBrowseButton);
		paneSettings.add(Right2StockIconBrowseButton);

		paneSettings.add(StockIconDirLabel);
		paneSettings.add(StockIconDirText);
		paneSettings.add(StockIconDirBrowseButton);
		paneSettings.add(PortsDirLabel);
		paneSettings.add(PortsDirText);
		paneSettings.add(PortsDirBrowseButton);
		paneSettings.add(reloadFilesButton);
		tabbedPane.addTab("Updating", paneUpdating);
		tabbedPane.addTab("Settings", settingsPaneScroller);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

	public static void main(String arg[]) {
		new Updater();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (leftScoreDec == e.getSource()) {
			leftScoreDec.setEnabled(false);
			leftScoreValue--;
			writeToFile("" + leftScoreValue, leftScoreFile);
			leftScore.setText("" + leftScoreValue);
			leftScoreDec.setEnabled(true);
		} else if (leftScoreInc == e.getSource()) {
			leftScoreInc.setEnabled(false);
			leftScoreValue++;
			writeToFile("" + leftScoreValue, leftScoreFile);
			leftScore.setText("" + leftScoreValue);
			leftScoreInc.setEnabled(true);
		} else if (rightScoreDec == e.getSource()) {
			rightScoreDec.setEnabled(false);
			rightScoreValue--;
			writeToFile("" + rightScoreValue, rightScoreFile);
			rightScore.setText("" + rightScoreValue);
			rightScoreDec.setEnabled(true);
		} else if (rightScoreInc == e.getSource()) {
			rightScoreInc.setEnabled(false);
			rightScoreValue++;
			writeToFile("" + rightScoreValue, rightScoreFile);
			rightScore.setText("" + rightScoreValue);
			rightScoreInc.setEnabled(true);
		}
	}

}
