/*
 * Copyright 2022, 2023 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.storage.spies.archive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.function.Supplier;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class InputStreamSpyTest {

	private static final String ADD_CALL = "addCall";
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private InputStreamSpy inputStream;
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrFromMCRSpy;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrFromMCRSpy = MCRSpy.MCR;
		inputStream = new InputStreamSpy();
	}

	private void replaceDefaultMCRWithMCRSpyToEnableTestOfHowTheSpyUsesMCR() {
		inputStream.MCR = MCRSpy;
	}

	@Test
	public void testMakeSureSpyHelpersAreSetUp() throws Exception {
		assertTrue(inputStream.MCR instanceof MethodCallRecorder);
		assertTrue(inputStream.MRV instanceof MethodReturnValues);
		assertSame(inputStream.MCR.onlyForTestGetMRV(), inputStream.MRV);
	}

	@Test
	public void testDefaultGetResponseBinary() throws Exception {
		assertEquals(inputStream.read(), 0);
	}

	@Test
	public void testGetResponseBinary() throws Exception {
		replaceDefaultMCRWithMCRSpyToEnableTestOfHowTheSpyUsesMCR();
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				(Supplier<Integer>) () -> 900);

		var returnedValue = inputStream.read();

		mcrFromMCRSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, returnedValue);
	}

	@Test
	public void testClose() throws Exception {
		replaceDefaultMCRWithMCRSpyToEnableTestOfHowTheSpyUsesMCR();

		inputStream.close();

		mcrFromMCRSpy.assertMethodWasCalled(ADD_CALL);
	}
}
