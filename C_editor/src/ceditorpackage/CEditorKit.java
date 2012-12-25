package ceditorpackage;



import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledEditorKit;

public class CEditorKit extends StyledEditorKit {
	
	//private static final long serialVersionUID = 2969169649596107757L;
    private CViewFactory cViewFactory;
 
    public CEditorKit() {
        cViewFactory = new CViewFactory();
        
	    	//new StyledEditorKit.BoldAction();
    }
     
    @Override
    public CViewFactory getViewFactory() {
        return cViewFactory;
    }
 
 

}
