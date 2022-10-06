/*
 * Copyright 2022 Uppsala University Library
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
package se.uu.ub.cora.testspies.storage;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.storage.StorageReadResult;
import se.uu.ub.cora.testspies.data.DataGroupSpy;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class RecordStorageSpyTest {

	private static final String ADD_CALL = "addCall";
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;
	private RecordStorageSpy recordStorage;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		recordStorage = new RecordStorageSpy();
	}

	@Test
	public void testMakeSureSpyHelpersAreSetUp() throws Exception {
		assertTrue(recordStorage.MCR instanceof MethodCallRecorder);
		assertTrue(recordStorage.MRV instanceof MethodReturnValues);
		assertSame(recordStorage.MCR.onlyForTestGetMRV(), recordStorage.MRV);
	}

	@Test
	public void testDefaultRead() throws Exception {
		assertTrue(recordStorage.read(List.of("types"), "id") instanceof DataGroupSpy);
	}

	@Test
	public void testReadRecord() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV, DataGroupSpy::new);

		List<String> types = List.of("someType");
		DataGroup retunedValue = recordStorage.read(types, "id");

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "types", types);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", "id");
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

	@Test
	public void testDefaultReadList() throws Exception {
		assertTrue(recordStorage.readList(List.of("types"),
				new DataGroupSpy()) instanceof StorageReadResult);
	}

	@Test
	public void testReadRecordList() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				StorageReadResult::new);

		List<String> types = List.of("someType");
		DataGroupSpy filter = new DataGroupSpy();
		StorageReadResult retunedValue = recordStorage.readList(types, filter);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "types", types);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "filter", filter);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

}
