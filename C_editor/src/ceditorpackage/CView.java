package ceditorpackage;

import javax.swing.text.Element;
import javax.swing.text.PlainView;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;

public class CView extends PlainView {
	 
    private static HashMap<Pattern, Color> patternColors;
    private static String COMMENT ="(//.*)";
    private static String COMMENTL ="(/)(\\*)((\\s)*(.)*)*(\\*)/";
    //	"n";//"/\*(\n)*(.)*(\n)*(.)*(\n)*\*/";
    	//	"(/)\\*(\\n)*(.)*(\\n)*(.)*(\\n)*\\*(/)";
    	//	
    private static String STRING = "(\").*(\")";
    private static String STRINGL = "(</?[A-Za-z\\-\\.]*)\\s?>?";//"<.*>";
    private static String PPD = "(#[^\\<]*)";
    private static String KEY_WORDS = "(\\b)(auto|char|const|double|extern|float|int|long|register|short|signed|static|unsigned|void|volatile|_Packed)(?!\\w)";
    private static String CONTROL_KEY_WORDS = "(\\b)(break|case|continue|default|do|else|enum|for|goto|return|sizeof|struct|switch|typedef|union|while|if)(?!\\w)";

  //  private static String TAG_CDATA_START = "(\\<\\!\\[CDATA\\[)";
  //  private static String TAG_CDATA_END = ".*(]]>)";

  
 //STRING[3]='"';
    static {
        // NOTE: the order is important!
    	   patternColors = new HashMap<Pattern, Color>();
    	//  patternColors.put(Pattern.compile(TAG_CDATA_START), new Color(128, 0, 0));
         //  patternColors.put(Pattern.compile(TAG_CDATA_END), new Color(128, 0, 0));
           
        patternColors.put(Pattern.compile(KEY_WORDS), new Color(1,128,1));
     
        patternColors.put(Pattern.compile(CONTROL_KEY_WORDS), new Color(128, 1, 1));
        patternColors.put(Pattern.compile(COMMENT), new Color(1, 1, 127));
        patternColors.put(Pattern.compile(COMMENTL), new Color(1, 1, 127));
        
        patternColors.put(Pattern.compile(STRING), new Color(
                127, 0, 97));
        patternColors.put(Pattern.compile(STRINGL), new Color(127, 0, 97));
        patternColors.put(Pattern.compile(PPD), new Color(97, 0,
                127));
    }
 
    public CView(Element element) {
 
        super(element);
 
        // Set tabsize to 4 (instead of the default 8)
        getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);
    }
 
    @Override
    protected int drawUnselectedText(Graphics graphics, int x, int y, int p0,
            int p1) throws BadLocationException {
 
        Document doc = getDocument();
        String text = doc.getText(p0, p1 - p0);
 
        Segment segment = getLineBuffer();
 
        SortedMap<Integer, Integer> startMap = new TreeMap<Integer, Integer>();
        SortedMap<Integer, Color> colorMap = new TreeMap<Integer, Color>();
 
        // Match all regexes on this snippet, store positions
        for (Map.Entry<Pattern, Color> entry : patternColors.entrySet()) {
 
            Matcher matcher = entry.getKey().matcher(text);
 
            while (matcher.find()) {
                startMap.put(matcher.start(1), matcher.end());
                colorMap.put(matcher.start(1), entry.getValue());
            }
        }
 
        // TODO: check the map for overlapping parts
         
        int i = 0;
 
        // Colour the parts
        for (Map.Entry<Integer, Integer> entry : startMap.entrySet()) {
            int start = entry.getKey();
            int end = entry.getValue();
 
            if (i < start) {
                graphics.setColor(Color.black);
                doc.getText(p0 + i, start - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
            }
 
            graphics.setColor(colorMap.get(start));
            i = end;
            doc.getText(p0 + start, i - start, segment);
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, start);
        }
 
        // Paint possible remaining text black
        if (i < text.length()) {
            graphics.setColor(Color.black);
            doc.getText(p0 + i, text.length() - i, segment);
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
        }
 
        return x;
    }
 
}