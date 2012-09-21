package org.bimserver.shared.cobie.targetunits.volume;


import org.bimserver.models.ifc2x3tc1.IfcReal;
import org.bimserver.models.ifc2x3tc1.IfcUnit;

public class CubicInchesTextMatcher extends
		VolumeConversionBasedUnitTextMatcher
{

	public CubicInchesTextMatcher(String searchString)
	{
		super(searchString);
	}

	@Override
	protected IfcReal getConversionFactorValue()
	{
		return VolumeStatics.Imperial.CubicInches.CONVERSION_FACTOR;
	}

	@Override
	protected String getUnitName()
	{
		return VolumeStatics.Imperial.CubicInches.UNIT_NAME;
	}

	@Override
	protected IfcUnit getConversionFactorUnit()
	{
		return VolumeStatics.Imperial.CubicInches.CONVERSION_UNIT;
	}

	@Override
	protected String[] getTargetStringArray()
	{
		return VolumeStatics.Imperial.CubicInches.UNIT_STRINGS;
	}

}
