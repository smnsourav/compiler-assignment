package ceditorpackage;

import java.awt.*;

import java.awt.event.*;

import java.io.*;

import javax.swing.*;

import javax.swing.text.*;

//import m.KeyBindingExample.EnterAction;




public class TextEditor extends JFrame{

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new TextEditor();
	}
	
	//private StyledDocument style = new DefaultStyledDocument();
	
	private static CTextPane area = new CTextPane();
	
	private static Action enterAction = new EnterAction();
	private static Action parenthesisAction = new ParenthesisAction();
	private static Action sqBracAction = new SqBracAction();
	private static Action quotesAction = new QuotesAction();
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));

	private String currentFile = "Untitled";

	private boolean changed = false;
	
    static class EnterAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            
            area.replaceSelection("}"); 
            area.setCaretPosition(area.getCaretPosition()-1);
            
             
        } // end method actionPerformed()
         
    } // end class EnterAction
    
    static class ParenthesisAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            
            area.replaceSelection(")"); 
            area.setCaretPosition(area.getCaretPosition()-1);
            
             
        } // end method actionPerformed()
         
    } // end class EnterAction
    static class SqBracAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            
            area.replaceSelection("]"); 
            area.setCaretPosition(area.getCaretPosition()-1);
            
             
        } // end method actionPerformed()
         
    } // end class EnterAction
    static class QuotesAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            
            area.replaceSelection("\""); 
            area.setCaretPosition(area.getCaretPosition()-1);
            
             
        } // end method actionPerformed()
         
    } // end class EnterAction
    public TextEditor() {
    	
		area.setEditable(true);
		
		Font font = new Font("Consolas", Font.BOLD, 12);
        area.setFont(font);
        
        
        area.getInputMap().put( KeyStroke.getKeyStroke(91, InputEvent.SHIFT_MASK),
        "doEnterAction" );
        area.getActionMap().put( "doEnterAction", enterAction );

        area.getInputMap().put( KeyStroke.getKeyStroke(91, 0),
        "doSquareBracketAction" );
        area.getActionMap().put( "doSquareBracketAction", sqBracAction );
        
        area.getInputMap().put( KeyStroke.getKeyStroke(57, InputEvent.SHIFT_MASK),
        "doParenthesisAction" );
        area.getActionMap().put( "doParenthesisAction", parenthesisAction );
      
        area.getInputMap().put( KeyStroke.getKeyStroke(222, InputEvent.SHIFT_MASK),
        "doQuotesAction" );
        area.getActionMap().put( "doQuotesAction", quotesAction );
      
    
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(600, 400));
        scroll.setMinimumSize(new Dimension(100, 100));
		add(scroll,BorderLayout.CENTER);

		
// menu bar
		JMenuBar JMB = new JMenuBar();

		setJMenuBar(JMB);

		JMenu file = new JMenu("File");

		JMenu edit = new JMenu("Edit");

		JMB.add(file); JMB.add(edit);
		
		file.add(New);file.add(Open);file.add(Save);

		file.add(Quit);file.add(SaveAs);

		file.addSeparator();

		

		for(int i=0; i<4; i++)

			file.getItem(i).setIcon(null);

		

		edit.add(Cut);edit.add(Copy);edit.add(Paste);


		edit.getItem(0).setText("Cut out");

		edit.getItem(1).setText("Copy");

		edit.getItem(2).setText("Paste");

//tool bar
		JToolBar tool = new JToolBar();

		add(tool,BorderLayout.NORTH);

		JButton n = tool.add(New), o = tool.add(Open), s = tool.add(Save);
		n.setText("new");
		o.setText("open");
		s.setText("save");

		tool.addSeparator();

		

		JButton cut = tool.add(Cut), cop = tool.add(Copy),pas = tool.add(Paste);

		

		cut.setText("cut"); //cut.setIcon(new ImageIcon("cut.gif"));

		cop.setText("copy"); //cop.setIcon(new ImageIcon("copy.gif"));

		pas.setText("paste"); //pas.setIcon(new ImageIcon("paste.gif"));

		

		Save.setEnabled(false);

		SaveAs.setEnabled(false);

		

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();

		area.addKeyListener(k1);

		setTitle(currentFile);

		setVisible(true);

	}

	
	

	private KeyListener k1 = new KeyAdapter() {

		public void keyPressed(KeyEvent e) {
			//trace("DOWN -> Code: " + e.getCode() + "\tACSII: " + e.getAscii() + "\tKey: " + chr(Key.getAscii()));
			changed = true;

			Save.setEnabled(true);

			SaveAs.setEnabled(true);

		}

	};


	Action Open = new AbstractAction("Open", new ImageIcon("open.gif")) {

		public void actionPerformed(ActionEvent e) {

			saveOld();

			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {

				readInFile(dialog.getSelectedFile().getAbsolutePath());

			}

			SaveAs.setEnabled(true);

		}

	};
	
	

	Action New = new AbstractAction("New", new ImageIcon("open.gif")) {

		public void actionPerformed(ActionEvent e) {

			saveOld();

			

			SaveAs.setEnabled(false);

		
		
		area.setText("");

		dialog = new JFileChooser(System.getProperty("user.dir"));

		currentFile = "Untitled";

		 changed = false;
		}
	};

	Action Save = new AbstractAction("Save", new ImageIcon("save.gif")) {

		public void actionPerformed(ActionEvent e) {

			if(!currentFile.equals("Untitled"))

				saveFile(currentFile);

			else

				saveFileAs();

		}

	};


	Action SaveAs = new AbstractAction("Save as...") {

		public void actionPerformed(ActionEvent e) {

			saveFileAs();

		}

	};


	Action Quit = new AbstractAction("Quit") {

		public void actionPerformed(ActionEvent e) {

			saveOld();

			System.exit(0);

		}

	};


	ActionMap m = area.getActionMap();

	Action Cut = m.get(DefaultEditorKit.cutAction);

	Action Copy = m.get(DefaultEditorKit.copyAction);

	Action Paste = m.get(DefaultEditorKit.pasteAction);


	private void saveFileAs() {

		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)

			saveFile(dialog.getSelectedFile().getAbsolutePath());

	}
	
	private void saveOld() {
		if(changed) {
			if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
			{
				if(!currentFile.equals("Untitled"))

					saveFile(currentFile);

				else

					saveFileAs();

			}
		}

	}


	private void readInFile(String fileName) {

		try {

			FileReader r = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(r);
			area.read(r,null);
reader.close();
			r.close();

			currentFile = fileName;

			setTitle(currentFile);

			changed = false;

		}

		catch(IOException e) {

			Toolkit.getDefaultToolkit().beep();

			JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);

		}

	}


	private void saveFile(String fileName) {

		try {

			FileWriter w = new FileWriter(fileName);

			area.write(w);

			w.close();

			currentFile = fileName;

			setTitle(currentFile);

			changed = false;

			Save.setEnabled(false);

		}

		catch(IOException e) {

		}

	}

}
