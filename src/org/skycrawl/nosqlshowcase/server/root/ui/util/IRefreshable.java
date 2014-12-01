package org.skycrawl.nosqlshowcase.server.root.ui.util;

import org.skycrawl.nosqlshowcase.server.root.db.AbstractDataController;


public interface IRefreshable<DC extends AbstractDataController<?>>
{
	void refresh(DC dataController);
}