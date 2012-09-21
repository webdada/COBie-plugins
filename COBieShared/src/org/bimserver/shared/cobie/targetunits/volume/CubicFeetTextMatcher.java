package org.bimserver.shared.cobie.targetunits.volume;


import org.bimserver.models.ifc2x3tc1.IfcReal;
import org.bimserver.models.ifc2x3tc1.IfcUnit;

public class CubicFeetTextMatcher extends VolumeConversionBasedUnitTextMatcher
{

	public CubicFeetTextMatcher(String searchString)
	{
		super(searchString);
	}

	@Override
	protected IfcReal getConversionFactorValue()
	{
		return VolumeStatics.Imperial.CubicFeet.CONVERSION_FACTOR;
	}

	@Override
	protected String getUnitName()
	{
		return VolumeStatics.Imperial.CubicFeet.UNIT_NAME;
	}

	@Override
	protected IfcUnit getConversionFactorUnit()
	{
		return VolumeStatics.Imperial.CubicFeet.CONVERSION_UNIT;
	}

	@Override
	protected String[] getTargetStringArray()
	{
		return VolumeStatics.Imperial.CubicFeet.UNIT_STRINGS;
	}

}
