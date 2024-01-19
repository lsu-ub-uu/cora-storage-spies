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
package se.uu.ub.cora.storage.spies.path;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class StreamPathBuilderSpyTest {

	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";
	private static final String SOME_DATA_DVIDER = "someDataDvider";
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private StreamPathBuilderSpy streamPathBuilderSpy;
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		streamPathBuilderSpy = new StreamPathBuilderSpy();
	}

	@Test
	public void testDefaultBuildPathToAFileAndEnsureFolderExists() throws Exception {
		String path = streamPathBuilderSpy.buildPathToAFileAndEnsureFolderExists(SOME_DATA_DVIDER,
				SOME_TYPE, SOME_ID);

		assertEquals(path, "somePathToAFile");
	}

	@Test
	public void testBuildPathToAFileAndEnsureFolderExists() throws Exception {
		streamPathBuilderSpy.MCR = MCRSpy;
		String expectedPath = "someOtherPath";
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV, () -> expectedPath);

		String path = streamPathBuilderSpy.buildPathToAFileAndEnsureFolderExists(SOME_DATA_DVIDER,
				SOME_TYPE, SOME_ID);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", SOME_ID);
		assertEquals(path, expectedPath);

	}

}
