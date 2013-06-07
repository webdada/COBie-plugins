package org.erdc.cobie.plugins.serializers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bimserver.plugins.PluginConfiguration;
import org.bimserver.plugins.PluginException;
import org.bimserver.plugins.PluginManager;
import org.bimserver.plugins.serializers.Serializer;
import org.erdc.cobie.plugins.utils.PluginRuntimeFileHelper;
import org.erdc.cobie.shared.enums.COBieSerializerPluginInfo;

public class COBieZoneReportPlugin extends AbstractCOBieSerializerPlugin
{
	private boolean initialized = false;
	private static final String ZONE_REPORT_CSS_PATH = "lib/SpaceReport.css";
	private static final String ZONE_REPORT_XSLT_PATH = "lib/ZoneReport.xslt";
	private ArrayList<String> configFilePaths;
	private HashMap<String, File> configFiles;



	@Override
	public Serializer createSerializer(PluginConfiguration plugin)
	{

		return new org.erdc.cobie.plugins.serializers.COBieHTMLReportSerializer(
				configFiles.get(ZONE_REPORT_XSLT_PATH).getAbsolutePath(),
				configFiles.get(ZONE_REPORT_CSS_PATH).getAbsolutePath());
	}

	@Override
	public String getDefaultContentType()
	{
		return "appliction/html";
	}

	@Override
	public String getDefaultExtension()
	{
		return COBieSerializerPluginInfo.REPORT_ZONE.getFileExtension();
	}

	@Override
	public String getDefaultName()
	{
		return COBieSerializerPluginInfo.REPORT_ZONE.toString();
	}

	@Override
	public String getDescription()
	{
		return COBieSerializerPluginInfo.REPORT_ZONE.getDescription();
	}


	@Override
	public void init(PluginManager pluginManager) throws PluginException
	{
		configFilePaths = new ArrayList<String>();

		configFilePaths.add(ZONE_REPORT_XSLT_PATH);
		configFilePaths.add(ZONE_REPORT_CSS_PATH);
		pluginManager.requireSchemaDefinition();
		try
		{
			configFiles = PluginRuntimeFileHelper.prepareSerializerConfigFiles(
					pluginManager, getDefaultName(), this, configFilePaths);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new PluginException("Could not find configuration files");
		}
		initialized = true;

	}

	@Override
	public boolean isInitialized()
	{
		return initialized;
	}

	@Override
	public boolean needsGeometry()
	{
		return false;
	}

}