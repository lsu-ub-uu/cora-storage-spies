/*
 * Copyright 2024 Uppsala University Library
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

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.spies.DataGroupSpy;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class RecordArchiveSpyTest {

	private static final String SOME_MIME_TYPE = "someMimeType";
	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";
	private static final String SOME_DATA_DVIDER = "someDataDvider";
	private static final String ADD_CALL = "addCall";
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;
	private RecordArchiveSpy recordArchiveSpy;
	private DataGroup someDataRecord;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		recordArchiveSpy = new RecordArchiveSpy();

		someDataRecord = new DataGroupSpy();
	}

	@Test
	public void testMakeSureSpyHelpersAreSetUp() throws Exception {
		assertTrue(recordArchiveSpy.MCR instanceof MethodCallRecorder);
		assertTrue(recordArchiveSpy.MRV instanceof MethodReturnValues);
		assertSame(recordArchiveSpy.MCR.onlyForTestGetMRV(), recordArchiveSpy.MRV);
	}

	@Test
	public void create() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();

		recordArchiveSpy.create(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID, someDataRecord);

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", SOME_ID);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataRecord", someDataRecord);
	}

	@Test
	public void update() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();

		recordArchiveSpy.update(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID, someDataRecord);

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", SOME_ID);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataRecord", someDataRecord);
	}

	@Test
	public void delete() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();

		recordArchiveSpy.delete(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID);

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", SOME_ID);
	}

	private void replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR() {
		recordArchiveSpy.MCR = MCRSpy;
	}

}
