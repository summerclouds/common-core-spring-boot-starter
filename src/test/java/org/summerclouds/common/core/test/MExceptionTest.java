package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.summerclouds.common.core.error.ErrorException;
import org.summerclouds.common.core.error.ErrorRuntimeException;
import org.summerclouds.common.core.error.IResult;
import org.summerclouds.common.core.error.NotFoundException;
import org.summerclouds.common.core.error.RC.CAUSE;
import org.summerclouds.common.junit.TestCase;

public class MExceptionTest extends TestCase {

	@Test
	public void testErrorException() {
		try {
			throw new ErrorException("test");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\"]", e.getMessage());
		}
		try {
			throw new ErrorException("test","nr1");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\"]", e.getMessage());
		}
		try {
			throw new ErrorException("test","special \"\\");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"special \\\"\\\\\"]", e.getMessage());
		}
		try {
			throw new ErrorException("test", "nr1", "nr2", "nr3");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\",\"nr2\",\"nr3\"]", e.getMessage());
		}
		try {
			throw new ErrorException("test", "nr1", new Object[] {"nr2", "nr3"});
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\",\"[nr2, nr3]\"]", e.getMessage());
		}
		try {
			throw new ErrorException("test", "nr1", new int[] {2, 3});
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\",\"[2, 3]\"]", e.getMessage());
		}
		try {
			throw new ErrorException((IResult)new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(404, e.getReturnCode());
			assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
		}
		try {
			throw new ErrorException("error1", new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"error1\"]", e.getMessage());
		}
		try {
			throw new ErrorException(CAUSE.ADAPT, "error1",new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(404, e.getReturnCode());
			assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
		}
		try {
			throw new ErrorException(CAUSE.APPEND, "error1", new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorException);
			ErrorException e = (ErrorException)t;
			assertEquals(404, e.getReturnCode()); // APPEND will ADAPT the Error Code (!)
			assertEquals("[400,\"error1\",[404,\"test\",\"nr1\"]]", e.getMessage());
		}
	}
	
	@Test
	public void testErrorRuntimeException() {
		try {
			throw new ErrorRuntimeException("test");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException("test","nr1");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException("test","special \"\\");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"special \\\"\\\\\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException("test", "nr1", "nr2", "nr3");
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\",\"nr2\",\"nr3\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException("test", "nr1", new Object[] {"nr2", "nr3"});
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\",\"[nr2, nr3]\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException("test", "nr1", new int[] {2, 3});
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"test\",\"nr1\",\"[2, 3]\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException((IResult)new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(404, e.getReturnCode());
			assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException("error1", new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(400, e.getReturnCode());
			assertEquals("[400,\"error1\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException(CAUSE.ADAPT, "error1",new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(404, e.getReturnCode());
			assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
		}
		try {
			throw new ErrorRuntimeException(CAUSE.APPEND, "error1", new NotFoundException("test", "nr1"));
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(t instanceof ErrorRuntimeException);
			ErrorRuntimeException e = (ErrorRuntimeException)t;
			assertEquals(404, e.getReturnCode()); // APPEND will ADAPT the Error Code (!)
			assertEquals("[400,\"error1\",[404,\"test\",\"nr1\"]]", e.getMessage());
		}
	}	
}
