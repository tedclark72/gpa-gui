/**
*@author Teddy Clark (ejc7re)
*Homework 5
*Sources: JTable API, Model API, MouseAdapter/MouseEvent API, Font API, FlowLayout API, Box Layout API
*			Dimension API, ScrollPane API, DecimalFormat API, Double API
**/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.DecimalFormat;


public class GpaGui {
	//Main Display Fields
	public static JFrame frame;
	public static JScrollPane pane;
	public static JScrollPane pane2;
	public static JPanel pnlCourseTitle;
	public static JPanel pnlSummaryTitle;
	public static JPanel courseOptions;
	public static JPanel courseTable;
	public static JPanel summaryCreditOptions;
	public static JPanel summaryGPAOptions;
	public static JLabel coursesTitle;
	public static JLabel summaryTitle;
	
	//Fields that deal with gpa
	public static JLabel currentGpa;
	public static JLabel requiredGpa;
	public static JLabel targetGpa;
	public static JButton addTargetGpa;
	public static JButton confirmTargetGpa;
	public static JTextField enterTargetGpa;
	
	//Fields that deal with Credits
	public static JLabel totalCreditsLbl;
	public static JLabel currentCreditsLbl;
	public static JLabel untakenCreditsLbl;
	
	//Fields that deal with Course Table
	public static JTable courses;
	public static String strGrade;
	public static String strName;
	public static String strCreditHours;
	public static String strStatus;
	public static DefaultTableModel model;

	//Fields to Remove a Course
	public static JButton removeCourse;
	public static JButton removedCourse;
	public static JButton removeAll;
	public static JTextField courseToBeRemoved;
	
	//Fields to Add a Course
	public static JButton addedCourse;
	public static JButton newCourse;
	public static JButton add15Hours;
	public static JTextField creditHours;
	public static JTextField courseName;
	public static JTextField grade;
	public static JComboBox courseStatus;

	//Number Fields
	public static double currentCredits;
	public static double untakenCredits;
	public static double totalCredits;
	public static double numCurrentGpa;
	public static double numRequiredGpa;
	public static double numTargetGpa;
	public static String[] statuses = {"Previously Taken", "Currently Enrolled", "Anticipated Course"}; 
	
	//Constructor
	public GpaGui() {
		initialize();
	}
	
	/**Initial Frame Setup**/
	public void initialize() {
		//Setting up the Frame
		frame = new JFrame();
		frame.setSize(1024, 768);
		frame.setTitle("GPA Calculator and Planner by Teddy Clark");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		//Creating JPanels
		pnlCourseTitle = new JPanel();
		courseOptions = new JPanel();
		courseOptions.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 1));
		courseTable = new JPanel();
		pnlSummaryTitle = new JPanel();
		summaryCreditOptions = new JPanel();
		summaryCreditOptions.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 1));
		summaryGPAOptions = new JPanel();
		summaryGPAOptions.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 1));
		
		//Setting up courseTitle
		coursesTitle = new JLabel("Course Options");
		pnlCourseTitle.add(coursesTitle);
		coursesTitle.setFont(new Font("Sans Serif", Font.PLAIN, 24));
		
		//Adding New Course Button
		newCourse = new JButton("Add a Course");
		courseOptions.add(newCourse);
		newCourse.addActionListener(new HandleNewCoursePress());

		//These fields appear only when New Course has been pressed
		courseName = new JTextField("  (Optional) Name of Course  ");
		creditHours = new JTextField("  Number of Credit Hours  ");
		grade = new JTextField("  (Optional) Letter or 4.0 Grade  ");
		//This code adds MouseListeners to each textbox to delete inner text
		creditHours.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				creditHours.setText("");
			}
		});
		grade.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				grade.setText("");
			}
		});
		courseName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				courseName.setText("");
			}
		});
		//Set up rest of add new Course Button
		courseStatus = new JComboBox(statuses);
		addedCourse = new JButton("Add");
		addedCourse.addActionListener(new HandleCourseAdded());
		//Add options to JPanel but make them invisible
		courseOptions.add(courseName);
		courseOptions.add(creditHours);
		courseOptions.add(grade); 
		courseOptions.add(courseStatus);
		courseOptions.add(addedCourse);
		creditHours.setVisible(false); 
		grade.setVisible(false); 
		courseName.setVisible(false);
		addedCourse.setVisible(false);
		courseStatus.setVisible(false);
		
		//Add 15 Credit Hours button
		add15Hours = new JButton("Add 15 Blank Credit Hours");
		courseOptions.add(add15Hours);
		add15Hours.addActionListener(new Handle15CoursePress());
	
		//Add Remove Course button
		removeCourse = new JButton("Remove a Course");
		courseOptions.add(removeCourse);
		removeCourse.addActionListener(new HandleRemoveCoursePress());
		
		//Appears when RemoveCourse has been pressed
		courseToBeRemoved = new JTextField("  Enter the row number of the course you'd like to remove  ");
		courseOptions.add(courseToBeRemoved);
		courseToBeRemoved.setVisible(false);
		//Adds a mouselistener to delete inner text when clicked
		courseToBeRemoved.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				courseToBeRemoved.setText("");
			}
		});
		removedCourse = new JButton("Remove");
		courseOptions.add(removedCourse);
		removedCourse.addActionListener(new HandleCourseRemoved());
		removedCourse.setVisible(false);
		
		//Add Remove all courses
		removeAll = new JButton("Remove all Courses");
		courseOptions.add(removeAll);
		removeAll.addActionListener(new HandleRemoveAll());
			
		//Set up the table that displays all current courses/gpas
		model = new DefaultTableModel();
		model.addColumn("Course Name"); model.addColumn("Credit Hours"); model.addColumn("Grade"); model.addColumn("Course Status");
		courses = new JTable(model);
		courseTable.add(courses);
		courses.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//Set width/rowheight of columns
		courses.getColumnModel().getColumn(0).setPreferredWidth(150); 
		courses.getColumnModel().getColumn(1).setPreferredWidth(150);
		courses.getColumnModel().getColumn(2).setPreferredWidth(150);
		courses.getColumnModel().getColumn(3).setPreferredWidth(150);
		courses.setRowHeight(25);
		//Sets the size to hold 45 courses. 
		courses.setPreferredSize(new Dimension(500, 1200));
		//Delete vertical lines
		courses.setShowVerticalLines(false);
		//Add Column titles
		model = (DefaultTableModel) courses.getModel();
		model.addRow(new String[] {"Course Name", "Credit Hours", "Grade", "Course Status"});
		
		//Add summary title
		summaryTitle = new JLabel("Summary");
		pnlSummaryTitle.add(summaryTitle);
		summaryTitle.setFont(new Font("Sans Serif", Font.PLAIN, 24));
		
		//Add credit labels
		totalCreditsLbl = new JLabel("Total Credits: 0");
		currentCreditsLbl = new JLabel("Current Credits: 0");
		untakenCreditsLbl = new JLabel("Untaken Credits: 0");
		summaryCreditOptions.add(totalCreditsLbl);
		summaryCreditOptions.add(currentCreditsLbl);
		summaryCreditOptions.add(untakenCreditsLbl);
		
		//Add current GPA Label
		currentGpa = new JLabel("Current GPA: Need more classes");
		summaryGPAOptions.add(currentGpa);
		
		//Add enter target gpa
		targetGpa = new JLabel("Target GPA: Not yet entered");
		addTargetGpa = new JButton("Enter Target GPA");
		enterTargetGpa = new JTextField("  Enter Target GPA (4.0 Scale)  ");
		confirmTargetGpa = new JButton("Confirm Target GPA");
		enterTargetGpa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				enterTargetGpa.setText("");
			}
		});
		addTargetGpa.addActionListener(new HandleTargetGpaPress());
		confirmTargetGpa.addActionListener(new HandleTargetGpaConfirmed());
		courseOptions.add(addTargetGpa);
		courseOptions.add(enterTargetGpa);
		courseOptions.add(confirmTargetGpa);
		summaryGPAOptions.add(targetGpa);
		confirmTargetGpa.setVisible(false);
		enterTargetGpa.setVisible(false);
		
		//Add required Gpa
		requiredGpa = new JLabel("Required GPA: Need target GPA");
		summaryGPAOptions.add(requiredGpa);
		
		//Add panels to frame
		frame.add(pnlCourseTitle, BorderLayout.BEFORE_FIRST_LINE);
		frame.add(courseOptions, BorderLayout.NORTH);
		frame.add(courseTable, BorderLayout.CENTER);
		frame.add(pnlSummaryTitle);
		frame.add(summaryCreditOptions, BorderLayout.WEST);
		frame.add(summaryGPAOptions, BorderLayout.AFTER_LAST_LINE);
		
		//Making the table scrollable
		pane = new JScrollPane(courseTable);
		frame.getContentPane().add(pane);
		frame.setVisible(true);
	}
	
	//ACTION LISTENERS
	/**Add a new course**/
	class HandleNewCoursePress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Adding a course...");
			
			//Change the visibilities of fields
			creditHours.setVisible(true);
			courseName.setVisible(true);
			grade.setVisible(true);
			addedCourse.setVisible(true);
			courseStatus.setVisible(true);
			
			newCourse.setVisible(false);
			add15Hours.setVisible(false);
			removeCourse.setVisible(false);
			removeAll.setVisible(false);
			addTargetGpa.setVisible(false);
		}
	}
	/**Confirm a course to be added**/
	class HandleCourseAdded implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Course Added!");
			
			//Get the values of the Textboxes
			strName =  courseName.getText();
			strGrade = grade.getText();
			strCreditHours = creditHours.getText();
			strStatus = (String)courseStatus.getSelectedItem();
			
			//These if statements check our variables and rename accordingly
			//Assuming only mistakes are user doesn't enter anything or leaves area blank
			if((courseName.getText().equals("") || courseName.getText().equals("  (Optional) Name of Course  ")) && (grade.getText().equals("") || grade.getText().equals("  (Optional) Letter Grade  "))) {
				strName = "Unknown Class";
				strGrade = "Unknown Grade";
			}
			else if(strName.equals("") || strName.equals("  (Optional) Name of Course  ")) {
				strName = "Unknown Class";
			}
			else if(strGrade.equals("") || strGrade.equals("  (Optional) Letter Grade  ")) {
				strGrade = "Unknown Grade";
			}
			
			//This line adds the class to the table if credits were entered & updates values
			model = (DefaultTableModel) courses.getModel();
			if(!(strCreditHours.equals("") || strCreditHours.equals("  Number of Credit Hours  "))) {
				model.addRow(new String[] {strName, strCreditHours, strGrade, strStatus});
				calcValues();
			}
			
			//Change visibility of fields
			creditHours.setVisible(false);
			courseName.setVisible(false);
			grade.setVisible(false);
			addedCourse.setVisible(false);
			courseStatus.setVisible(false);
			
			newCourse.setVisible(true);
			removeCourse.setVisible(true);
			removeAll.setVisible(true);
			add15Hours.setVisible(true);
			addTargetGpa.setVisible(true);
			
			//Set original textfields back
			creditHours.setText("  Number of Credit Hours  ");
			grade.setText("  (Optional) Letter Grade  ");
			courseName.setText("  (Optional) Name of Course  ");
			
			//Set currentGpa label if the Gpa is an actual number and update other labels
			if(!Double.isNaN(numCurrentGpa)) {
				currentGpa.setText("Current GPA: " + numCurrentGpa);
			}
			updateLabels();
		}
	}
	/**Add a 15 credit block**/
	class Handle15CoursePress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Added 15 Credit Hour Block!");
			
			//Add a row of 15 credits with these titles
			strName = "15 Credit Hour Block";
			strCreditHours = "15.0";
			strGrade = "Unknown Grade";
			strStatus = "Anticipated Course";
			
			model = (DefaultTableModel) courses.getModel();
			//Add the row to the table, calculate values, update labels
			model.addRow(new String[] {strName, strCreditHours, strGrade, strStatus});
			calcValues();
			updateLabels();
		}
	}
	/**Remove a course**/
	class HandleRemoveCoursePress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Removing a course...");
			
			//Change visibilities of fields
			courseToBeRemoved.setVisible(true);
			removedCourse.setVisible(true);
			
			newCourse.setVisible(false);
			add15Hours.setVisible(false);
			removeCourse.setVisible(false);
			removeAll.setVisible(false);
			addTargetGpa.setVisible(false);
		}
	}
	/**Confirm removed course**/
	class HandleCourseRemoved implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			model = (DefaultTableModel) courses.getModel();
			int row = 0;
			
			//Check if user actually entered a row
			if(!(courseToBeRemoved.getText().equals("") || courseToBeRemoved.getText().equals("  Enter the row number of the course you'd like to remove  "))) {
				row = Integer.parseInt(courseToBeRemoved.getText());
			}
			
			//Check if row index is valid
			if(row > 0 && row < model.getRowCount()) {
				model.removeRow(row);
				System.out.println("Course Removed");
			}
			else {
				System.out.println("Invalid row");
			}
			
			//Change visibilities of fields
			courseToBeRemoved.setVisible(false);
			removedCourse.setVisible(false);
			
			newCourse.setVisible(true);
			add15Hours.setVisible(true);
			removeCourse.setVisible(true);
			removeAll.setVisible(true);
			addTargetGpa.setVisible(true);
			
			//Reset textfield
			courseToBeRemoved.setText("  Enter the row number of the course you'd like to remove  ");
			
			//Update values and labels
			calcValues();
			updateLabels();
		}
	}
	/**Remove all Courses**/
	class HandleRemoveAll implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Removing all Courses!");
			
			model = (DefaultTableModel) courses.getModel();
			int rowCount = model.getRowCount();
			
			//Cycle through table, remove all rows except titles
			for(int i = rowCount - 1; i > 0; i--) {
				model.removeRow(i);
			}
			
			//Update values and labels
			calcValues();
			//Reset currentGpa label
			currentGpa.setText("Current GPA: Need more classes");
			updateLabels();
		}
	}
	/**Add target GPA**/
	class HandleTargetGpaPress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Adding a Target GPA...");
			
			//Change visibilities of fields
			enterTargetGpa.setVisible(true);
			confirmTargetGpa.setVisible(true);
			
			addTargetGpa.setVisible(false);
			newCourse.setVisible(false);
			add15Hours.setVisible(false);
			removeCourse.setVisible(false);
			removeAll.setVisible(false);
		}
	}
	/**Confirm target GPA**/
	class HandleTargetGpaConfirmed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {						
			//Ensure target Gpa is valid
			if(!enterTargetGpa.getText().equals("") && !enterTargetGpa.getText().equals("  Enter Target GPA (4.0 Scale)  ")) {
				numTargetGpa = Double.parseDouble(enterTargetGpa.getText());
				
				if(numTargetGpa > 0 && numTargetGpa <= 4.0) {
					//If valid, set GPA from text field
					targetGpa.setText("Target GPA: " + numTargetGpa);
				}
				else {
					targetGpa.setText("Invalid Target entered. Please update");
				}
			}
			else {
				targetGpa.setText("Invalid Target entered. Please update");
			}
			
			System.out.println("Target GPA of " + numTargetGpa + " added!");
			
			//Change Visibility of Fields
			enterTargetGpa.setVisible(false);
			confirmTargetGpa.setVisible(false);
			
			addTargetGpa.setVisible(true);
			newCourse.setVisible(true);
			add15Hours.setVisible(true);
			removeCourse.setVisible(true);
			removeAll.setVisible(true);
			
			//Reset Textfield
			enterTargetGpa.setText("  Enter Target GPA (4.0 Scale)  ");
			
			//Update values and labels
			calcValues();
			updateLabels();
		}
	}
	
	//METHODS
	/**This method calculates the values for credits and gpas**/
	public static void calcValues() {
		DecimalFormat fmt = new DecimalFormat("#.000");
		model = (DefaultTableModel) courses.getModel();
		int rowCount = model.getRowCount();
		
		//Initialize all values to 0 every time method is called
		totalCredits = 0;
		currentCredits = 0;
		untakenCredits = 0;
		numCurrentGpa = 0;
		numRequiredGpa = 0;
		
		//Calculating Credits
		for(int i = 1; i < rowCount; i++) {
			try {
				totalCredits += Double.parseDouble((String)model.getValueAt(i, 1));
				
				if(model.getValueAt(i, 3).equals("Anticipated Course")) {
					untakenCredits += Double.parseDouble((String)model.getValueAt(i, 1));
				}
				//Add to current credits only if grade is known
				else if(!model.getValueAt(i, 2).equals("Unknown Grade")) {
					currentCredits += Double.parseDouble((String)model.getValueAt(i, 1));
				}
			}
			catch(NumberFormatException e) {
				System.out.println("Invalid Entry");
			}
		}
		
		//Calculating GPA
		for(int i = 1; i < rowCount; i++) {
			//Convert Letter Grade to 4.0 Scale
			if((model.getValueAt(i, 3).equals("Previously Taken") || model.getValueAt(i, 3).equals("Currently Enrolled"))) {
				double gradeAtValue = 0;
				
				if(model.getValueAt(i, 2).equals("A+")) {
					gradeAtValue = 4.0;
				}
				else if(model.getValueAt(i, 2).equals("A")) {
					gradeAtValue = 4.0;
				}
				else if(model.getValueAt(i, 2).equals("A-")) {
					gradeAtValue = 3.7;
				}
				else if(model.getValueAt(i, 2).equals("B+")) {
					gradeAtValue = 3.3;
				}
				else if(model.getValueAt(i, 2).equals("B")) {
					gradeAtValue = 3.0;
				}
				else if(model.getValueAt(i, 2).equals("B-")) {
					gradeAtValue = 2.7;
				}
				else if(model.getValueAt(i, 2).equals("C+")) {
					gradeAtValue = 2.3;
				}
				else if(model.getValueAt(i, 2).equals("C")) {
					gradeAtValue = 2.0;
				}
				else if(model.getValueAt(i, 2).equals("C-")) {
					gradeAtValue = 1.7;
				}
				else if(model.getValueAt(i, 2).equals("D+")) {
					gradeAtValue = 1.3;
				}
				else if(model.getValueAt(i, 2).equals("D")) {
					gradeAtValue = 1.0;
				}
				else if(model.getValueAt(i, 2).equals("F")) {
					gradeAtValue = 0;
				}
				else {
					try {
						gradeAtValue = Double.parseDouble((String)model.getValueAt(i, 2));
					}
					catch(NumberFormatException e) {
						System.out.println("Invalid Entry");
						gradeAtValue = 0;
					}
				}
				//Calculate value of the sum of all (grade * credits) for every row in table
				numCurrentGpa += gradeAtValue * Double.parseDouble((String)model.getValueAt(i, 1));
			}
		}
		
		//Calculate current gpa and required gpa
		numCurrentGpa = numCurrentGpa / currentCredits;
		numRequiredGpa = ((numTargetGpa * totalCredits) - (numCurrentGpa * currentCredits)) / untakenCredits;
		
		//Formatting the numbers to 3 decimal places max
		numCurrentGpa = Double.parseDouble(fmt.format(numCurrentGpa));
		if(numRequiredGpa > 0 && numRequiredGpa < 4.001) {
			numRequiredGpa = Double.parseDouble(fmt.format(numRequiredGpa));
		}
		
		//Print all calculations to console
		System.out.println("Total Credits: " + totalCredits);	
		System.out.println("Untaken Credits: " + untakenCredits);
		System.out.println("Current Credits: " + currentCredits);
		System.out.println("Current GPA: " + numCurrentGpa);
		System.out.println("Target GPA: " + numTargetGpa);
		System.out.println("Required GPA: " + numRequiredGpa);
		System.out.println();
	}
	/**This method updates the labels on the GUI**/
	public static void updateLabels() {
		//Update required GPA label and summary based on value of required GPA.
		if(numTargetGpa == 0) {
			requiredGpa.setText("Required GPA: Need Target GPA");
		}
		else if(untakenCredits == 0) {
			requiredGpa.setText("Required GPA: Need anticipated credits");
		}
		else if(Double.isNaN(numRequiredGpa)) {
			requiredGpa.setText("Required GPA: Need Current GPA");
		}
		else if(numRequiredGpa > 4.0 || numRequiredGpa == Double.POSITIVE_INFINITY) {
			requiredGpa.setText("Required GPA greater than 4.0. Add more credits");
		}
		else if(numRequiredGpa < 2.0 && numRequiredGpa >= 0) {
			requiredGpa.setText("Required GPA is less than 2.0. Consider fewer credits");

		}
		else if(numRequiredGpa < 0 || numTargetGpa < numCurrentGpa || numRequiredGpa == Double.NEGATIVE_INFINITY) {
			requiredGpa.setText("Current GPA is higher than target. Feel free to take fewer credits");
		}
		else {
			requiredGpa.setText("Required GPA: " + numRequiredGpa);
		}
		
		//Prints out labels if there are actual numbers
		totalCreditsLbl.setText("Total Credits: " + totalCredits);
		if(!Double.isNaN(currentCredits)) {
				currentCreditsLbl.setText("Current Credits: " + currentCredits);
		}
		untakenCreditsLbl.setText("Untaken Credits: " + untakenCredits);
	}
	
	/**Main method**/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GpaGui gpa = new GpaGui();
				System.out.println("Application Running.");
			}
		});
		System.out.println("Exiting main method.");
	}
}
