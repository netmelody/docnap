package org.netmelody.docnap.core.published;

public interface IDocnapStore {

	//TODO: change to File
	void setStorageLocation(String path);

	String getStorageLocation();
}