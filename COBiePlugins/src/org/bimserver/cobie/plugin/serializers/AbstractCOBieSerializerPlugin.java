package org.bimserver.cobie.plugin.serializers;

import org.bimserver.cobie.shared.serialization.COBieSerializerPluginInfo;
import org.bimserver.plugins.serializers.AbstractSerializerPlugin;
import org.bimserver.plugins.serializers.Serializer;

public abstract class AbstractCOBieSerializerPlugin extends
		AbstractSerializerPlugin
{

	public AbstractCOBieSerializerPlugin()
	{
		super();
	}

	public final Serializer createSerializer()
	{
		return createSerializer(null);
	
	}
	
	@Override
	public String getVersion()
	{
		return "1.0";
	}
	
	@Override
	public final String getDefaultExtension()
	{
		return getCOBieSerializerInfo().getFileExtension();
	}

	@Override
	public final String getDescription()
	{
		return getCOBieSerializerInfo().getDescription();
	}

	@Override
	public final String getDefaultName()
	{
		return getCOBieSerializerInfo().getPluginName();
	}
	
	

	@Override
	public final String getDefaultContentType()
	{
		return getCOBieSerializerInfo().getContentType();
	}

	protected abstract COBieSerializerPluginInfo getCOBieSerializerInfo();
}