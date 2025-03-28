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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecordGroup;
import se.uu.ub.cora.data.collected.Link;
import se.uu.ub.cora.data.collected.StorageTerm;
import se.uu.ub.cora.data.spies.DataRecordGroupSpy;
import se.uu.ub.cora.storage.Filter;
import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.storage.StorageReadResult;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class RecordStorageSpy implements RecordStorage {
	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public RecordStorageSpy() {
		MCR.useMRV(MRV);

		// TODO: We do not have possibility to overload methods in MRV.
		// Meanwhile no default value will be returned for new read in this spy. Pleas fix.
		MRV.setDefaultReturnValuesSupplier("read", DataRecordGroupSpy::new);
		MRV.setDefaultReturnValuesSupplier("readList", StorageReadResult::new);
		MRV.setDefaultReturnValuesSupplier("linksExistForRecord", (Supplier<Boolean>) () -> false);
		MRV.setDefaultReturnValuesSupplier("recordExists", (Supplier<Boolean>) () -> false);
		MRV.setDefaultReturnValuesSupplier("getLinksToRecord", Collections::emptySet);
		MRV.setDefaultReturnValuesSupplier("getLinksFromRecord", Collections::emptySet);
		MRV.setDefaultReturnValuesSupplier("getStorageTermsForRecord", Collections::emptySet);
		MRV.setDefaultReturnValuesSupplier("getTotalNumberOfRecordsForTypes",
				(Supplier<Long>) () -> 0L);
	}

	@Override
	public void create(String type, String id, DataGroup dataRecord, Set<StorageTerm> storageTerms,
			Set<Link> links, String dataDivider) {
		MCR.addCall("type", type, "id", id, "dataRecord", dataRecord, "storageTerms", storageTerms,
				"links", links, "dataDivider", dataDivider);
	}

	@Override
	public DataGroup read(List<String> types, String id) {
		return (DataGroup) MCR.addCallAndReturnFromMRV("types", types, "id", id);
	}

	@Override
	public DataRecordGroup read(String type, String id) {
		return (DataRecordGroup) MCR.addCallAndReturnFromMRV("type", type, "id", id);
	}

	@Override
	public StorageReadResult readList(String type, Filter filter) {
		return (StorageReadResult) MCR.addCallAndReturnFromMRV("type", type, "filter", filter);
	}

	@Override
	public StorageReadResult readList(List<String> types, Filter filter) {
		return (StorageReadResult) MCR.addCallAndReturnFromMRV("types", types, "filter", filter);
	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		MCR.addCall("type", type, "id", id);
	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		return (boolean) MCR.addCallAndReturnFromMRV("type", type, "id", id);
	}

	@Override
	public void update(String type, String id, DataGroup dataRecord, Set<StorageTerm> storageTerms,
			Set<Link> links, String dataDivider) {
		MCR.addCall("type", type, "id", id, "dataRecord", dataRecord, "storageTerms", storageTerms,
				"links", links, "dataDivider", dataDivider);
	}

	@Override
	public boolean recordExists(List<String> types, String id) {
		return (boolean) MCR.addCallAndReturnFromMRV("types", types, "id", id);
	}

	@Override
	public Set<Link> getLinksToRecord(String type, String id) {
		return (Set<Link>) MCR.addCallAndReturnFromMRV("type", type, "id", id);
	}

	@Override
	public Set<Link> getLinksFromRecord(String type, String id) {
		return (Set<Link>) MCR.addCallAndReturnFromMRV("type", type, "id", id);
	}

	@Override
	public Set<StorageTerm> getStorageTermsForRecord(String type, String id) {
		return (Set<StorageTerm>) MCR.addCallAndReturnFromMRV("type", type, "id", id);
	}

	@Override
	public long getTotalNumberOfRecordsForTypes(List<String> types, Filter filter) {
		return (long) MCR.addCallAndReturnFromMRV("types", types, "filter", filter);
	}

}
