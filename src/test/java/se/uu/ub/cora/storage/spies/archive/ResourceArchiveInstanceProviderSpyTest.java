/*
 * Copyright 2022, 2024 Uppsala University Library
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

public class ResourceArchiveInstanceProviderSpyTest {
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;
	private ResourceArchiveInstanceProviderSpy resourceArchiveProvider;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		resourceArchiveProvider = new ResourceArchiveInstanceProviderSpy();
	}

	@Test
	public void testMakeSureSpyHelpersAreSetUp() throws Exception {
		assertTrue(resourceArchiveProvider.MCR instanceof MethodCallRecorder);
		assertTrue(resourceArchiveProvider.MRV instanceof MethodReturnValues);
		assertSame(resourceArchiveProvider.MCR.onlyForTestGetMRV(), resourceArchiveProvider.MRV);
	}

	@Test
	public void testDefaultGetOrderToSelectImplementionsBy() throws Exception {
		int order = resourceArchiveProvider.getOrderToSelectImplementionsBy();
		assertEquals(order, 0);
	}

	@Test
	public void testGetOrderToSelectImplementionsBy() throws Exception {
		resourceArchiveProvider.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				(Supplier<Integer>) () -> 99);

		var returnedValue = resourceArchiveProvider.getOrderToSelectImplementionsBy();

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, returnedValue);
	}

	@Test
	public void testDefaultGetRecordStorage() throws Exception {
		assertTrue(resourceArchiveProvider.getResourceArchive() instanceof ResourceArchiveSpy);
	}

	@Test
	public void testGetRecordStorage() throws Exception {
		resourceArchiveProvider.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				ResourceArchiveSpy::new);

		var returnedValue = resourceArchiveProvider.getResourceArchive();

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, returnedValue);
	}

}
