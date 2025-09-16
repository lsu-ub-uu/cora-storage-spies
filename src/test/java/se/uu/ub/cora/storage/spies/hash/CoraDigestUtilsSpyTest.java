/*
 * Copyright 2025 Uppsala University Library
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
package se.uu.ub.cora.storage.spies.hash;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.storage.hash.CoraDigestUtils;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class CoraDigestUtilsSpyTest {

	private static final String SOME_VALUE_TO_HASH = "someValueToHash";
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private CoraDigestUtilsSpy digestor;
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		digestor = new CoraDigestUtilsSpy();
	}

	@Test
	public void testImplements() {
		assertTrue(digestor instanceof CoraDigestUtils);
	}

	@Test
	public void testDefaultBuildPathToAFileAndEnsureFolderExists() {
		String hashedValue = digestor.sha256Hex(SOME_VALUE_TO_HASH);

		assertEquals(hashedValue, "someHahedValue");
	}

	@Test
	public void testBuildPathToAFileAndEnsureFolderExists() {
		digestor.MCR = MCRSpy;
		String expectedHashedValue = "someOtherHashedValue";
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				() -> expectedHashedValue);

		String hashedValue = digestor.sha256Hex(SOME_VALUE_TO_HASH);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		assertEquals(hashedValue, expectedHashedValue);

	}

}
