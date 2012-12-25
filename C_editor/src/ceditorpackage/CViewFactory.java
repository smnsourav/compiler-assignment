
package ceditorpackage;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class CViewFactory implements ViewFactory {

	@Override
	public View create(Element element) {
		// TODO Auto-generated method stub
		
		return new CView(element);
	}

}
