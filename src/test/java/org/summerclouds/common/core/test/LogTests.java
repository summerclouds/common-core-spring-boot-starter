package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.summerclouds.common.core.error.IResult;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.error.RC;
import org.summerclouds.common.core.error.RC.CAUSE;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.junit.TestCase;

@SpringBootTest(classes = TestsApplication.class)
class LogTests extends TestCase  {

	@Test
	void testLogRegistry() {
		Log log1 = Log.getLog(LogTests.class);
		Log log2 = Log.getLog(LogTests.class);
		
		assertEquals(log1, log2);
	}
	
	@Test
	void testLogLevel() {
		Log.getLog(LogTests.class).t("Test trace log");
		Log.getLog(LogTests.class).d("Test debug log");
		Log.getLog(LogTests.class).i("Test info log");
		Log.getLog(LogTests.class).w("Test warn log");
		Log.getLog(LogTests.class).e("Test error log");
		Log.getLog(LogTests.class).f("Test fatal log");
	}

	@Test
	void testLogLevelUpgrade() {
		Log.getLog(LogTests.class).setLocalUpgrade(true);
		assertTrue(Log.getLog(LogTests.class).isLocalUpgrade());
		try {
			Log.getLog(LogTests.class).t("Test trace log");
			Log.getLog(LogTests.class).d("Test debug log");
			Log.getLog(LogTests.class).i("Test info log");
			Log.getLog(LogTests.class).w("Test warn log");
			Log.getLog(LogTests.class).e("Test error log");
			Log.getLog(LogTests.class).f("Test fatal log");
		} finally {
			Log.getLog(LogTests.class).setLocalUpgrade(false);
		}
	}

    @Test
    public void testRCMessage() throws Exception {
        {
            String msg = RC.toMessage(1,(IResult)null, "test", null , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\"]", msg);
        }
        {
            String msg = RC.toMessage(1,(IResult)null, "test", new Object[] {"nr1"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\"]", msg);
        }
        {
            String msg = RC.toMessage(1,(IResult)null, "test", new Object[] {"nr1",null} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",null]", msg);
        }
        {
            String msg = RC.toMessage(1,(IResult)null, "test", new Object[] {"nr1",null, new String[] {"a","b"}, "last" } , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",null,\"[a, b]\",\"last\"]", msg);
        }
        {
            String msg = RC.toMessage(1,CAUSE.APPEND, "test", new Object[] {"nr1", new Exception("exception"), "nr2"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\"]", msg);
        }
        {
            MException cause = new MException(1,"cause");
            String msg = RC.toMessage(1,cause, "test", new Object[] {"nr1", "nr2"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"cause\"]]", msg);
        }
        {
            MException cause = new MException(1,"cause", "c1");
            String msg = RC.toMessage(1,cause, "test", new Object[] {"nr1", "nr2"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"cause\",\"c1\"]]", msg);
        }
    }
    
    @Test
    public void testRCMessageTruncate() throws Exception {
        { // exact at end of second entry
            MException cause = new MException(1, "cause", "c1");
            String msg = RC.toMessage(1, cause, "test", new Object[] {"nr1", "nr2"}, 10);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"...\"]", msg);
        }
        { // end in mittle of attribute
            MException cause = new MException(1, "cause", "c1");
            String msg = RC.toMessage(1, cause, "test", new Object[] {"nr1", "nr2"}, 13);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr...\"]", msg);
        }
        { // end in middle of attribute
            MException cause = new MException(1, "cause", "c1");
            String msg = RC.toMessage(1, cause, "test", new Object[] {"nr1", "nr2"}, 23);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"...cause...\"]]", msg);
        }
        { // end of array
            MException cause = new MException(1, "cause", "c1");
            String msg = RC.toMessage(1, cause, "test", new Object[] {"nr1", "nr2"}, 37);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"...cause...\"]]", msg);
        }
        { // end of array
            MException cause = new MException(1, "cause", "c1");
            String msg = RC.toMessage(1, cause, "test", new Object[] {"nr1", "nr2"}, 38);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"cause\",\"c1\"]]", msg);
        }
        
    	{ // cut at the end
    		String msg = RC.toMessage(1, (IResult)null, "test", new Object[] {"nr1", "nr2"}, 20);
    		System.out.println(msg);
    		assertEquals("[1,\"test\",\"nr1\",\"nr2...\"]", msg);
    	}
    	{ // cut at the end
    		String msg = RC.toMessage(1, (IResult)null, "test", new Object[] {"nr1", "nr2"}, 21);
    		System.out.println(msg);
    		assertEquals("[1,\"test\",\"nr1\",\"nr2\",\"...\"]", msg);
    	}
    	{ // cut at the end
    		String msg = RC.toMessage(1, (IResult)null, "test", new Object[] {"nr1", "nr2\\"}, 21);
    		System.out.println(msg);
    		assertEquals("[1,\"test\",\"nr1\",\"nr2\\\\...\"]", msg);
    	}
    	{ // cut at the end
    		String msg = RC.toMessage(1, (IResult)null, "test", new Object[] {"nr1", "nr2\\"}, 22);
    		System.out.println(msg);
    		assertEquals("[1,\"test\",\"nr1\",\"nr2\\\\...\"]", msg);
    	}
    	{ // cut at the end
    		String msg = RC.toMessage(1, (IResult)null, "test", new Object[] {"nr1", "nr2\""}, 21);
    		System.out.println(msg);
    		assertEquals("[1,\"test\",\"nr1\",\"nr2\\\\...\"]", msg);
    	}
    	{ // cut at the end
    		String msg = RC.toMessage(1, (IResult)null, "test", new Object[] {"nr1", "nr2\""}, 22);
    		System.out.println(msg);
    		assertEquals("[1,\"test\",\"nr1\",\"nr2\\\"...\"]", msg);
    	}
    	{ // cut at the end
    		String msg = RC.toMessage(1, (IResult)null, "test", new Object[] {"nr1", "nr2\""}, 23);
    		System.out.println(msg);
    		assertEquals("[1,\"test\",\"nr1\",\"nr2\\\"\",\"...\"]", msg);
    	}
        { // cut at the end - not cause truncation
            MException cause = new MException(1, "cause", "c1");
            String msg = RC.toMessage(1, cause, "test", new Object[] {"nr1", "nr2"}, 20);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"...cause...\"]]", msg);
        }
        
    }

    
}
