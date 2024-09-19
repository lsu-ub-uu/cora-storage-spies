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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecordGroup;
import se.uu.ub.cora.data.collected.Link;
import se.uu.ub.cora.data.collected.StorageTerm;
import se.uu.ub.cora.data.spies.DataGroupSpy;
import se.uu.ub.cora.data.spies.DataRecordGroupSpy;
import se.uu.ub.cora.storage.Filter;
import se.uu.ub.cora.storage.StorageReadResult;
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
	public void testReadOld() throws Exception {
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
	public void testDefaultRead() throws Exception {
		assertTrue(recordStorage.read("type", "id") instanceof DataRecordGroupSpy);
	}

	@Test
	public void testRead() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				DataRecordGroupSpy::new);

		DataRecordGroup retunedValue = recordStorage.read("type", "id");

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "type", "type");
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", "id");
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

	@Test
	public void testCreate() throws Exception {
		recordStorage.MCR = MCRSpy;
		DataGroup data = new DataGroupSpy();
		Set<StorageTerm> storageTerms = new LinkedHashSet<>();
		Set<Link> links = new LinkedHashSet<>();

		recordStorage.create("someType", "someId", data, storageTerms, links, "someDataDivider");

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", "someType");
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", "someId");
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataRecord", data);
		mcrForSpy.assertParameter(ADD_CALL, 0, "storageTerms", storageTerms);
		mcrForSpy.assertParameter(ADD_CALL, 0, "links", links);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", "someDataDivider");
	}

	@Test
	public void testDeleteByTypeAndId() throws Exception {
		recordStorage.MCR = MCRSpy;

		recordStorage.deleteByTypeAndId("someType", "someId");

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", "someType");
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", "someId");
	}

	@Test
	public void testDefaultLinksExistForRecord() throws Exception {
		assertFalse(recordStorage.linksExistForRecord("someType", "someId"));
	}

	@Test
	public void testLinksExistForRecord() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				(Supplier<Boolean>) () -> true);

		boolean retunedValue = recordStorage.linksExistForRecord("someType", "someId");

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "type", "someType");
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", "someId");
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

	@Test
	public void testUpdate() throws Exception {
		recordStorage.MCR = MCRSpy;
		DataGroup data = new DataGroupSpy();
		Set<StorageTerm> storageTerms = new LinkedHashSet<>();
		Set<Link> links = new LinkedHashSet<>();

		recordStorage.update("someType", "someId", data, storageTerms, links, "someDataDivider");

		mcrForSpy.assertMethodWasCalled(ADD_CALL);
		mcrForSpy.assertParameter(ADD_CALL, 0, "type", "someType");
		mcrForSpy.assertParameter(ADD_CALL, 0, "id", "someId");
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataRecord", data);
		mcrForSpy.assertParameter(ADD_CALL, 0, "storageTerms", storageTerms);
		mcrForSpy.assertParameter(ADD_CALL, 0, "links", links);
		mcrForSpy.assertParameter(ADD_CALL, 0, "dataDivider", "someDataDivider");
	}

	@Test
	public void testDefaultReadList() throws Exception {
		assertTrue(recordStorage.readList("types", new Filter()) instanceof StorageReadResult);
	}

	@Test
	public void testReadList() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				StorageReadResult::new);

		String type = "someType";
		Filter filter = new Filter();
		StorageReadResult retunedValue = recordStorage.readList(type, filter);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "type", type);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "filter", filter);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

	@Test
	public void testDefaultReadListOld() throws Exception {
		assertTrue(recordStorage.readList(List.of("types"),
				new Filter()) instanceof StorageReadResult);
	}

	@Test
	public void testReadListOld() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				StorageReadResult::new);

		List<String> types = List.of("someType");
		Filter filter = new Filter();
		StorageReadResult retunedValue = recordStorage.readList(types, filter);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "types", types);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "filter", filter);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

	@Test
	public void testDefaultRecordExists() throws Exception {
		List<String> types = List.of("someType");

		assertFalse(recordStorage.recordExists(types, "someId"));
	}

	@Test
	public void testRecordExist() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				(Supplier<Boolean>) () -> true);
		List<String> types = List.of("someType");

		boolean retunedValue = recordStorage.recordExists(types, "someId");

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "types", types);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", "someId");
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

	@Test
	public void testDefaultGetLinksToRecord() throws Exception {
		assertEquals(recordStorage.getLinksToRecord("someType", "someId"), Collections.emptyList());
	}

	@Test
	public void testGetLinksToRecord() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				(Supplier<Set<Link>>) () -> new LinkedHashSet<>());

		Collection<Link> retunedValue = recordStorage.getLinksToRecord("someType", "someId");

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "type", "someType");
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "id", "someId");
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}

	@Test
	public void testDefaultGetTotalNumberOfRecordsForTypes() throws Exception {
		assertEquals(recordStorage.getTotalNumberOfRecordsForTypes(List.of("types"), new Filter()),
				0);
	}

	@Test
	public void testGetTotalNumberOfRecordsForTypes() throws Exception {
		recordStorage.MCR = MCRSpy;
		MCRSpy.MRV.setDefaultReturnValuesSupplier(ADD_CALL_AND_RETURN_FROM_MRV,
				(Supplier<Long>) () -> 321L);

		List<String> types = List.of("someType");
		Filter filter = new Filter();
		long retunedValue = recordStorage.getTotalNumberOfRecordsForTypes(types, filter);

		mcrForSpy.assertMethodWasCalled(ADD_CALL_AND_RETURN_FROM_MRV);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "types", types);
		mcrForSpy.assertParameter(ADD_CALL_AND_RETURN_FROM_MRV, 0, "filter", filter);
		mcrForSpy.assertReturn(ADD_CALL_AND_RETURN_FROM_MRV, 0, retunedValue);
	}
}
