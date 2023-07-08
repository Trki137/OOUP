package texteditor.util;

import hr.fer.zemris.ooup.texteditor.model.Location;
import hr.fer.zemris.ooup.texteditor.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilTest {
    private final String testCase= "Dean rj. OOUP";
    @Test
    public void testReturnEmptyString(){
        Location location = new Location(-1,0);
        String result = Util.getSubstringForCursor(location,testCase);
        assertEquals("",result);
    }

    @Test
    public void testReturnWholeString(){
        Location location = new Location(testCase.length(),0);
        String result = Util.getSubstringForCursor(location,testCase);
        assertEquals(testCase,result);
    }

    @Test
    public void testReturnString(){
        Location location = new Location(2,0);
        String result = Util.getSubstringForCursor(location,testCase);
        assertEquals("Dea",result);
    }

    @Test
    public void testDeleteFirst(){
        String result = Util.removeOneCharacter(testCase,0);
        assertEquals("ean rj. OOUP",result);
    }

    @Test
    public void testDelete(){
        String result = Util.removeOneCharacter(testCase,3);
        assertEquals("Dea rj. OOUP",result);
    }

    @Test
    public void testAllCases(){
        String[] expectedValues = new String[]{"D","De", "Dea", "Dean", "Dean ","Dean r","Dean rj", "Dean rj.", "Dean rj. ","Dean rj. O", "Dean rj. OO","Dean rj. OOU", "Dean rj. OOUP"};
        for(int i = 0; i < testCase.length() - 1; i++){
            String result = Util.getSubstringForCursor(new Location(i,0),testCase);
            assertEquals(expectedValues[i],result);
        }
    }

    @Test
    public void testHighlighted(){
        Location leftStart = new Location(-1,0);
        Location rightEnd = new Location(3,0);

        String result = Util.getHighlightedText(leftStart,rightEnd,testCase);
        assertEquals("Dean", result);

        rightEnd.setCoordinateX(testCase.length() - 1);
        result = Util.getHighlightedText(leftStart,rightEnd,testCase);
        assertEquals(testCase, result);

        leftStart.setCoordinateX(2);
        result = Util.getHighlightedText(leftStart,rightEnd,testCase);
        assertEquals("n rj. OOUP", result);

        leftStart.setCoordinateX(11);
        result = Util.getHighlightedText(leftStart,rightEnd,testCase);
        assertEquals("P", result);
    }
}
