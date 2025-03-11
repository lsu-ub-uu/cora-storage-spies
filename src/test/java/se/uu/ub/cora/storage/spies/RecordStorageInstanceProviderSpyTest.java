/*
 * Copyright 2022, 2025 Uppsala University Library
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
package se.uu.ub.cora.storage.spies;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.function.Supplier;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class RecordStorageInstanceProviderSpyTest {

	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private static final String ADD_CALL = "addCall";
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;
	private RecordStorageInstanceProviderSpy recordStorage;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		recordStorage = new RecordStorageInstanceProviderSpy();
	}

	@Test
	public void testMakeSureSpyHelpersAreSetUp() {
		assertTrue(recordStorage.MCR instanceof MethodCallRecorder);
		assertTrue(recordStorage.MRV instanceof MethodReturnValues);
		assertSame(recordStorage.MCR.onlyForTestGetMRV(), recordStorage.MRV);
	}

	@Test
	public void testDefaultGetOrderToSelectImplementionsBy() {
		int order = recordStorage.getOrderToSelectImplementionsBy();
		assertEquals(order, 0);
	}

	@Test
	public void testGetOrderToSelectImplementionsBy() {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				(Supplier<Integer>) () -> 99);

		var returnedValue = recordStorage.getOrderToSelectImplementionsBy();

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, returnedValue);
	}

	@Test
	public void testDefaultGetRecordStorage() {
		assertTrue(recordStorage.getRecordStorage() instanceof RecordStorage);
	}

	@Test
	public void testGetRecordStorage() {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				RecordStorageSpy::new);

		var returnedValue = recordStorage.getRecordStorage();

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, returnedValue);
	}

	@Test
	public void testDataChanged() {
		recordStorage.MCR = MCRSpy;

		recordStorage.dataChanged("someType", "someId", "someAction");

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", "someType");
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", "someId");
		mcrForSpy.assertParameter(ADD_CALL, 0, "action", "someAction");
	}
}