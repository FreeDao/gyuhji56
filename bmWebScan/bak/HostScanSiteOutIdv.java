package com.bmtech.spider.core.scan;

import java.io.File;
import java.io.IOException;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.bmfs.MDir;

public class HostScanSiteOut extends HostScanSiteOutItf {

	public HostScanSiteOut(HostInfo hostInfo) throws IOException {
		super(hostInfo);
	}

	@Override
	public void close() {
		MDir.closeMDir(mdir);
	}

	@Override
	protected MDir getMDir(HostInfo hostInfo) throws IOException {
		File f = ScanConfig.instance.getSaveDir(hostInfo);
		return MDir.makeMDir(f, true);
	}

}
