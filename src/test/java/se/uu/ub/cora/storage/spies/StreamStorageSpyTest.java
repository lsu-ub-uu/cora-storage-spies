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
package se.uu.ub.cora.storage.spies;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.storage.spies.archive.InputStreamSpy;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class StreamStorageSpyTest {

	private static final String STREAM_ID = "someStreamId";
	private static final String DATA_DIVIDER = "someDataDivider";
	private static final String ADD_CALL = "addCall";
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;
	private StreamStorageSpy streamStorage;
	private InputStream stream;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		streamStorage = new StreamStorageSpy();
		stream = new InputStreamSpy();
	}

	@Test
	public void testMakeSureSpyHelpersAreSetUp() throws Exception {
		assertTrue(streamStorage.MCR instanceof MethodCallRecorder);
		assertTrue(streamStorage.MRV instanceof MethodReturnValues);
		assertSame(streamStorage.MCR.onlyForTestGetMRV(), streamStorage.MRV);
	}

	@Test
	public void testStoreDefault() throws Exception {
		assertEquals(streamStorage.store(STREAM_ID, DATA_DIVIDER, stream), 100L);
	}

	@Test
	public void testStore() throws Exception {
		streamStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV, () -> 1L);

		streamStorage.store(STREAM_ID, DATA_DIVIDER, stream);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "streamId", STREAM_ID);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "dataDivider", DATA_DIVIDER);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "stream", stream);
	}

	@Test
	public void testRetrieveDefault() throws Exception {
		assertTrue(streamStorage.retrieve(STREAM_ID, DATA_DIVIDER) instanceof InputStream);
	}

	@Test
	public void testRetrieve() throws Exception {
		streamStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				InputStreamSpy::new);

		streamStorage.retrieve(STREAM_ID, DATA_DIVIDER);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "streamId", STREAM_ID);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "dataDivider", DATA_DIVIDER);
	}

	@Test
	public void testDelete() throws Exception {
		streamStorage.MCR = MCRSpy;

		streamStorage.delete(STREAM_ID, DATA_DIVIDER);

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "streamId", STREAM_ID);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", DATA_DIVIDER);
	}

}
