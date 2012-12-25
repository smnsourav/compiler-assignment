package ceditorpackage;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.StyledEditorKit;
;

public class CTextPane extends JTextPane {
	
	   
	    public CTextPane() {
	         
	        // Set editor kit
	    	
	        this.setEditorKit( new CEditorKit());
	        
	    }

	    
}
