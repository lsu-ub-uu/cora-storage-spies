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

import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.collected.Link;
import se.uu.ub.cora.data.collected.StorageTerm;
import se.uu.ub.cora.data.spies.DataGroupSpy;
import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.storage.StorageReadResult;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class RecordStorageSpy implements RecordStorage {
	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public RecordStorageSpy() {
		MCR.useMRV(MRV);
		MRV.setDefaultReturnValuesSupplier("read", DataGroupSpy::new);
		MRV.setDefaultReturnValuesSupplier("readList", StorageReadResult::new);
	}

	@Override
	public DataGroup read(List<String> types, String id) {
		return (DataGroup) MCR.addCallAndReturnFromMRV("types", types, "id", id);
	}

	@Override
	public void create(String type, String id, DataGroup dataRecord, List<StorageTerm> storageTerms,
			List<Link> links, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup dataRecord, List<StorageTerm> storageTerms,
			List<Link> links, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public StorageReadResult readList(List<String> types, DataGroup filter) {
		return (StorageReadResult) MCR.addCallAndReturnFromMRV("types", types, "filter", filter);
	}

	@Override
	public boolean recordExistsForListOfImplementingRecordTypesAndRecordId(List<String> types,
			String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTotalNumberOfRecordsForTypes(List<String> types, DataGroup filter) {
		// TODO Auto-generated method stub
		return 0;
	}

}
