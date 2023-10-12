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

import java.io.InputStream;

import se.uu.ub.cora.storage.archive.ResourceArchive;
import se.uu.ub.cora.storage.archive.ResourceMetadata;
import se.uu.ub.cora.storage.archive.record.ResourceMetadataToUpdate;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class ResourceArchiveSpy implements ResourceArchive {
	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public ResourceArchiveSpy() {
		MCR.useMRV(MRV);
		MRV.setDefaultReturnValuesSupplier("create", () -> 100L);
		MRV.setDefaultReturnValuesSupplier("read", InputStreamSpy::new);
		MRV.setDefaultReturnValuesSupplier("readMetadata",
				() -> new ResourceMetadata("someFileSize", "someChecksumSHA512"));
	}

	@Override
	public void create(String dataDivider, String type, String id, InputStream resource,
			String mimeType) {
		MCR.addCall("dataDivider", dataDivider, "type", type, "id", id, "resource", resource,
				"mimeType", mimeType);
	}

	@Override
	public InputStream read(String dataDivider, String type, String id) {
		return (InputStream) MCR.addCallAndReturnFromMRV("dataDivider", dataDivider, "type", type,
				"id", id);
	}

	@Override
	public ResourceMetadata readMetadata(String dataDivider, String type, String id) {
		return (ResourceMetadata) MCR.addCallAndReturnFromMRV("dataDivider", dataDivider, "type",
				type, "id", id);
	}

	@Override
	public void updateMetadata(String dataDivider, String type, String id,
			ResourceMetadataToUpdate resourceMetadataToUpdate) {
		MCR.addCall("dataDivider", dataDivider, "type", type, "id", id, "resourceMetadataToUpdate",
				resourceMetadataToUpdate);
	}

	@Override
	public void update(String dataDivider, String type, String id, InputStream resource,
			String mimeType) {
		MCR.addCall("dataDivider", dataDivider, "type", type, "id", id, "resource", resource,
				"mimeType", mimeType);
	}

	@Override
	public void delete(String dataDivider, String type, String id) {
		MCR.addCall("dataDivider", dataDivider, "type", type, "id", id);
	}

}
