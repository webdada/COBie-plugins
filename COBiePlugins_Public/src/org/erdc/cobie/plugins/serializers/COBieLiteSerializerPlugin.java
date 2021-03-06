package org.erdc.cobie.plugins.serializers;

import java.util.Set;

import org.bimserver.emf.Schema;
import org.bimserver.plugins.PluginConfiguration;
import org.bimserver.plugins.PluginException;
import org.bimserver.plugins.PluginManager;
import org.bimserver.plugins.serializers.Serializer;
import org.erdc.cobie.shared.bimserver.cobietab.serialization.COBieLiteSerializer;
import org.erdc.cobie.shared.bimserver.cobietab.serialization.COBieSerializerPluginInfo;

public class COBieLiteSerializerPlugin extends AbstractCOBieSerializerPlugin
{

	protected static final String VERSION = "1.1";
	protected static final boolean NEEDS_GEOMETRY = false;
	private boolean isInitialized;



	@Override
	public Serializer createSerializer(PluginConfiguration plugin)
	{
		return new COBieLiteSerializer();
	}

	@Override
	public void init(PluginManager pluginManager) throws PluginException
	{
		isInitialized = true;
	}

	@Override
	public boolean isInitialized()
	{
		return isInitialized;
	}

	@Override
	public boolean needsGeometry()
	{
		return NEEDS_GEOMETRY;
	}

	@Override
	protected COBieSerializerPluginInfo getCOBieSerializerInfo()
	{
		return COBieSerializerPluginInfo.COBIE_LITE;
	}

	@Override
	public Set<Schema> getSupportedSchemas() 
	{
		return Schema.IFC2X3TC1.toSet();
	}

}
