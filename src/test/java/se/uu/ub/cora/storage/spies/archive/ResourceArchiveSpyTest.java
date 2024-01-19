/*
 * Copyright 2023 Uppsala University Library
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

import java.io.InputStream;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.storage.archive.ResourceMetadata;
import se.uu.ub.cora.storage.archive.record.ResourceMetadataToUpdate;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;
import se.uu.ub.cora.testutils.spies.MCRSpy;

public class ResourceArchiveSpyTest {

	private static final String SOME_MIME_TYPE = "someMimeType";
	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";
	private static final String SOME_DATA_DVIDER = "someDataDvider";
	private static final String ADD_CALL = "addCall";
	private static final String ADD_CALL_AND_RETURN_FROM_MRV = "addCallAndReturnFromMRV";
	private MCRSpy MCRSpy;
	private MethodCallRecorder mcrForSpy;
	private ResourceArchiveSpy resourceArchiveSpy;
	private InputStreamSpy resource;

	@BeforeMethod
	public void beforeMethod() {
		MCRSpy = new MCRSpy();
		mcrForSpy = MCRSpy.MCR;
		resourceArchiveSpy = new ResourceArchiveSpy();
	}

	@Test
	public void testMakeSureSpyHelpersAreSetUp() throws Exception {
		resource = new InputStreamSpy();

		assertTrue(resourceArchiveSpy.MCR instanceof MethodCallRecorder);
		assertTrue(resourceArchiveSpy.MRV instanceof MethodReturnValues);
		assertSame(resourceArchiveSpy.MCR.onlyForTestGetMRV(), resourceArchiveSpy.MRV);
	}

	@Test
	public void testCreate() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();

		resourceArchiveSpy.createMasterResource(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID, resource, SOME_MIME_TYPE);

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", SOME_ID);
		mcrForSpy.assertParameter(ADD_CALL, 0, "resource", resource);
		mcrForSpy.assertParameter(ADD_CALL, 0, "mimeType", SOME_MIME_TYPE);
	}

	private void replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR() {
		resourceArchiveSpy.MCR = MCRSpy;
	}

	@Test
	public void testDefaultRead() throws Exception {

		InputStream read = resourceArchiveSpy.readMasterResource(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID);
		assertTrue(read instanceof InputStreamSpy);
	}

	@Test
	public void testRead() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();

		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV, () -> resource);
		InputStream readResource = resourceArchiveSpy.readMasterResource(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID);

		assertEquals(readResource, resource);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", SOME_ID);
	}

	@Test
	public void testDefaultReadMetadata() throws Exception {
		ResourceMetadata resourceMetadata = resourceArchiveSpy.readMasterResourceMetadata(SOME_DATA_DVIDER,
				SOME_TYPE, SOME_ID);

		assertEquals(resourceMetadata.fileSize(), "someFileSize");
		assertEquals(resourceMetadata.checksumSHA512(), "someChecksumSHA512");
	}

	@Test
	public void testReadMetadata() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();
		ResourceMetadata resourceMetadata = new ResourceMetadata("someFileSizeA",
				"someChecksumSHA512A");
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				() -> resourceMetadata);

		ResourceMetadata readMetadata = resourceArchiveSpy.readMasterResourceMetadata(SOME_DATA_DVIDER, SOME_TYPE,
				SOME_ID);

		assertEquals(readMetadata, resourceMetadata);
		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", SOME_ID);

	}

	@Test
	public void testUpdateMetadata() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();
		ResourceMetadataToUpdate resourceMetadataToUpdate = new ResourceMetadataToUpdate(
				"someOriginalFileName", "someMimeType");

		resourceArchiveSpy.updateMasterResourceMetadata(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID,
				resourceMetadataToUpdate);

		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", SOME_ID);
		mcrForSpy.assertParameter(ADD_CALL, 0, "resourceMetadataToUpdate",
				resourceMetadataToUpdate);
	}

	@Test
	public void testUpdate() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();

		resourceArchiveSpy.update(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID, resource, SOME_MIME_TYPE);

		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", SOME_ID);
		mcrForSpy.assertParameter(ADD_CALL, 0, "resource", resource);
		mcrForSpy.assertParameter(ADD_CALL, 0, "mimeType", SOME_MIME_TYPE);
	}

	@Test
	public void testDelete() throws Exception {
		replaceDefaultMCRWithTheOneFromTestToEnableTestOfHowTheSpyUsesMCR();

		resourceArchiveSpy.delete(SOME_DATA_DVIDER, SOME_TYPE, SOME_ID);

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", SOME_DATA_DVIDER);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", SOME_TYPE);
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", SOME_ID);
	}

}
